package com.github.desperado2.data.open.api.service.impl;

/**
 * 1
 *
 * @author tu nan
 * @date 2023/2/13
 **/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApi;
import com.github.desperado2.data.open.api.api.manage.enums.ApiStatusEnum;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiService;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.entity.ApiSubscribe;
import com.github.desperado2.data.open.api.enums.ApiSubscribeStatusEnum;
import com.github.desperado2.data.open.api.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.mapper.ApiSubscribeMapper;
import com.github.desperado2.data.open.api.model.*;
import com.github.desperado2.data.open.api.service.IApiSubscribeService;
import com.github.desperado2.data.open.api.user.manage.entity.Role;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.enums.UserStatusEnum;
import com.github.desperado2.data.open.api.user.manage.service.IRoleService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author tu nan
 * @date 2023/2/13
 **/
@Service
public class ApiSubscribeServiceImpl extends ServiceImpl<ApiSubscribeMapper, ApiSubscribe> implements IApiSubscribeService {

    private final UserInfoProvider userInfoProvider;

    private final IOpenApiService openApiService;

    private final IRoleService roleService;

    private final IUserService userService;

    private final IApiApplyInfoCache apiApplyInfoCache;

    public ApiSubscribeServiceImpl(UserInfoProvider userInfoProvider,
                                   IOpenApiService openApiService,
                                   IRoleService roleService,
                                   IUserService userService,
                                   IApiApplyInfoCache apiApplyInfoCache) {
        this.userInfoProvider = userInfoProvider;
        this.openApiService = openApiService;
        this.roleService = roleService;
        this.userService = userService;
        this.apiApplyInfoCache = apiApplyInfoCache;
    }


    @Override
    public void add(ApiSubscribeRequestModel apiSubscribeRequestModel) throws DataOpenPlatformException {
        // 新增订阅
        Long apiId = apiSubscribeRequestModel.getApiId();
        OpenApi openApi = openApiService.getById(apiId);
        if(openApi == null || ApiStatusEnum.DOWN.getCode().equals(openApi.getStatus())){
            throw new DataOpenPlatformException("该接口暂不支持申请");
        }
        // 检测 是否已订阅
        Long userId = userInfoProvider.getLoginUserInfo().getId();
        if(isSubscribed(userId, apiId)){
            throw new DataOpenPlatformException("请勿重复申请");
        }
        // 订阅
        ApiSubscribe apiSubscribe = new ApiSubscribe();
        apiSubscribe.setApiId(apiId);
        apiSubscribe.setUserId(userId);
        apiSubscribe.setSubscribeStatus(ApiSubscribeStatusEnum.WAIT_APPROVAL.getCode());
        apiSubscribe.setCreateTime(new Date());
        apiSubscribe.setUpdateTime(new Date());
        this.save(apiSubscribe);
    }

    @Override
    public void subscribe(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException {
        Long subscribeId = apiSubscribeApprovalRequestModel.getSubscribeId();
        Integer status = apiSubscribeApprovalRequestModel.getStatus();
        String refuseMessage = apiSubscribeApprovalRequestModel.getRefuseMessage();
        if(Arrays.stream(ApiSubscribeStatusEnum.values()).noneMatch(it -> it.getCode().equals(status))){
            throw new DataOpenPlatformException("无效的审核状态");
        }
        ApiSubscribe apiSubscribe = new ApiSubscribe();
        apiSubscribe.setId(subscribeId);
        apiSubscribe.setSubscribeStatus(status);
        apiSubscribe.setRefuseMessage(refuseMessage);
        this.getBaseMapper().updateById(apiSubscribe);
    }

    @Override
    public String cancelSubscribe(Long subscribeId) throws DataOpenPlatformException {
        ApiSubscribe apiSubscribe = this.getBaseMapper().selectOne(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getId,subscribeId)
                .last(" limit 1"));
        if(apiSubscribe == null){
            throw new DataOpenPlatformException("申请记录不存在");
        }
        apiSubscribe.setSubscribeStatus(ApiSubscribeStatusEnum.CANCEL.getCode());
        this.getBaseMapper().updateById(apiSubscribe);
        // 移除缓存
        apiApplyInfoCache.remove(ApiApplyInfo.Builder.builder().apiId(apiSubscribe.getApiId()).userId(apiSubscribe.getUserId()).build());
        return "取消成功";
    }

