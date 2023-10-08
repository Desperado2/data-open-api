package com.github.desperado2.data.open.api.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.model.*;
import com.github.desperado2.data.open.api.service.IApiSubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 接口申请控制器
 * @author tu nan
 * @date 2023/2/13
 **/
@RestController
@RequestMapping(value = Constants.BASE_PATH + "/api-subscribe/")
@Api(tags = "API操作")
@Validated
public class ApiSubscribeController {

    @Autowired
    private IApiSubscribeService apiSubscribeService;


    @PostMapping("subscribe")
    @ResponseBody
    @ApiOperation(value = "申请", notes = "[接口申请]")
    public DataResult<String> subscribe(@RequestBody @Valid ApiSubscribeRequestModel apiSubscribeRequestModel) throws DataOpenPlatformException {
        apiSubscribeService.add(apiSubscribeRequestModel);
        return new DataResult<>();
    }

    @PostMapping("cancel-subscribe/{id}")
    @ResponseBody
    @ApiOperation(value = "取消申请", notes = "[接口申请]")
    public DataResult<String> cancelSubscribe(@PathVariable("id") Long subscribeId) throws DataOpenPlatformException {
        apiSubscribeService.cancelSubscribe(subscribeId);
        return new DataResult<>();
    }

    @PostMapping("approval")
    @ResponseBody
    @ApiOperation(value = "审核", notes = "[接口申请]")
    public DataResult<String> approval(@RequestBody ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException {
        apiSubscribeService.approval(apiSubscribeApprovalRequestModel);
        return new DataResult<>();
    }

    @PostMapping("disable")
    @ResponseBody
    @ApiOperation(value = "禁用", notes = "[接口申请]")
    public DataResult<String> disable(@RequestBody ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException {
        apiSubscribeService.disable(apiSubscribeApprovalRequestModel);
        return new DataResult<>();
    }


    @PostMapping("list-by-user")
    @ResponseBody
    @ApiOperation(value = "查询订阅列表", notes = "[接口申请]")
    public DataResult<PageResults<List<ApiSubscribeSearchResponseModel>>> listByUser(@RequestBody ApiSubscribeSearchModel apiSubscribeSearchModel) throws DataOpenPlatformException {
        return new DataResult<>(apiSubscribeService.listByUser(apiSubscribeSearchModel));
    }

    @PostMapping("list-all")
    @ResponseBody
    @ApiOperation(value = "查询全部订阅", notes = "[接口申请]")
    public DataResult<PageResults<List<ApiSubscribeSearchResponseModel>>> list(@RequestBody ApiSubscribeSearchAllModel apiSubscribeSearchAllModel) throws DataOpenPlatformException {
        return new DataResult<>(apiSubscribeService.listAll(apiSubscribeSearchAllModel));
    }
}
