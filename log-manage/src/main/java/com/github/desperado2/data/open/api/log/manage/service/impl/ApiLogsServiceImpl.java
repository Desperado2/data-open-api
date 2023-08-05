package com.github.desperado2.data.open.api.log.manage.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.log.manage.entity.ApiLogs;
import com.github.desperado2.data.open.api.log.manage.enums.RequestStatusEnums;
import com.github.desperado2.data.open.api.log.manage.mapper.ApiLogsMapper;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogSearchModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogStatisticsModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogsModel;
import com.github.desperado2.data.open.api.log.manage.service.IApiLogsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class ApiLogsServiceImpl extends ServiceImpl<ApiLogsMapper, ApiLogs> implements IApiLogsService {

    private final UserInfoProvider userInfoProvider;

    public ApiLogsServiceImpl(UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }

    @Override
    public void add(ApiLogsModel apiLogsModel) {
        // 新增日志
        ApiLogs apiLogs = new ApiLogs();
        apiLogs.setApiId(apiLogsModel.getApiId());
        apiLogs.setLogKey(apiLogsModel.getLogKey());
        apiLogs.setAppKey(apiLogsModel.getAppKey());
        apiLogs.setApiName(apiLogsModel.getApiName());
        apiLogs.setUserId(apiLogsModel.getUserId());
        apiLogs.setUsername(apiLogsModel.getUsername());
        apiLogs.setRequestTime(apiLogsModel.getRequestTime());
        apiLogs.setRequestParams(apiLogsModel.getRequestParams());
        apiLogs.setResultCount(apiLogsModel.getResultCount());
        apiLogs.setErrorMsg(apiLogsModel.getErrorMsg());
        apiLogs.setStatus(apiLogsModel.getStatus());
        apiLogs.setCreateTime(new Date());
        this.save(apiLogs);
    }

    @Override
    public ApiLogs getByKey(String logKey) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<ApiLogs>().eq(ApiLogs::getLogKey, logKey)
                .last(" limit 1"));
    }

    @Override
    public PageResults<List<ApiLogsModel>> selectByApi(Long apiId) {
//        LambdaQueryWrapper<ApiLogs> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ApiLogs::getApiId, apiId);
//        IPage<ApiLogs> page = new Page<>()
        return null;
    }

    @Override
    public PageResults<List<ApiLogsModel>> listByUser(ApiLogSearchModel apiLogSearchModel) {
        LambdaQueryWrapper<ApiLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ApiLogs::getCreateTime);
        if(apiLogSearchModel.getApiId() != null){
            queryWrapper.eq(ApiLogs::getApiId, apiLogSearchModel.getApiId());
        }
        if(StringUtils.isNotBlank(apiLogSearchModel.getName())){
            queryWrapper.like(ApiLogs::getApiName, apiLogSearchModel.getName());
        }
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        queryWrapper.eq(ApiLogs::getUserId, loginUserInfo.getId());
        IPage<ApiLogs> page = new Page<>(apiLogSearchModel.getCurrent(), apiLogSearchModel.getPageSize());
        IPage<ApiLogs> apiLogsIPage = this.getBaseMapper().selectPage(page, queryWrapper);
        if (apiLogsIPage.getRecords().size() <= 0) {
            return new PageResults<>(null, new PageInfo(apiLogSearchModel.getCurrent(),
                    apiLogSearchModel.getPageSize(), apiLogsIPage.getTotal()));
        }
        List<ApiLogsModel> apiLogsModels = apiLogsIPage.getRecords().stream()
                .map(it -> JSONObject.parseObject(JSONObject.toJSONString(it), ApiLogsModel.class))
                .collect(Collectors.toList());

        return new PageResults<>(apiLogsModels, new PageInfo(apiLogSearchModel.getCurrent(),
                apiLogSearchModel.getPageSize(), apiLogsIPage.getTotal()));
    }

    @Override
    public PageResults<List<ApiLogsModel>> listAll(ApiLogSearchModel apiLogSearchModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        if(!RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(),ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        LambdaQueryWrapper<ApiLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ApiLogs::getCreateTime);
        if(apiLogSearchModel.getApiId() != null){
            queryWrapper.eq(ApiLogs::getApiId, apiLogSearchModel.getApiId());
        }
        if(StringUtils.isNotBlank(apiLogSearchModel.getName())){
            queryWrapper.and(it ->
                    it.like(ApiLogs::getApiName, apiLogSearchModel.getName())
                            .or()
                            .like(ApiLogs::getUsername, apiLogSearchModel.getName()));
        }
        IPage<ApiLogs> page = new Page<>(apiLogSearchModel.getCurrent(), apiLogSearchModel.getPageSize());
        IPage<ApiLogs> apiLogsIPage = this.getBaseMapper().selectPage(page, queryWrapper);
        if (apiLogsIPage.getRecords().size() <= 0) {
            return new PageResults<>(null, new PageInfo(apiLogSearchModel.getCurrent(),
                    apiLogSearchModel.getPageSize(), apiLogsIPage.getTotal()));
        }
        List<ApiLogsModel> apiLogsModels = apiLogsIPage.getRecords().stream()
                .map(it -> JSONObject.parseObject(JSONObject.toJSONString(it), ApiLogsModel.class))
                .collect(Collectors.toList());

        return new PageResults<>(apiLogsModels, new PageInfo(apiLogSearchModel.getCurrent(),
                apiLogSearchModel.getPageSize(), apiLogsIPage.getTotal()));
    }

    @Override
    public Integer normalCount() {
        return this.getBaseMapper().selectCount(null);
    }

    @Override
    public Integer successCount() {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiLogs>()
                .eq(ApiLogs::getStatus, RequestStatusEnums.SUCCESS.getCode()));
    }

    @Override
    public Integer failCount() {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiLogs>()
                .eq(ApiLogs::getStatus, RequestStatusEnums.FAIL.getCode()));
    }

    @Override
    public Integer normalCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiLogs>()
                .eq(ApiLogs::getUserId, userId));
    }

    @Override
    public Integer successCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiLogs>()
                .eq(ApiLogs::getUserId, userId)
                .eq(ApiLogs::getStatus, RequestStatusEnums.SUCCESS.getCode()));
    }

    @Override
    public Integer failCountByUserId(Long userId) {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<ApiLogs>()
                .eq(ApiLogs::getUserId, userId)
                .eq(ApiLogs::getStatus, RequestStatusEnums.FAIL.getCode()));
    }

    @Override
    public List<ApiLogStatisticsModel> logStatistics() {
        return this.getBaseMapper().logStatistics();
    }

    @Override
    public List<ApiLogStatisticsModel> logStatisticsByUserId(Long userId) {
        return this.getBaseMapper().logStatisticsByUser(userId);
    }
}
