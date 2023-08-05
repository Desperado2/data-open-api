package com.github.desperado2.data.open.api.udf.manage.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.annontation.manage.annontation.AdminPermissions;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.udf.manage.entity.UdfDefinition;
import com.github.desperado2.data.open.api.udf.manage.mapper.UdfDefinitionMapper;
import com.github.desperado2.data.open.api.udf.manage.model.*;
import com.github.desperado2.data.open.api.udf.manage.service.IUdfDefinitionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/5/22
 **/
@Service
public class UdfDefinitionServiceImpl extends ServiceImpl<UdfDefinitionMapper, UdfDefinition> implements IUdfDefinitionService {

    @AdminPermissions
    @Override
    public void addUdf(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        UdfDefinition udfDefinition = new UdfDefinition();
        udfDefinition.setDefinition(udfDefinitionRequestModel.getDefinition());
        udfDefinition.setName(udfDefinitionRequestModel.getName());
        udfDefinition.setType(udfDefinitionRequestModel.getType());
        udfDefinition.setParameter(udfDefinitionRequestModel.getParameter());
        udfDefinition.setReturnValue(udfDefinitionRequestModel.getReturnValue());
        udfDefinition.setDescription(udfDefinitionRequestModel.getDescription());
        udfDefinition.setExample(udfDefinitionRequestModel.getDefinition());
        this.getBaseMapper().insert(udfDefinition);
    }

    @AdminPermissions
    @Override
    public void updateUdf(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        Long id = udfDefinitionRequestModel.getId();
        if(id == null){
            throw new DataOpenPlatformException(ErrorCodeEnum.RESOURCE_NOT_EXISTS.getErrorMessage(), ErrorCodeEnum.RESOURCE_NOT_EXISTS.getHttpCode());
        }
        UdfDefinition udfDefinition = this.getBaseMapper().selectById(id);
        if(udfDefinition == null){
            throw new DataOpenPlatformException(ErrorCodeEnum.RESOURCE_NOT_EXISTS.getErrorMessage(), ErrorCodeEnum.RESOURCE_NOT_EXISTS.getHttpCode());
        }
        udfDefinition.setDefinition(udfDefinitionRequestModel.getDefinition());
        udfDefinition.setName(udfDefinitionRequestModel.getName());
        udfDefinition.setType(udfDefinitionRequestModel.getType());
        udfDefinition.setParameter(udfDefinitionRequestModel.getParameter());
        udfDefinition.setReturnValue(udfDefinitionRequestModel.getReturnValue());
        udfDefinition.setDescription(udfDefinitionRequestModel.getDescription());
        udfDefinition.setExample(udfDefinitionRequestModel.getDefinition());
        this.getBaseMapper().updateById(udfDefinition);
    }

    @AdminPermissions
    @Override
    public List<UdfDefinitionResponseModel> udfList(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        LambdaQueryWrapper<UdfDefinition> udfDefinitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(udfDefinitionRequestModel.getType() != null){
            udfDefinitionLambdaQueryWrapper.eq(UdfDefinition::getType, udfDefinitionRequestModel.getType());
        }
        List<UdfDefinition> udfDefinitions = this.getBaseMapper().selectList(udfDefinitionLambdaQueryWrapper);

        return BeanUtil.sourceToTargetList(udfDefinitions, UdfDefinitionResponseModel.class);
    }

    @AdminPermissions
    @Override
    public List<UdfDefinitionResponseModel> udfListBySearch(String searchWord) throws DataOpenPlatformException {
        LambdaQueryWrapper<UdfDefinition> udfDefinitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(searchWord)){
            udfDefinitionLambdaQueryWrapper.like(UdfDefinition::getType, searchWord)
                    .or()
                    .like(UdfDefinition::getDefinition, searchWord)
                    .or()
                    .like(UdfDefinition::getName, searchWord);
        }
        List<UdfDefinition> udfDefinitions = this.getBaseMapper().selectList(udfDefinitionLambdaQueryWrapper);
        return BeanUtil.sourceToTargetList(udfDefinitions, UdfDefinitionResponseModel.class);
    }

    @Override
    public UdfDefinitionResponseModel udfById(Long id) {
        UdfDefinition udfDefinition = this.getBaseMapper().selectById(id);
        UdfDefinitionResponseModel udfDefinitionResponseModel = BeanUtil.sourceToTarget(udfDefinition, UdfDefinitionResponseModel.class);


        if(udfDefinition.getParameter() != null && udfDefinition.getParameter().length() > 0){
            udfDefinitionResponseModel.setParameter(JSONArray.parseArray(udfDefinition.getParameter(), UdfParamModel.class));
        }

        if(udfDefinition.getReturnValue() != null && udfDefinition.getReturnValue().length() > 0){
            udfDefinitionResponseModel.setReturnValue(JSON.parseObject(udfDefinition.getReturnValue(), UdfReturnModel.class));
        }

        return udfDefinitionResponseModel;
    }

    @AdminPermissions
    @Override
    public List<UdfTypeResponseModel> udfType(){
        Map<String, List<UdfDefinition>> typeDataList = this.getBaseMapper().getTypeList()
                .stream().collect(Collectors.groupingBy(UdfDefinition::getType));
        return typeDataList.keySet().stream().map(it -> {
            UdfTypeResponseModel udfTypeResponseModel = new UdfTypeResponseModel();
            udfTypeResponseModel.setType(it);
            udfTypeResponseModel.setUdfList(BeanUtil.sourceToTargetList(typeDataList.get(it), UdfDefinitionResponseModel.class));
            return udfTypeResponseModel;
        }).collect(Collectors.toList());
    }
}