    @Override
    public void disable(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException {
        Long subscribeId = apiSubscribeApprovalRequestModel.getSubscribeId();
        ApiSubscribe apiSubscribe = this.getBaseMapper().selectOne(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getId,subscribeId)
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode())
                .last(" limit 1"));
        if(apiSubscribe == null){
            throw new DataOpenPlatformException("订阅不存在");
        }
        apiSubscribe.setSubscribeStatus(ApiSubscribeStatusEnum.DISABLE.getCode());
        this.getBaseMapper().updateById(apiSubscribe);
        // 移除缓存
        apiApplyInfoCache.remove(ApiApplyInfo.Builder.builder().apiId(apiSubscribe.getApiId()).userId(apiSubscribe.getUserId()).build());

    }

    @Override
    public void approval(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException {
        Long subscribeId = apiSubscribeApprovalRequestModel.getSubscribeId();
        ApiSubscribe apiSubscribe = this.getBaseMapper().selectOne(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getId,subscribeId)
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.WAIT_APPROVAL.getCode())
                .last(" limit 1"));
        if(apiSubscribe == null){
            throw new DataOpenPlatformException("订阅不存在");
        }
        Integer status = apiSubscribeApprovalRequestModel.getStatus();
        String refuseMessage = apiSubscribeApprovalRequestModel.getRefuseMessage();
        apiSubscribe.setId(subscribeId);
        apiSubscribe.setSubscribeStatus(status);
        apiSubscribe.setRefuseMessage(refuseMessage);
        this.getBaseMapper().updateById(apiSubscribe);
        // 新增缓存
        if(ApiSubscribeStatusEnum.NORMAL.getCode().equals(status)){
            User user = userService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getStatus, UserStatusEnum.NORMAL.getCode())
                    .eq(User::getId, apiSubscribe.getUserId()));

            ApiApplyInfo.Builder builder = ApiApplyInfo.Builder.builder().apiId(apiSubscribe.getApiId())
                    .userId(apiSubscribe.getUserId());
            if(user != null){
                builder.username(user.getName());
            }
            apiApplyInfoCache.put(builder.build());
        }
    }

    @Override
    public PageResults<List<ApiSubscribeSearchResponseModel>> listByUser(ApiSubscribeSearchModel apiSubscribeSearchModel) {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        Long userId = loginUserInfo.getId();
        LambdaQueryWrapper<ApiSubscribe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ApiSubscribe::getUserId, userId);
        Integer status = apiSubscribeSearchModel.getStatus();
        if(status != null){
            lambdaQueryWrapper.eq(ApiSubscribe::getSubscribeStatus, status);
        }
        lambdaQueryWrapper.orderByDesc(ApiSubscribe::getCreateTime);
        IPage<ApiSubscribe> iPage = new Page<>(apiSubscribeSearchModel.getCurrent(), apiSubscribeSearchModel.getPageSize());
        IPage<ApiSubscribe> apiSubscribeIPage = this.getBaseMapper().selectPage(iPage, lambdaQueryWrapper);
        if(apiSubscribeIPage.getRecords().size() <= 0){
            return new PageResults<>(null, new PageInfo(apiSubscribeSearchModel.getCurrent(),apiSubscribeSearchModel.getPageSize(),apiSubscribeIPage.getTotal()));
        }

        List<Long> apiIdList = apiSubscribeIPage.getRecords().stream().map(ApiSubscribe::getApiId).collect(Collectors.toList());
        Collection<OpenApi> openApis = openApiService.listByIds(apiIdList);
        Map<Long, OpenApi> apiMap = new HashMap<>();
        openApis.forEach(it -> apiMap.put(it.getId(), it));
        // 组合数据
        List<ApiSubscribeSearchResponseModel> responseModelList = apiSubscribeIPage.getRecords().stream().map(it -> {
            ApiSubscribeSearchResponseModel apiSubscribeSearchResponseModel = new ApiSubscribeSearchResponseModel();
            apiSubscribeSearchResponseModel.setSubscribeId(it.getId());
            apiSubscribeSearchResponseModel.setStatus(it.getSubscribeStatus());
            apiSubscribeSearchResponseModel.setRefuseMessage(it.getRefuseMessage());
            apiSubscribeSearchResponseModel.setCreateTime(it.getCreateTime());
            if (apiMap.containsKey(it.getApiId())) {
                OpenApi openApi = apiMap.get(it.getApiId());
                apiSubscribeSearchResponseModel.setApiName(openApi.getName());
                apiSubscribeSearchResponseModel.setApiStatus(openApi.getStatus());
                apiSubscribeSearchResponseModel.setApiId(it.getApiId());
            }
            return apiSubscribeSearchResponseModel;
        }).collect(Collectors.toList());
        return new PageResults<>(responseModelList, new PageInfo(apiSubscribeSearchModel.getCurrent(),apiSubscribeSearchModel.getPageSize(),apiSubscribeIPage.getTotal()));
    }

    @Override
    public PageResults<List<ApiSubscribeSearchResponseModel>> listAll(ApiSubscribeSearchAllModel apiSubscribeSearchAllModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        Long roleId = loginUserInfo.getRoleId();
        Role role = roleService.getById(roleId);
        // 判断是否有权限
        if(role == null || !RoleCodeEnum.ADMIN.getCode().equals(role.getCode())){
            throw new DataOpenPlatformException("您没有权限访问");
        }
        LambdaQueryWrapper<ApiSubscribe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Integer status = apiSubscribeSearchAllModel.getStatus();
        if(status != null){
            lambdaQueryWrapper.eq(ApiSubscribe::getSubscribeStatus, status);
        }
        lambdaQueryWrapper.orderByDesc(ApiSubscribe::getCreateTime);
        IPage<ApiSubscribe> iPage = new Page<>(apiSubscribeSearchAllModel.getCurrent(), apiSubscribeSearchAllModel.getPageSize());
        IPage<ApiSubscribe> apiSubscribeIPage = this.getBaseMapper().selectPage(iPage, lambdaQueryWrapper);
        if(apiSubscribeIPage.getRecords().size() <= 0){
            return new PageResults<>(null, new PageInfo(apiSubscribeSearchAllModel.getCurrent(),apiSubscribeSearchAllModel.getPageSize(),apiSubscribeIPage.getTotal()));
        }
        // 查询API
        List<Long> apiIdList = apiSubscribeIPage.getRecords().stream().map(ApiSubscribe::getApiId).distinct().collect(Collectors.toList());
        Collection<OpenApi> openApis = openApiService.listByIds(apiIdList);
        Map<Long, OpenApi> apiMap = new HashMap<>();
        openApis.forEach(it -> apiMap.put(it.getId(), it));

        // 查询用户
        List<Long> userIdList = apiSubscribeIPage.getRecords().stream().map(ApiSubscribe::getUserId).distinct().collect(Collectors.toList());
        List<User> users = userService.getBaseMapper().selectBatchIds(userIdList);
        Map<Long, User> userMap = new HashMap<>();
        users.forEach(it -> userMap.put(it.getId(), it));
        // 组合数据
        List<ApiSubscribeSearchResponseModel> responseModelList = apiSubscribeIPage.getRecords().stream().map(it -> {
            ApiSubscribeSearchResponseModel apiSubscribeSearchResponseModel = new ApiSubscribeSearchResponseModel();
            apiSubscribeSearchResponseModel.setSubscribeId(it.getId());
            apiSubscribeSearchResponseModel.setStatus(it.getSubscribeStatus());
            apiSubscribeSearchResponseModel.setRefuseMessage(it.getRefuseMessage());
            apiSubscribeSearchResponseModel.setCreateTime(it.getCreateTime());
            if (apiMap.containsKey(it.getApiId())) {
                OpenApi openApi = apiMap.get(it.getApiId());
                apiSubscribeSearchResponseModel.setApiName(openApi.getName());
                apiSubscribeSearchResponseModel.setApiStatus(openApi.getStatus());
                apiSubscribeSearchResponseModel.setApiId(it.getApiId());
            }
            if(userMap.containsKey(it.getUserId())){
                apiSubscribeSearchResponseModel.setUserId(it.getUserId());
                apiSubscribeSearchResponseModel.setUsername(userMap.get(it.getUserId()).getName());
            }
            return apiSubscribeSearchResponseModel;
        }).collect(Collectors.toList());
        return new PageResults<>(responseModelList, new PageInfo(apiSubscribeSearchAllModel.getCurrent(),apiSubscribeSearchAllModel.getPageSize(),apiSubscribeIPage.getTotal()));

    }

    @Override
    public List<ApiSubscribe> getAllEnableList() {
        return this.getBaseMapper().selectList(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode()));
    }

    @Override
    public Integer normalCount() {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode()));
    }

    @Override
    public Integer subscribeCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getUserId, userId));
    }

    @Override
    public Integer normalCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getUserId, userId)
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode()));
    }

    @Override
    public Integer noNormalCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getUserId, userId)
                .ne(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode()));
    }

    @Override
    public Integer approvalingCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getUserId, userId)
                .ne(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode()));
    }


    /**
     * 是否已经订阅
     * @param userId 用户id
     * @param apiId 接口id
     * @return 订阅 true  未订阅 false
     */
    private boolean isSubscribed(Long userId, Long apiId){
        ApiSubscribe apiSubscribe = this.getBaseMapper().selectOne(new LambdaQueryWrapper<ApiSubscribe>()
                .eq(ApiSubscribe::getUserId, userId)
                .eq(ApiSubscribe::getApiId, apiId)
                .eq(ApiSubscribe::getSubscribeStatus, ApiSubscribeStatusEnum.NORMAL.getCode())
                .last(" limit 1"));
        return apiSubscribe != null;
    }
}
