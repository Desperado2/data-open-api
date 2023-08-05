package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiResponse;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiResponseRequestModel;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiResponseService extends IService<OpenApiResponse> {

    /**
     * 新增API的响应参数
     * @param openApiResponseRequestModel 请求参数
     */
    void add(OpenApiResponseRequestModel openApiResponseRequestModel);


    /**
     * 批量新增API的响应参数
     * @param openApiResponseRequestModelList 请求参数
     */
    void batchAdd(List<OpenApiResponseRequestModel> openApiResponseRequestModelList);


    /**
     * 批量更新API的响应参数
     * @param apiId api id
     * @param openApiResponseRequestModelList 响应参数
     */
    void batchUpdate(Long apiId,List<OpenApiResponseRequestModel> openApiResponseRequestModelList);

    /**
     * 根据api id获取返回参数
     * @param apiId api id
     * @return 响应值列表
     */
    List<OpenApiResponse> getApiResponseByApiId(Long apiId);
}
