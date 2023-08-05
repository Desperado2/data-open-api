package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiDetails;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiDetailsRequestModel;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiDetailsService extends IService<OpenApiDetails> {

    /**
     * 新增API详情
     * @param openApiDetailsRequestModel 请求参数
     */
    void add(OpenApiDetailsRequestModel openApiDetailsRequestModel);

    /**
     * 更新
     * @param apiId api id
     * @param openApiDetailsRequestModel 参数
     */
    void update(Long apiId, OpenApiDetailsRequestModel openApiDetailsRequestModel) throws DataOpenPlatformException;

    /**
     * 根据地址查询详情
     * @param address 地址
     * @return 详情
     */
    OpenApiDetails getApiDetailsByAddress(String address);

    /**
     * 根据api id查询
     * @param apiId api id
     * @return 详情
     */
    OpenApiDetails getOpenApiDetailsByApiId(Long apiId);
}
