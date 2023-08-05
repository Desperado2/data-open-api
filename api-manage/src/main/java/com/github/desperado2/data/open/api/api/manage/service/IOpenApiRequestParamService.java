package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiRequestParams;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiRequestParamsRequestModel;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiRequestParamService extends IService<OpenApiRequestParams> {

    /**
     * 新增API的请求参数
     * @param openApiRequestParamsRequestModel 请求参数
     */
    void add(OpenApiRequestParamsRequestModel openApiRequestParamsRequestModel);


    /**
     * 批量新增API的请求参数
     * @param list 请求参数
     */
    void batchAdd(List<OpenApiRequestParamsRequestModel> list);

    /**
     * 批次更新API的请求参数
     * @param id id
     * @param list 列表
     */
    void batchUpdate(Long id, List<OpenApiRequestParamsRequestModel> list);

    /**
     * 根据API id 查询请求参数列表
     * @param apiId api id
     * @return 参数列表
     */
    List<OpenApiRequestParams> getApiRequestParamsByApiId(Long apiId);
}
