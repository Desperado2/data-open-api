package com.github.desperado2.data.open.api.datasource.manage.controller;

import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceInfoModel;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceList;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceListRequest;
import com.github.desperado2.data.open.api.datasource.manage.service.IDataSourceConfigService;
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
@RequestMapping(Constants.BASE_PATH + "/data-source")
@Api(tags = "数据源管理")
public class DataSourceController {

    private final IDataSourceConfigService dataSourceConfigService;

    public DataSourceController(IDataSourceConfigService dataSourceConfigService) {
        this.dataSourceConfigService = dataSourceConfigService;
    }

    @PostMapping("list-page")
    @ResponseBody
    @ApiOperation(value = "查询列表", notes = "[数据源管理]")
    public DataResult<PageResults<List<DataSourceList>>> listByUser(@RequestBody DataSourceListRequest dataSourceListRequest){
        PageResults<List<DataSourceList>> listPageResults = dataSourceConfigService.getPageList(dataSourceListRequest);
        return new DataResult<>(listPageResults);
    }


    @GetMapping("support-list")
    @ResponseBody
    @ApiOperation(value = "查询支持数据源类型列表", notes = "[数据源管理]")
    public DataResult<List<DataSourceInfoModel>> supportList(){
        return new DataResult<>( dataSourceConfigService.getSupportDataSourceList());
    }

    @GetMapping("list-all")
    @ResponseBody
    @ApiOperation(value = "查询全部数据源", notes = "[数据源管理]")
    public DataResult<List<DataSourceList>> list() throws DataOpenPlatformException {
        List<DataSourceList> listPageResults = dataSourceConfigService.getDataSourceList();
        return new DataResult<>(listPageResults);
    }

    @GetMapping("get/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id查询数据源", notes = "[数据源管理]")
    public DataResult<DataSourceConfig> getById(@PathVariable("id") Long id) throws DataOpenPlatformException {
        DataSourceConfig dataSourceConfig = dataSourceConfigService.getById(id);
        return new DataResult<>(dataSourceConfig);
    }

    @PostMapping("add")
    @ResponseBody
    @ApiOperation(value = "新增", notes = "[数据源管理]")
    public DataResult<Void> add(@RequestBody DataSourceConfig dataSourceConfig) throws Exception {
        dataSourceConfigService.addDatasource(dataSourceConfig);
        return new DataResult<>();
    }


    @PostMapping("update")
    @ResponseBody
    @ApiOperation(value = "更新", notes = "[数据源管理]")
    public DataResult<Void> update(@RequestBody DataSourceConfig dataSourceConfig) throws Exception {
        dataSourceConfigService.updateDataSource(dataSourceConfig);
        return new DataResult<>();
    }

    @PostMapping("test-connect")
    @ResponseBody
    @ApiOperation(value = "测试连接是否成功", notes = "[数据源管理]")
    public DataResult<Void> testConnect(@RequestBody DataSourceConfig dataSourceConfig) throws Exception {
        dataSourceConfigService.testDataSourceConfig(dataSourceConfig);
        return new DataResult<>();
    }

    @GetMapping("test-connect-by-id/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id测试连接是否成功", notes = "[数据源管理]")
    public DataResult<Void> testConnect(@PathVariable("id") Long id) throws Exception {
        dataSourceConfigService.testDataSourceConfigById(id);
        return new DataResult<>();
    }

    @PutMapping("change-status/{id}/{status}")
    @ResponseBody
    @ApiOperation(value = "根据id修改状态", notes = "[数据源管理]")
    public DataResult<Void> changeStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status) throws Exception {
        dataSourceConfigService.changeStatusById(id, status);
        return new DataResult<>();
    }
}
