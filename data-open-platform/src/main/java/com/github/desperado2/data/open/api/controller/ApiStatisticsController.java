package com.github.desperado2.data.open.api.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.model.statistic.BaseInfoStatisticsModel;
import com.github.desperado2.data.open.api.model.statistic.RequestInfoStatisticsModel;
import com.github.desperado2.data.open.api.service.IApiStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API统计控制器
 * @author tu nan
 * @date 2023/2/13
 **/
@RestController
@RequestMapping(value = Constants.BASE_PATH + "/api-statistic/")
@Api(tags = "API信息统计")
@Validated
public class ApiStatisticsController {

    @Autowired
    private IApiStatisticsService apiStatisticsService;

    @GetMapping("/base")
    @ResponseBody
    @ApiOperation(value = "基础统计信息", notes = "[API信息统计]")
    public DataResult<BaseInfoStatisticsModel> base() throws Exception {
        return new DataResult<>(apiStatisticsService.baseStatistics());
    }


    @GetMapping("/require")
    @ResponseBody
    @ApiOperation(value = "请求统计信息", notes = "[API信息统计]")
    public DataResult<List<RequestInfoStatisticsModel>> require() throws DataOpenPlatformException {
        return new DataResult<>(apiStatisticsService.requestStatistics());
    }
}
