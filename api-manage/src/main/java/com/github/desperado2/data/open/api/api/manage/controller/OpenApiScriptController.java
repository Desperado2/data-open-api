package com.github.desperado2.data.open.api.api.manage.controller;


import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsReleaseList;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsRequest;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiScriptsReleaseService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiScriptsService;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API脚本控制器
 * @author tu nan
 * @date 2023/3/13
 **/
@RestController
@RequestMapping((Constants.BASE_PATH +  "/api-script"))
@Api(tags = "API脚本管理")
public class OpenApiScriptController {

    @Autowired
    private IOpenApiScriptsService openApiScriptsService;

    @Autowired
    private IOpenApiScriptsReleaseService openApiScriptsReleaseService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增API脚本")
    public DataResult<Long> addApiScript(@RequestBody OpenApiScriptsRequest openApiScripts) throws DataOpenPlatformException {
        return new DataResult<>(openApiScriptsService.add(openApiScripts));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("更新API脚本")
    public DataResult<Void> updateApiScript(@RequestBody OpenApiScriptsRequest openApiScripts) throws DataOpenPlatformException {
        openApiScriptsService.update(openApiScripts);
        return new DataResult<>();
    }


    @RequestMapping(value = "/get-by-apiId/{apiId}", method = RequestMethod.GET)
    @ApiOperation("根据API ID查询API脚本")
    public DataResult<OpenApiScriptsRequest> getById(@PathVariable("apiId") Long id) throws DataOpenPlatformException {
        return new DataResult<>(openApiScriptsService.getByApiId(id));
    }

    @RequestMapping(value = "/update-status/{id}/{status}", method = RequestMethod.PUT)
    @ApiOperation("更新状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "API的id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "path")
    })
    public DataResult<Void> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) throws DataOpenPlatformException {
        openApiScriptsService.updateStatus(id, status);
        return new DataResult<>();
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    @ApiOperation("执行API脚本")
    public ResponseEntity executeApiScript(@RequestBody OpenApiScriptsRequest openApiScripts) throws Exception {
        return openApiScriptsService.execute(openApiScripts);
    }

    @RequestMapping(value = "/smoke-test", method = RequestMethod.GET)
    @ApiOperation("冒烟测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "API的id", required = true, paramType = "param"),
    })
    public ResponseEntity smokeTest(@RequestParam("id") Long id) throws Exception {
        return openApiScriptsService.smokeTest(id);
    }

    @RequestMapping(value = "/publish/{id}", method = RequestMethod.PUT)
    @ApiOperation("发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "API的id", required = true, paramType = "path"),
    })
    public DataResult<Void> publish(@PathVariable("id") Long id) throws Exception {
        openApiScriptsService.publish(id);
        return new DataResult<>();
    }

    @RequestMapping(value = "/publish-history-list/{apiId}", method = RequestMethod.GET)
    @ApiOperation("获取发布历史列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", value = "API的id", required = true, paramType = "path"),
    })
    public DataResult<List<OpenApiScriptsReleaseList>> publishHistoryList(@PathVariable("apiId") Long apiId) throws Exception {
        return new DataResult<>(openApiScriptsReleaseService.apiHistory(apiId));
    }

    @RequestMapping(value = "/publish-history/{publishId}", method = RequestMethod.GET)
    @ApiOperation("获取某一条发布历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishId", value = "发布id", required = true, paramType = "path"),
    })
    public DataResult<OpenApiScriptsRequest> publishHistory(@PathVariable("publishId") Long publishId) throws Exception {
        return new DataResult<>(openApiScriptsReleaseService.apiHistoryInfo(publishId));
    }
}
