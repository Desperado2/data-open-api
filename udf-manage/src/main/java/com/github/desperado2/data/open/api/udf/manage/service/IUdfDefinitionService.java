package com.github.desperado2.data.open.api.udf.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.udf.manage.entity.UdfDefinition;
import com.github.desperado2.data.open.api.udf.manage.model.UdfDefinitionRequestModel;
import com.github.desperado2.data.open.api.udf.manage.model.UdfDefinitionResponseModel;
import com.github.desperado2.data.open.api.udf.manage.model.UdfTypeResponseModel;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/5/22
 **/
public interface IUdfDefinitionService extends IService<UdfDefinition> {

    void addUdf(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException;


    void updateUdf(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException;

    List<UdfDefinitionResponseModel> udfList(UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException;

    List<UdfDefinitionResponseModel> udfListBySearch(String searchWord) throws DataOpenPlatformException;

    UdfDefinitionResponseModel udfById(Long id);

    List<UdfTypeResponseModel> udfType() throws DataOpenPlatformException;
}
