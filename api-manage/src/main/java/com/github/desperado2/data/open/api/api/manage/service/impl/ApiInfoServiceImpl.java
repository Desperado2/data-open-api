package com.github.desperado2.data.open.api.api.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApi;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiDetails;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiRequestParams;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiScriptsRelease;
import com.github.desperado2.data.open.api.api.manage.enums.ApiCustomResponseStructureEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiRequestParamRequiredEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiScriptsReleaseStatusEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiStatusEnum;
import com.github.desperado2.data.open.api.api.manage.model.NewOldApiInfo;
import com.github.desperado2.data.open.api.api.manage.service.*;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.engine.manage.RequestMappingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务
 * @author tu nan
 * @date 2023/3/13
 **/
@Service
public class ApiInfoServiceImpl implements IApiInfoService {

    private final IOpenApiService openApiService;

    private final IOpenApiDetailsService openApiDetailsService;

    private final IOpenApiRequestParamService openApiRequestParamService;

    private final IOpenApiScriptsReleaseService openApiScriptsReleaseService;

    private final IApiInfoCache apiInfoCache;

    private final RequestMappingService requestMappingService;

    public ApiInfoServiceImpl(IOpenApiService openApiService, IOpenApiDetailsService openApiDetailsService, IOpenApiRequestParamService openApiRequestParamService, IOpenApiScriptsReleaseService openApiScriptsReleaseService, IApiInfoCache apiInfoCache, RequestMappingService requestMappingService) {
        this.openApiService = openApiService;
        this.openApiDetailsService = openApiDetailsService;
        this.openApiRequestParamService = openApiRequestParamService;
        this.openApiScriptsReleaseService = openApiScriptsReleaseService;
        this.apiInfoCache = apiInfoCache;
        this.requestMappingService = requestMappingService;
    }

    @Override
    public void reLoadApiInfo(Boolean isStart) throws NoSuchMethodException {
        // 查询所有的API
        List<OpenApi> apiList = openApiService.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApi>()
                .ne(OpenApi::getStatus, ApiStatusEnum.DOWN.getCode()));
        if(apiList.isEmpty()){
            return;
        }
        // 查询详情
        List<Long> apiIdList = apiList.stream().map(OpenApi::getId).collect(Collectors.toList());
        List<OpenApiDetails> openApiDetailsList = openApiDetailsService.list(new LambdaQueryWrapper<OpenApiDetails>().in(OpenApiDetails::getApiId, apiIdList));
        if(openApiDetailsList.isEmpty()){
            return;
        }
        // 查询参数
        List<OpenApiRequestParams> apiRequestParamsList = openApiRequestParamService.list(new LambdaQueryWrapper<OpenApiRequestParams>().in(OpenApiRequestParams::getApiId, apiIdList)
                .eq(OpenApiRequestParams::getRequired, ApiRequestParamRequiredEnum.YES.getCode()));

        // 查询脚本
        List<OpenApiScriptsRelease> scriptList = openApiScriptsReleaseService.list(new LambdaQueryWrapper<OpenApiScriptsRelease>()
                .in(OpenApiScriptsRelease::getApiId, apiIdList)
                .eq(OpenApiScriptsRelease::getApiScriptStatus, ApiScriptsReleaseStatusEnum.PUBLISH.getCode()));

        List<NewOldApiInfo> newOldApiInfoList = new ArrayList<>();
        // 组合API
        openApiDetailsList.forEach(detail -> {
            List<OpenApiScriptsRelease> collect = scriptList.stream().filter(it -> detail.getApiId().equals(it.getApiId())).collect(Collectors.toList());
            if(!collect.isEmpty()){
                OpenApiScriptsRelease openApiScriptsRelease = collect.stream().max(Comparator.comparing(OpenApiScriptsRelease::getCreateTime)).orElse(null);
                Integer customResultStructure = openApiScriptsRelease.getCustomResultStructure();

                List<String> requiredParamList = apiRequestParamsList.stream().filter(it -> it.getApiId().equals(detail.getApiId())).map(OpenApiRequestParams::getName).collect(Collectors.toList());

                OpenApi openApi = apiList.stream().filter(it -> it.getId().equals(detail.getApiId())).findFirst().orElse(new OpenApi());
                ApiInfo apiInfo = ApiInfo.Builder.builder()
                        .apiName(openApi.getName())
                        .id(detail.getApiId())
                        .path(detail.getApiAddress())
                        .testDatasource(openApiScriptsRelease.getTestDatasourceCode())
                        .preDatasource(openApiScriptsRelease.getPreDatasourceCode())
                        .prodDatasource(openApiScriptsRelease.getProdDatasourceCode())
                        .method(detail.getRequestMode())
                        .type(openApiScriptsRelease.getApiScriptType())
                        .requestParamsList(requiredParamList)
                        .script(openApiScriptsRelease.getApiScript())
                        .responseFormat(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure) ?
                                JSON.parseObject(openApiScriptsRelease.getApiResponseFormat()): null)
                        .optionData(JSON.parseObject(openApiScriptsRelease.getApiOption()))
                        .build();
                newOldApiInfoList.add(new NewOldApiInfo(detail.getApiId(), apiInfo, null));
            }
        });
        if(scriptList.isEmpty()){
            return;
        }
        Collection<ApiInfo> cacheApiInfoList = apiInfoCache.getAll();
        cacheApiInfoList.forEach(cacheItem -> {
            NewOldApiInfo newOldApiInfo = newOldApiInfoList.stream().filter(dbItem -> dbItem.getApiInfoId().equals(cacheItem.getId())).findFirst().orElse(null);
            if(newOldApiInfo != null){
                newOldApiInfo.setOldApiInfo(cacheItem);
            }else{
                newOldApiInfo = new NewOldApiInfo(cacheItem.getId(), null, cacheItem);
                newOldApiInfoList.add(newOldApiInfo);
            }
        });
        for (NewOldApiInfo item : newOldApiInfoList) {
            if(item.getOldApiInfo() != null){
                requestMappingService.unregisterMappingForApiInfo(item.getOldApiInfo());
                apiInfoCache.remove(item.getOldApiInfo());
            }
            if(item.getNewApiInfo() != null){
                requestMappingService.registerMappingForApiInfo(item.getNewApiInfo());
                apiInfoCache.put(item.getNewApiInfo());
            }
        }

    }
}
