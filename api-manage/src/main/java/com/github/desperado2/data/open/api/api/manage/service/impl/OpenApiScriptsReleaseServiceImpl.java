package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.*;
import com.github.desperado2.data.open.api.api.manage.enums.ApiCustomResponseStructureEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiRequestParamRequiredEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiScriptsReleaseStatusEnum;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiScriptsReleaseMapper;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsReleaseList;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsRequest;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiDetailsService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiRequestParamService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiScriptsReleaseService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiService;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.engine.manage.RequestMappingService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiScriptsReleaseServiceImpl extends ServiceImpl<OpenApiScriptsReleaseMapper, OpenApiScriptsRelease> implements IOpenApiScriptsReleaseService {

    private final UserInfoProvider userInfoProvider;

    private final IOpenApiService openApiService;

    private final IOpenApiDetailsService openApiDetailsService;

    private final IOpenApiRequestParamService openApiRequestParamService;

    private final RequestMappingService requestMappingService;

    private final IApiInfoCache apiInfoCache;

    public OpenApiScriptsReleaseServiceImpl(UserInfoProvider userInfoProvider, IOpenApiService openApiService, IOpenApiDetailsService openApiDetailsService, IOpenApiRequestParamService openApiRequestParamService, RequestMappingService requestMappingService, IApiInfoCache apiInfoCache) {
        this.userInfoProvider = userInfoProvider;
        this.openApiService = openApiService;
        this.openApiDetailsService = openApiDetailsService;
        this.openApiRequestParamService = openApiRequestParamService;
        this.requestMappingService = requestMappingService;
        this.apiInfoCache = apiInfoCache;
    }


    @Override
    public void add(OpenApiScripts openApiScripts) throws DataOpenPlatformException, NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }

        // 下线历史
        Long apiId = openApiScripts.getApiId();
        OpenApi openApi = openApiService.getById(apiId);
        OpenApiDetails openApiDetails = openApiDetailsService.getOpenApiDetailsByApiId(apiId);
        // 获取参数
        List<OpenApiRequestParams> apiRequestParamsList = openApiRequestParamService.getApiRequestParamsByApiId(apiId);

        // 上线
        List<String> requiredParamList = apiRequestParamsList.stream().filter(it -> ApiRequestParamRequiredEnum.YES.getCode().equals(it.getRequired()))
                .map(OpenApiRequestParams::getName).collect(Collectors.toList());
        Integer customResultStructure = openApiScripts.getCustomResultStructure();
        ApiInfo apiInfo = ApiInfo.Builder.builder()
                .id(openApiDetails.getApiId())
                .apiName(openApi.getName())
                .path(openApiDetails.getApiAddress())
                .method(openApiDetails.getRequestMode())
                .script(openApiScripts.getApiScript())
                .requestParamsList(requiredParamList)
                .type(openApiScripts.getApiScriptType())
                .testDatasource(openApiScripts.getTestDatasourceCode())
                .preDatasource(openApiScripts.getPreDatasourceCode())
                .prodDatasource(openApiScripts.getProdDatasourceCode())
                .responseFormat(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure) ?
                JSON.parseObject(openApiScripts.getApiResponseFormat()): null)
                .build();
        apiInfoCache.put(apiInfo);
        requestMappingService.registerMappingForApiInfo(apiInfo);
        // 新增
        OpenApiScriptsRelease openApiScriptsRelease = BeanUtil.sourceToTarget(openApiScripts, OpenApiScriptsRelease.class);
        openApiScriptsRelease.setApiScriptStatus(ApiScriptsReleaseStatusEnum.PUBLISH.getCode());
        openApiScriptsRelease.setCreateTime(new Date());
        openApiScriptsRelease.setUpdateTime(new Date());
        openApiScriptsRelease.setId(null);
        this.getBaseMapper().insert(openApiScriptsRelease);
    }

    @Override
    public void offline(OpenApiScripts openApiScripts) throws DataOpenPlatformException,NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }

        // 下线历史
        Long apiId = openApiScripts.getApiId();
        OpenApiDetails openApiDetails = openApiDetailsService.getOpenApiDetailsByApiId(apiId);

        List<OpenApiScriptsRelease> openApiScriptsReleases = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiScriptsRelease>()
                .eq(OpenApiScriptsRelease::getApiId, apiId)
                .eq(OpenApiScriptsRelease::getApiScriptStatus, ApiScriptsReleaseStatusEnum.PUBLISH));
        for (OpenApiScriptsRelease it : openApiScriptsReleases) {// 下线
            ApiInfo apiInfo = ApiInfo.Builder.builder()
                    .path(openApiDetails.getApiAddress())
                    .method(openApiDetails.getRequestMode())
                    .script(it.getApiScript())
                    .build();
            requestMappingService.unregisterMappingForApiInfo(apiInfo);
            apiInfoCache.remove(apiInfo);
            // 更新
            it.setUpdateTime(new Date());
            it.setApiScriptStatus(ApiScriptsReleaseStatusEnum.NOT_PUBLISH.getCode());
            this.updateById(it);
        }
    }

    @Override
    public List<OpenApiScriptsReleaseList> apiHistory(Long apiId) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        List<OpenApiScriptsRelease> openApiScriptsReleases = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiScriptsRelease>()
                .eq(OpenApiScriptsRelease::getApiId, apiId)
                .orderByDesc(OpenApiScriptsRelease::getCreateTime));
        return openApiScriptsReleases.stream().map(it -> BeanUtil.sourceToTarget(it, OpenApiScriptsReleaseList.class)).collect(Collectors.toList());
    }

    @Override
    public OpenApiScriptsRequest apiHistoryInfo(Long id) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApiScriptsRelease openApiScriptsRelease = this.getBaseMapper().selectById(id);
        OpenApiScriptsRequest openApiScriptsRequest = null;
        if(openApiScriptsRelease != null){
            openApiScriptsRequest = BeanUtil.sourceToTarget(openApiScriptsRelease, OpenApiScriptsRequest.class);
            openApiScriptsRequest.setApiRequestHeader(JSONArray.parseObject(openApiScriptsRelease.getApiRequestHeader(), List.class));
            openApiScriptsRequest.setApiRequestBody(JSON.parseObject(openApiScriptsRelease.getApiRequestBody()));
            openApiScriptsRequest.setApiResponseHeader(JSONArray.parseObject(openApiScriptsRelease.getApiResponseHeader(), List.class));
            openApiScriptsRequest.setApiResponseBody(JSON.parseObject(openApiScriptsRelease.getApiResponseBody()));
            openApiScriptsRequest.setApiResponseFormat(JSON.parseObject(openApiScriptsRelease.getApiResponseFormat()));
            openApiScriptsRequest.setApiOption(JSON.parseObject(openApiScriptsRelease.getApiOption()));
        }
        return openApiScriptsRequest;
    }
}
