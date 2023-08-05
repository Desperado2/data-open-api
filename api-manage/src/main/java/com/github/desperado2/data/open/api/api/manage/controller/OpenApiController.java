package com.github.desperado2.data.open.api.api.manage.controller;


import com.github.desperado2.data.open.api.api.manage.model.*;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiService;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开发接口控制器
 * @author tu nan
 * @date 2023/2/9
 **/
@RestController
@RequestMapping((Constants.BASE_PATH +  "/api"))
@Api(tags = "API管理")
public class OpenApiController {

    @Autowired
    private IOpenApiService openApiService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增API")
    public DataResult<Void> addApi(@RequestBody OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException {
        openApiService.add(openApiRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("更新API")
    public DataResult<Void> updateApi(@RequestBody OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException {
        openApiService.update(openApiRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ApiOperation("分页查询列表")
    public DataResult<PageResults<List<OpenApiListResponseModel>>> pageList(@RequestBody OpenApiListRequestModel openApiListRequestModel) throws DataOpenPlatformException {
        return new DataResult<>(openApiService.pageList(openApiListRequestModel));
    }


    @RequestMapping(value = "/index-list", method = RequestMethod.GET)
    @ApiOperation("查询列表")
    public DataResult<List<IndexApiList>> indexList() throws DataOpenPlatformException {
        return new DataResult<>(openApiService.indexList());
    }

    @RequestMapping(value = "/index-list/{classifyId}/{page}", method = RequestMethod.GET)
    @ApiOperation("查询某个分类下面的API列表")
    public DataResult<IndexApiList> indexListByClassify(@PathVariable("classifyId") Long classifyId, @PathVariable("page") Integer page) throws DataOpenPlatformException {
        return new DataResult<>(openApiService.indexListByClassify(classifyId, page));
    }

    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET)
    @ApiOperation("搜索API")
    public DataResult<List<OpenApiResponseModel>> indexListBySearch(@PathVariable("searchWord") String searchWord) throws DataOpenPlatformException {
        return new DataResult<>(openApiService.indexListBySearch(searchWord));
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("根据ID查询API")
    public DataResult<OpenApiResponseModel> getById(@PathVariable("id") Long id) throws DataOpenPlatformException {
        return new DataResult<>(openApiService.get(id));
    }

    @RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
    @ApiOperation("根据ID查询API")
    public DataResult<IndexOpenApiResponseModel> indexDetailById(@PathVariable("id") Long id) throws DataOpenPlatformException {
        return new DataResult<>(openApiService.indexDetail(id));
    }

    @RequestMapping(value = "/update-status/{id}/{status}", method = RequestMethod.PUT)
    @ApiOperation("更新状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "API的id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "path")
    })
    public DataResult<Void> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) throws DataOpenPlatformException, NoSuchMethodException {
        openApiService.updateStatus(id, status);
        return new DataResult<>();
    }

    @RequestMapping(value = "/update-apply-status/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "API的id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "applyStatus", value = "开放申请状态", required = true, paramType = "param"),
            @ApiImplicitParam(name = "notOpenApplyReason", value = "不开放申请的原因", required = false, paramType = "param")
    })
    public DataResult<Void> updateApplyStatus(@PathVariable("id") Long id, @RequestParam("applyStatus") Integer applyStatus,
        @RequestParam("notOpenApplyReason") String notOpenApplyReason) throws DataOpenPlatformException, NoSuchMethodException {
        openApiService.updateOpenApplyStatus(id, applyStatus, notOpenApplyReason);
        return new DataResult<>();
    }
}
