package com.github.desperado2.data.open.api.authentication.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.authentication.manage.entity.KeySecret;
import com.github.desperado2.data.open.api.authentication.manage.enums.KeySecretStatusEnum;
import com.github.desperado2.data.open.api.authentication.manage.mapper.KeySecretMapper;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretModel;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretQueryRequest;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretRequest;
import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.common.manage.utils.UUIDUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * key管理
 *
 * @author tu nan
 * @date 2023/2/7
 **/
@Service
public class KeySecretServiceImpl extends ServiceImpl<KeySecretMapper, KeySecret> implements IKeySecretService {

    private final UserInfoProvider userInfoProvider;

    private final IKeySecretCache keySecretCache;

    public KeySecretServiceImpl(UserInfoProvider userInfoProvider, IKeySecretCache keySecretCache) {
        this.userInfoProvider = userInfoProvider;
        this.keySecretCache = keySecretCache;
    }

    public void add(KeySecretRequest keySecretRequest) {
        KeySecret keySecret = new KeySecret();
        keySecret.setAppKey(keySecretRequest.getAppKey());
        keySecret.setAppSecret(keySecretRequest.getAppSecret());
        keySecret.setUserId(keySecretRequest.getUserId());
        keySecret.setStatus(keySecretRequest.getStatus());
        keySecret.setCreateTime(new Date());
        keySecret.setUpdateTime(new Date());
        this.save(keySecret);
        if(KeySecretStatusEnum.NORMAL.getCode().equals(keySecret.getStatus())){
            keySecretCache.put(KeySecretInfo.Builder.builder()
                    .id(keySecret.getId())
                    .appKey(keySecret.getAppKey())
                    .appSecret(keySecret.getAppSecret())
                    .userId(keySecret.getUserId()).build());
        }

    }

    @Override
    public void generateKeySecret(Long userId) {
        // 生成
        // 查询是否已经存在
        List<KeySecretModel> keySecretList = this.getKeySecretByUserId(userId);
        if(keySecretList != null && keySecretList.size() > 0){
            // 全部置为过期
            List<KeySecret> keySecrets = keySecretList.stream()
                    .map(it -> new KeySecret(it.getId(), KeySecretStatusEnum.DEACTIVATE.getCode(), new Date()))
                    .collect(Collectors.toList());
            keySecretList.forEach(it -> keySecretCache.remove(KeySecretInfo.Builder.builder().appKey(it.getAppKey()).build()));
            this.updateBatchById(keySecrets);
        }
        // 生成新的key
        String key = UUIDUtils.getUUID();
        // 生成信息的secret
        String secret = UUIDUtils.getUUID() + System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(10);
        // 加密
        String encryptSecret = AESUtils.encrypt(secret, null);
        KeySecretRequest keySecretRequest = new KeySecretRequest();
        keySecretRequest.setAppKey(key);
        keySecretRequest.setAppSecret(encryptSecret);
        keySecretRequest.setStatus(KeySecretStatusEnum.NORMAL.getCode());
        keySecretRequest.setUserId(userId);
        add(keySecretRequest);
    }

    public void update(KeySecretRequest keySecretRequest){
        // 更新
        String appKey = keySecretRequest.getAppKey();
        KeySecretModel keySecretByAppKey = this.getKeySecretByAppKey(appKey);
        // 循环更新
        KeySecret keySecret = new KeySecret();
        keySecret.setId(keySecretByAppKey.getId());
        keySecret.setAppKey(keySecretRequest.getAppKey());
        keySecret.setAppSecret(keySecretRequest.getAppSecret());
        keySecret.setUserId(keySecretRequest.getUserId());
        keySecret.setStatus(keySecretRequest.getStatus());

        if(KeySecretStatusEnum.NORMAL.getCode().equals(keySecret.getStatus())){
            keySecretCache.put(KeySecretInfo.Builder.builder()
                    .id(keySecret.getId())
                    .appKey(keySecret.getAppKey())
                    .appSecret(keySecret.getAppSecret())
                    .userId(keySecret.getUserId()).build());
        }
        this.updateById(keySecret);
    }

