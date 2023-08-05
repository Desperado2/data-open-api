package com.github.desperado2.data.open.api.api.manage.controller;

import com.github.desperado2.data.open.api.api.manage.model.IndexApiClassifyResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListRequest;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyRequestModel;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiClassifyService;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 开发接口控制器
 * @author tu nan
 * @date 2023/2/9
 **/
@RestController
@RequestMapping(Constants.BASE_PATH + "/classify")
@Api(tags = "API分类管理")
@Validated
public class OpenApiClassifyController {

    @Autowired
    private IOpenApiClassifyService openApiClassifyService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ApiOperation("新增分类")
    public DataResult<Void> addClassify(@RequestBody @Valid OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException {
        openApiClassifyService.add(openApiClassifyRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ApiOperation("编辑分类")
    public DataResult<Void> updateClassify(@RequestBody @Valid OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException {
        openApiClassifyService.update(openApiClassifyRequestModel);
        return new DataResult<>();
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE})
    @ApiOperation("删除分类")
    public DataResult<Void> deleteClassify(@PathVariable("id") Long id) throws DataOpenPlatformException {
        openApiClassifyService.delete(id);
        return new DataResult<>();
    }

    @RequestMapping(value = "/page-list", method = {RequestMethod.POST})
    @ApiOperation("分页列表")
    public DataResult<PageResults<List<OpenApiClassifyListResult>>> pageList(@RequestBody OpenApiClassifyListRequest openApiClassifyRequestModel) throws DataOpenPlatformException {
        return new DataResult<>(openApiClassifyService.pageList(openApiClassifyRequestModel));
    }

    @RequestMapping(value = "/index-list", method = {RequestMethod.GET})
    @ApiOperation("分页列表")
    public DataResult<List<IndexApiClassifyResult>> list() throws DataOpenPlatformException {
        return new DataResult<>(openApiClassifyService.normalList());
    }
}
