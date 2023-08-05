package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.*;
import com.github.desperado2.data.open.api.api.manage.enums.ApiCustomResponseStructureEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiOpenApplyStatusEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiStatusEnum;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiMapper;
import com.github.desperado2.data.open.api.api.manage.model.*;
import com.github.desperado2.data.open.api.api.manage.service.*;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.engine.manage.result.ReturnValueTransform;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiServiceImpl extends ServiceImpl<OpenApiMapper, OpenApi> implements IOpenApiService {

    private final IOpenApiDetailsService openApiDetailsService;

    private final IOpenApiRequestParamService openApiRequestParamService;

    private final IOpenApiResponseService openApiResponseService;

    private final IOpenApiClassifyService openApiClassifyService;

    private final IOpenApiScriptsService openApiScriptsService;

    private final UserInfoProvider userInfoProvider;

    private final OpenApiProperties openApiProperties;

    private final IApiInfoCache apiInfoCache;

    public OpenApiServiceImpl(IOpenApiDetailsService openApiDetailsService,
                              IOpenApiRequestParamService openApiRequestParamService,
                              IOpenApiResponseService openApiResponseService,
                              @Lazy IOpenApiClassifyService openApiClassifyService,
                              @Lazy IOpenApiScriptsService openApiScriptsService,
                              UserInfoProvider userInfoProvider,
                              OpenApiProperties openApiProperties,
                              IApiInfoCache apiInfoCache) {
        this.openApiDetailsService = openApiDetailsService;
        this.openApiRequestParamService = openApiRequestParamService;
        this.openApiResponseService = openApiResponseService;
        this.openApiClassifyService = openApiClassifyService;
        this.openApiScriptsService = openApiScriptsService;
        this.userInfoProvider = userInfoProvider;
        this.openApiProperties = openApiProperties;
        this.apiInfoCache = apiInfoCache;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApi openApi = new OpenApi();
        openApi.setName(openApiRequestModel.getName());
        openApi.setClassifyId(openApiRequestModel.getClassifyId());
        openApi.setDescription(openApiRequestModel.getDescription());
        openApi.setStatus(ApiStatusEnum.DOWN.getCode());
        openApi.setImageUrl(openApiRequestModel.getImageUrl());
        openApi.setOpenApplyStatus(openApiRequestModel.getOpenApplyStatus());
        openApi.setNotOpenApplyReason(openApiRequestModel.getNotOpenApplyReason());
        openApi.setCreateTime(new Date());
        openApi.setUpdateTime(new Date());
        this.getBaseMapper().insert(openApi);
        // 详情
        OpenApiDetailsRequestModel openApiDetailsRequestModel = openApiRequestModel.getOpenApiDetailsRequestModel();
        openApiDetailsRequestModel.setApiId(openApi.getId());
        openApiDetailsService.add(openApiDetailsRequestModel);
        // 请求参数
        List<OpenApiRequestParamsRequestModel> openApiRequestParamsRequestModelList = openApiRequestModel.getOpenApiRequestParamsRequestModelList();
        openApiRequestParamsRequestModelList.forEach(it -> it.setApiId(openApi.getId()));
        openApiRequestParamService.batchAdd(openApiRequestParamsRequestModelList);
        // 响应参数
        List<OpenApiResponseRequestModel> openApiResponseRequestModelList = openApiRequestModel.getOpenApiResponseRequestModelList();
        openApiResponseRequestModelList.forEach(it -> it.setApiId(openApi.getId()));
        openApiResponseService.batchAdd(openApiResponseRequestModelList);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 根据id查询
        OpenApi openApi = this.getBaseMapper().selectById(openApiRequestModel.getId());
        if(openApi == null){
            throw new DataOpenPlatformException("API不存在");
        }
        openApi.setName(openApiRequestModel.getName());
        openApi.setClassifyId(openApiRequestModel.getClassifyId());
        openApi.setDescription(openApiRequestModel.getDescription());
        openApi.setImageUrl(openApiRequestModel.getImageUrl());
        openApi.setOpenApplyStatus(openApiRequestModel.getOpenApplyStatus());
        openApi.setNotOpenApplyReason(openApiRequestModel.getNotOpenApplyReason());
        openApi.setUpdateTime(new Date());
        this.getBaseMapper().updateById(openApi);
        // 详情
        OpenApiDetailsRequestModel openApiDetailsRequestModel = openApiRequestModel.getOpenApiDetailsRequestModel();
        openApiDetailsRequestModel.setApiId(openApi.getId());
        openApiDetailsService.update(openApi.getId(), openApiDetailsRequestModel);
        // 请求参数
        List<OpenApiRequestParamsRequestModel> openApiRequestParamsRequestModelList = openApiRequestModel.getOpenApiRequestParamsRequestModelList();
        openApiRequestParamService.batchUpdate(openApi.getId(), openApiRequestParamsRequestModelList);
        // 响应参数
        List<OpenApiResponseRequestModel> openApiResponseRequestModelList = openApiRequestModel.getOpenApiResponseRequestModelList();
        openApiResponseService.batchUpdate(openApi.getId(), openApiResponseRequestModelList);
    }

    @Override
    public Boolean isExistByClassify(Long classifyId) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApi>().eq(OpenApi::getClassifyId, classifyId).last(" limit 1")) != null;
    }

    @Override
    public PageResults<List<OpenApiListResponseModel>> pageList(OpenApiListRequestModel openApiListRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        LambdaQueryWrapper<OpenApi> wrapper = new LambdaQueryWrapper<>();
        String classifyName = openApiListRequestModel.getClassifyName();
        if(StringUtils.isNotBlank(classifyName)){
            List<OpenApiClassify> classifyByNameLike = openApiClassifyService.getClassifyByNameLike(classifyName);
            if(classifyByNameLike.size() > 0){
                wrapper.in(OpenApi::getClassifyId, classifyByNameLike.stream().map(OpenApiClassify::getId).collect(Collectors.toList()));
            }
        }
        String name = openApiListRequestModel.getName();
        if(StringUtils.isNotBlank(name)){
            wrapper.like(OpenApi::getName, name);
        }
        Integer status = openApiListRequestModel.getStatus();
        if(status != null){
            wrapper.eq(OpenApi::getStatus, status);
        }
        wrapper.orderByDesc(OpenApi::getCreateTime);
        IPage<OpenApi> page = new Page<>(openApiListRequestModel.getCurrent(), openApiListRequestModel.getPageSize());
        IPage<OpenApi> openApiIPage = this.getBaseMapper().selectPage(page, wrapper);
        PageResults<List<OpenApiListResponseModel>> results = new PageResults<>();
        results.setPagination(new PageInfo(openApiIPage.getCurrent(),openApiIPage.getSize(), openApiIPage.getTotal()));
        List<OpenApiListResponseModel> modelList = openApiIPage.getRecords().stream().map(it -> BeanUtil.sourceToTarget(it, OpenApiListResponseModel.class)).collect(Collectors.toList());
        List<Long> classifyIdList = openApiIPage.getRecords().stream().map(OpenApi::getClassifyId).distinct().collect(Collectors.toList());
        List<OpenApiClassify> classifyList = openApiClassifyService.getClassifyByIdList(classifyIdList);
        Map<Long, String> classifyMap = classifyList.stream().collect(Collectors.toMap(OpenApiClassify::getId, OpenApiClassify::getName));
        modelList.forEach(it -> it.setClassifyName(classifyMap.get(it.getClassifyId())));
        results.setList(modelList);
        return results;
    }

    @Override
    public List<IndexApiList> indexList() {
        // 1. 查询分类
        List<IndexApiClassifyResult> openApiClassifyListResults = openApiClassifyService.normalList();
        // 2. 查询API
        return openApiClassifyListResults.stream().map(it -> {
            IndexApiList indexApiList = new IndexApiList();
            indexApiList.setId(it.getId());
            indexApiList.setCode(it.getCode());
            indexApiList.setName(it.getName());
            indexApiList.setDescription(it.getDescription());
            indexApiList.setShowIndex(it.getShowIndex());
            // 查询数据库
            List<OpenApi> openApis = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApi>()
                            .ne(OpenApi::getStatus, ApiStatusEnum.DOWN.getCode())
                    .eq(OpenApi::getClassifyId, it.getId()).orderByAsc(OpenApi::getId).last(" limit 6"));
            indexApiList.setApiList(openApis.stream().map(it1 -> BeanUtil.sourceToTarget(it1, OpenApiResponseModel.class)).collect(Collectors.toList()));
            return indexApiList;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer normalCount() {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<OpenApi>()
                        .eq(OpenApi::getOpenApplyStatus, ApiOpenApplyStatusEnum.OPEN.getCode())
                        .ne(OpenApi::getStatus, ApiStatusEnum.DOWN.getCode()));
    }

    @Override
    public IndexApiList indexListByClassify(Long classifyId, Integer page) throws DataOpenPlatformException {
        // 1. 查询分类
        List<IndexApiClassifyResult> openApiClassifyListResults = null;
        if(classifyId == null || classifyId == -1){
            openApiClassifyListResults = openApiClassifyService.normalList();
        }else{
            openApiClassifyListResults = openApiClassifyService.normalListById(classifyId);
        }
        if(openApiClassifyListResults.isEmpty()){
            throw new DataOpenPlatformException("暂无数据");
        }
        IndexApiClassifyResult classifyResult = openApiClassifyListResults.get(0);
        // 2. 查询API
        IndexApiList indexApiList = new IndexApiList();
        indexApiList.setId(classifyResult.getId());
        indexApiList.setCode(classifyResult.getCode());
        indexApiList.setName(classifyResult.getName());
        indexApiList.setDescription(classifyResult.getDescription());
        indexApiList.setShowIndex(classifyResult.getShowIndex());
        // 查询数据库
        LambdaQueryWrapper<OpenApi> lambdaQueryWrapper = new LambdaQueryWrapper<OpenApi>()
                .eq(OpenApi::getClassifyId, classifyResult.getId())
                .ne(OpenApi::getStatus, ApiStatusEnum.DOWN.getCode());
        // 查询count
        Integer count = this.getBaseMapper().selectCount(lambdaQueryWrapper);
        indexApiList.setApiCount(count);
        // 查询数据
        List<OpenApi> openApis = this.getBaseMapper().selectList(
                lambdaQueryWrapper
                .orderByAsc(OpenApi::getId)
                .last(" limit " + (page-1)*12 + ",12" ));
        indexApiList.setApiList(openApis.stream().map(it1 -> BeanUtil.sourceToTarget(it1, OpenApiResponseModel.class)).collect(Collectors.toList()));
        return indexApiList;
    }

    @Override
    public List<OpenApiResponseModel> indexListBySearch(String searchWord) {
        // 查询数据库
        List<OpenApi> openApis = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApi>()
                .ne(OpenApi::getStatus, ApiStatusEnum.DOWN.getCode())
                        .and(it ->  it.like(OpenApi::getName,searchWord)
                                .or().like(OpenApi::getDescription, searchWord))
                .orderByAsc(OpenApi::getId));
        return openApis.stream().map(it1 -> BeanUtil.sourceToTarget(it1, OpenApiResponseModel.class)).collect(Collectors.toList());
    }

    @Override
    public OpenApiResponseModel get(Long id) throws DataOpenPlatformException {
        // 根据id查询
        OpenApi openApi = this.getBaseMapper().selectById(id);
        if(openApi == null){
            throw new DataOpenPlatformException("API不存在");
        }
        OpenApiResponseModel openApiResponseModel = BeanUtil.sourceToTarget(openApi, OpenApiResponseModel.class);
        // 查询详情
        OpenApiDetails openApiDetails = openApiDetailsService.getOpenApiDetailsByApiId(id);
        openApiResponseModel.setOpenApiDetailsRequestModel(BeanUtil.sourceToTarget(openApiDetails, OpenApiDetailsRequestModel.class));
        // 查询请求
        List<OpenApiRequestParams> apiRequestParams = openApiRequestParamService.getApiRequestParamsByApiId(id);
        openApiResponseModel.setOpenApiRequestParamsRequestModelList(BeanUtil.sourceToTargetList(apiRequestParams, OpenApiRequestParamsRequestModel.class));
        // 返回参数
        List<OpenApiResponse> apiResponse = openApiResponseService.getApiResponseByApiId(id);
        openApiResponseModel.setOpenApiResponseRequestModelList(BeanUtil.sourceToTargetList(apiResponse, OpenApiResponseRequestModel.class));
        return openApiResponseModel;
    }

    @Override
    public IndexOpenApiResponseModel indexDetail(Long id) throws DataOpenPlatformException {
        // 根据id查询
        OpenApi openApi = this.getBaseMapper().selectById(id);
        if(openApi == null){
            throw new DataOpenPlatformException("API不存在");
        }
        IndexOpenApiResponseModel openApiResponseModel = BeanUtil.sourceToTarget(openApi, IndexOpenApiResponseModel.class);
        // 设置一些配置信息
        openApiResponseModel.setApiAddressPrefix(openApiProperties.getServiceAddress() + openApiProperties.getBaseRegisterPath());
        // 查询详情
        OpenApiDetails openApiDetails = openApiDetailsService.getOpenApiDetailsByApiId(id);
        openApiResponseModel.setOpenApiDetailsRequestModel(BeanUtil.sourceToTarget(openApiDetails, OpenApiDetailsRequestModel.class));
        // 查询请求
        List<OpenApiRequestParams> apiRequestParams = openApiRequestParamService.getApiRequestParamsByApiId(id);
        openApiResponseModel.setOpenApiRequestParamsRequestModelList(BeanUtil.sourceToTargetList(apiRequestParams, OpenApiRequestParamsRequestModel.class));
        // 返回参数
        List<OpenApiResponse> apiResponse = openApiResponseService.getApiResponseByApiId(id);
        openApiResponseModel.setOpenApiResponseRequestModelList(BeanUtil.sourceToTargetList(apiResponse, OpenApiResponseRequestModel.class));
        List<OpenApiResponseRequestModel> openApiResponseRequestModels = publicResponseModel(id);
        openApiResponseModel.setPublicResponseModel(openApiResponseRequestModels);
        return openApiResponseModel;
    }

    @Override
    public void updateStatus(Long id, Integer status) throws DataOpenPlatformException, NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        if(!ApiStatusEnum.isExist(status)){
            throw new DataOpenPlatformException("转态枚举不存在");
        }
        OpenApi openApi = this.getBaseMapper().selectById(id);
        if(openApi == null){
            throw new DataOpenPlatformException("API不存在");
        }
        // 上线
        OpenApiDetails openApiDetails = openApiDetailsService.getOpenApiDetailsByApiId(id);
        ApiInfo apiInfo = ApiInfo.Builder.builder().path(openApiDetails.getApiAddress())
                .method(openApiDetails.getRequestMode().toUpperCase(Locale.ROOT)).build();
        if(!ApiStatusEnum.DOWN.getCode().equals(status)){
            // 判断是否API已上线
            ApiInfo publishedApiInfo = apiInfoCache.get(apiInfo);
            if(publishedApiInfo == null){
                throw new DataOpenPlatformException("API没有发布，不能上线");
            }
        }else {
            openApiScriptsService.offline(openApiDetails.getApiId());
            apiInfoCache.remove(apiInfo);
        }
        openApi.setStatus(status);
        openApi.setUpdateTime(new Date());
        this.updateById(openApi);
    }

    @Override
    public void updateOpenApplyStatus(Long id, Integer openApplyStatus, String notOpenApplyReason) throws DataOpenPlatformException, NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        if(!ApiOpenApplyStatusEnum.isExist(openApplyStatus)){
            throw new DataOpenPlatformException(ErrorCodeEnum.VALID_STATUS.getErrorMessage(), ErrorCodeEnum.VALID_STATUS.getHttpCode());
        }
        OpenApi openApi = this.getBaseMapper().selectById(id);
        if(openApi == null){
            throw new DataOpenPlatformException(ErrorCodeEnum.RESOURCE_NOT_EXISTS.getErrorMessage(), ErrorCodeEnum.RESOURCE_NOT_EXISTS.getHttpCode());
        }
        // 更新状态
        openApi.setOpenApplyStatus(openApplyStatus);
        if(ApiOpenApplyStatusEnum.NOT_OPEN.getCode().equals(openApplyStatus)){
            // 判断是否API已上线
            openApi.setNotOpenApplyReason(StringUtils.isNotBlank(notOpenApplyReason) ? notOpenApplyReason:
                    Constants.DEFAULT_NOT_OPEN_APPLY_REASON);
        }
        openApi.setUpdateTime(new Date());
        this.updateById(openApi);
    }


    private List<OpenApiResponseRequestModel> publicResponseModel(Long apiId) throws DataOpenPlatformException {
        // 查询脚本
        List<OpenApiResponseRequestModel> publicResponseModel = new ArrayList<>();
        OpenApiScriptsRequest apiScriptsRequest = openApiScriptsService.getByApiId(apiId);
        if(apiScriptsRequest != null && ApiCustomResponseStructureEnum.OPEN.getCode().equals(apiScriptsRequest.getCustomResultStructure())){
            // 自定义了
            List<JSONObject> jsonObjects = ReturnValueTransform.transformPublicResult(apiScriptsRequest.getApiResponseFormat());
            publicResponseModel = JSONArray.parseArray(JSONArray.toJSONString(jsonObjects),OpenApiResponseRequestModel.class);
        }else{
            // 没有自定义
            publicResponseModel.add(new OpenApiResponseRequestModel("returnCode","String", "E00000", "结果码"));
            publicResponseModel.add(new OpenApiResponseRequestModel("message","String", "success", "成功或者失败的描述"));
            publicResponseModel.add(new OpenApiResponseRequestModel("results","Object", "{}", "返回的业务数据"));
            publicResponseModel.add(new OpenApiResponseRequestModel("dataSize","Integer", "0", "返回的业务数据数据量"));
        }
        return publicResponseModel;
    }
}