    public KeySecretModel getKeySecretByAppKey(String appKey) {
        KeySecret keySecrets = this.getBaseMapper().selectOne(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getAppKey, appKey)
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode())
                .orderByDesc(KeySecret::getCreateTime)
                .last(" limit 1"));
        return BeanUtil.sourceToTarget(keySecrets, KeySecretModel.class);
    }

    @Override
    public List<KeySecretModel> getKeySecretAllEnable() {
          List<KeySecret> keySecrets = this.getBaseMapper().selectList(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode()));
        return keySecrets.stream().map(it -> JSONObject.parseObject(JSONObject.toJSONString(it), KeySecretModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getSecretByAppKey(String appKey) {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        List<KeySecret> keySecrets = this.getBaseMapper().selectList(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getAppKey, appKey)
                .eq(KeySecret::getUserId, loginUserInfo.getId())
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode()));
        if(keySecrets.size() > 0){
            return AESUtils.decrypt(keySecrets.get(0).getAppSecret(), null);
        }
        return null;
    }

    @Override
    @Transactional
    public String reset(Long userId) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        if(userId == null){
            userId = loginUserInfo.getId();
        }else{
            if(!RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){
                throw new DataOpenPlatformException("您无权操作");
            }
        }
        // 2.重新生成
        generateKeySecret(userId);
        return "重置成功";
    }

    public List<KeySecretModel> getKeySecretByUserId(Long userId) {
        List<KeySecret> keySecrets = this.getBaseMapper().selectList(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getUserId, userId)
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode()));
        return keySecrets.stream().map(it -> {
                    KeySecretModel keySecretModel = JSONObject.parseObject(JSONObject.toJSONString(it), KeySecretModel.class);
                    keySecretModel.setAppSecret(null);
                    return keySecretModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public KeySecret keySecretByUserId(Long userId) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getUserId, userId)
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode())
                .last(" limit 1"));
    }

    @Override
    public List<KeySecretModel> getKeySecretByUser(Long userId) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        if(userId == null){
            userId = loginUserInfo.getId();
        }else{
            if(!RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){
                throw new DataOpenPlatformException("您无权操作");
            }
        }
        List<KeySecret> keySecrets = this.getBaseMapper().selectList(new LambdaQueryWrapper<KeySecret>()
                .eq(KeySecret::getUserId, userId)
                .eq(KeySecret::getStatus, KeySecretStatusEnum.NORMAL.getCode()));
        return keySecrets.stream().map(it -> {
                    KeySecretModel keySecretModel = JSONObject.parseObject(JSONObject.toJSONString(it), KeySecretModel.class);
                    if(!RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){
                        keySecretModel.setAppSecret(null);
                    }else{
                        keySecretModel.setAppSecret( AESUtils.decrypt(keySecretModel.getAppSecret(), null));
                    }
                    return keySecretModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageResults<List<KeySecretModel>> listAll(KeySecretQueryRequest keySecretQueryRequest) {
        LambdaQueryWrapper<KeySecret> wapper = new LambdaQueryWrapper<>();
        wapper.orderByDesc(KeySecret::getCreateTime);

        String appKey = keySecretQueryRequest.getAppKey();
        if(StringUtils.isNotBlank(appKey)){
            wapper.eq(KeySecret::getAppKey, appKey);
        }
        Integer status = keySecretQueryRequest.getStatus();
        if(status != null){
            wapper.eq(KeySecret::getStatus, status);
        }
        // 查询
        Integer current = keySecretQueryRequest.getCurrent();
        Integer pageSize = keySecretQueryRequest.getPageSize();
        IPage<KeySecret> pageList = this.getBaseMapper().selectPage(new Page<>(current, pageSize), wapper);
        PageResults<List<KeySecretModel>> result = new PageResults<>();
        // 转换结果
        PageInfo pageInfo = new PageInfo(current, pageSize, pageList.getTotal());
        List<KeySecretModel> responseModelList = pageList.getRecords().stream()
                .map(it ->  {
                    KeySecretModel keySecretModel = JSONObject.parseObject(JSONObject.toJSONString(it), KeySecretModel.class);
                    keySecretModel.setAppSecret(null);
                    return keySecretModel;
                })
                .collect(Collectors.toList());
        result.setPagination(pageInfo);
        result.setList(responseModelList);
        return result;
    }


    public PageResults<List<KeySecretModel>> list(KeySecretQueryRequest keySecretQueryRequest) {
        LambdaQueryWrapper<KeySecret> wapper = new LambdaQueryWrapper<>();
        wapper.orderByDesc(KeySecret::getCreateTime);

        String appKey = keySecretQueryRequest.getAppKey();
        if(StringUtils.isNotBlank(appKey)){
            wapper.eq(KeySecret::getAppKey, appKey);
        }
        Integer status = keySecretQueryRequest.getStatus();
        if(status != null){
            wapper.eq(KeySecret::getStatus, status);
        }
        // 查询
        Integer current = keySecretQueryRequest.getCurrent();
        Integer pageSize = keySecretQueryRequest.getPageSize();
        IPage<KeySecret> pageList = this.getBaseMapper().selectPage(new Page<>(current, pageSize), wapper);
        PageResults<List<KeySecretModel>> result = new PageResults<>();
        // 转换结果
        PageInfo pageInfo = new PageInfo(current, pageSize, pageList.getTotal());
        List<KeySecretModel> responseModelList = pageList.getRecords().stream()
                .map(it ->  {
                    KeySecretModel keySecretModel = JSONObject.parseObject(JSONObject.toJSONString(it), KeySecretModel.class);
                    keySecretModel.setAppSecret(null);
                    return keySecretModel;
                })
                .collect(Collectors.toList());
        result.setPagination(pageInfo);
        result.setList(responseModelList);
        return result;
    }
}
