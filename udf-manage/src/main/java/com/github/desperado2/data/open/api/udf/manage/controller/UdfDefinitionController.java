package com.github.desperado2.data.open.api.udf.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.udf.manage.model.UdfDefinitionRequestModel;
import com.github.desperado2.data.open.api.udf.manage.model.UdfDefinitionResponseModel;
import com.github.desperado2.data.open.api.udf.manage.model.UdfTypeResponseModel;
import com.github.desperado2.data.open.api.udf.manage.service.IUdfDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * udf定义接口
 * @author tu nan
 * @date 2023/5/22
 **/
@RestController
@RequestMapping((Constants.BASE_PATH +  "/udf-definition"))
@Api(tags = "API管理")
public class UdfDefinitionController {

    @Autowired
    private IUdfDefinitionService udfDefinitionService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增UDF")
    public DataResult<Void> addUdf(@RequestBody UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        udfDefinitionService.addUdf(udfDefinitionRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("更新UDF")
    public DataResult<Void> updateUdf(@RequestBody UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        udfDefinitionService.updateUdf(udfDefinitionRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ApiOperation("分页查询列表")
    public DataResult<List<UdfDefinitionResponseModel>> pageList(@RequestBody UdfDefinitionRequestModel udfDefinitionRequestModel) throws DataOpenPlatformException {
        return new DataResult<>(udfDefinitionService.udfList(udfDefinitionRequestModel));
    }


    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET)
    @ApiOperation("搜索UDF")
    public DataResult<List<UdfDefinitionResponseModel>> indexListBySearch(@PathVariable("searchWord") String searchWord) throws DataOpenPlatformException {
        return new DataResult<>(udfDefinitionService.udfListBySearch(searchWord));
    }


    @RequestMapping(value = "/{id}/id", method = RequestMethod.GET)
    @ApiOperation("搜索UDF")
    public DataResult<UdfDefinitionResponseModel> udfById(@PathVariable("id") Long id) throws DataOpenPlatformException {
        return new DataResult<>(udfDefinitionService.udfById(id));
    }


    @RequestMapping(value = "/type-list", method = RequestMethod.GET)
    @ApiOperation("UDF分类")
    public DataResult<List<UdfTypeResponseModel>> udfType() throws DataOpenPlatformException {
        return new DataResult<>(udfDefinitionService.udfType());
    }
}
