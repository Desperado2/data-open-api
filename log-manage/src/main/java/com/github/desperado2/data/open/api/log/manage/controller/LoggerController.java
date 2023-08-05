package com.github.desperado2.data.open.api.log.manage.controller;

import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogSearchModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogsModel;
import com.github.desperado2.data.open.api.log.manage.service.IApiLogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证信息
 * @author tu nan
 * @date 2023/2/15
 **/
@RestController
@RequestMapping(Constants.BASE_PATH + "/logging")
@Api(tags = "日志信息管理")
public class LoggerController {

    private final IApiLogsService apiLogsService;

    public LoggerController(IApiLogsService apiLogsService) {
        this.apiLogsService = apiLogsService;
    }

    @PostMapping("list-by-user")
    @ResponseBody
    @ApiOperation(value = "查询日志列表", notes = "[日志信息管理]")
    public DataResult<PageResults<List<ApiLogsModel>>> listByUser(@RequestBody ApiLogSearchModel apiLogSearchModel){
        PageResults<List<ApiLogsModel>> listPageResults = apiLogsService.listByUser(apiLogSearchModel);
        return new DataResult<>(listPageResults);
    }


    @PostMapping("list-all")
    @ResponseBody
    @ApiOperation(value = "查询全部日志", notes = "[日志信息管理]")
    public DataResult<PageResults<List<ApiLogsModel>>> list(@RequestBody ApiLogSearchModel apiLogSearchModel) throws DataOpenPlatformException {
        PageResults<List<ApiLogsModel>> listPageResults = apiLogsService.listAll(apiLogSearchModel);
        return new DataResult<>(listPageResults);
    }
}
