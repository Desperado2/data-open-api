package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiDetails;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiDetailsMapper;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiDetailsRequestModel;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiDetailsService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiDetailsServiceImpl extends ServiceImpl<OpenApiDetailsMapper, OpenApiDetails> implements IOpenApiDetailsService {

    @Override
    public void add(OpenApiDetailsRequestModel openApiDetailsRequestModel) {
        OpenApiDetails openApiDetails = new OpenApiDetails();
        openApiDetails.setApiId(openApiDetailsRequestModel.getApiId());
        openApiDetails.setDetailDescription(openApiDetailsRequestModel.getDetailDescription());
        openApiDetails.setApiAddress(openApiDetailsRequestModel.getApiAddress());
        openApiDetails.setProtocol(openApiDetailsRequestModel.getProtocol());
        openApiDetails.setRequestMode(openApiDetailsRequestModel.getRequestMode());
        openApiDetails.setReturnExample(openApiDetailsRequestModel.getReturnExample());
        openApiDetails.setReturnFormat(openApiDetailsRequestModel.getReturnFormat());
        this.getBaseMapper().insert(openApiDetails);
    }

    @Override
    public void update(Long apiId, OpenApiDetailsRequestModel openApiDetailsRequestModel) throws DataOpenPlatformException {
        OpenApiDetails openApiDetails = getOpenApiDetailsByApiId(apiId);
        if(openApiDetails == null){
            throw new DataOpenPlatformException("详情不存在");
        }
        BeanUtils.copyProperties(openApiDetailsRequestModel, openApiDetails);
        this.getBaseMapper().updateById(openApiDetails);
    }

    @Override
    public OpenApiDetails getApiDetailsByAddress(String address) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiDetails>()
                .eq(OpenApiDetails::getApiAddress, address)
                .last(" limit 1"));
    }

    @Override
    public OpenApiDetails getOpenApiDetailsByApiId(Long apiId) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiDetails>()
                .eq(OpenApiDetails::getApiId, apiId)
                .last(" limit 1"));
    }
}
