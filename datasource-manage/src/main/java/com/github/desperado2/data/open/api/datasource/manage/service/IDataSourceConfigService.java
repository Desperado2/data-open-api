package com.github.desperado2.data.open.api.datasource.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceInfoModel;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceList;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceListRequest;

import java.util.List;

/**
 * 数据源服务接口
 *
 * @author tu nan
 * @date 2023/3/9
 **/
public interface IDataSourceConfigService extends IService<DataSourceConfig> {

    /**
     * 新增数据源
     * @param dataSourceConfig 数据源配置
     */
    void addDatasource(DataSourceConfig dataSourceConfig) throws Exception;

    /**
     * 测试数据源
     * @param dataSourceConfig 数据源噢诶在
     * @throws Exception 异常
     */
    void testDataSourceConfig(DataSourceConfig dataSourceConfig) throws Exception;

    /**
     * 测试数据源
     * @param id 数据源id
     * @throws Exception 异常
     */
    void testDataSourceConfigById(Long id) throws Exception;

    /**
     * 更新状态
     * @param id id
     * @param status 状态
     * @throws Exception 异常
     */
    void changeStatusById(Long id, Integer status) throws Exception;

    /**
     * 加载所有数据源
     * @throws Exception 异常
     */
    void reloadAllDataSource() throws Exception;

    /**
     * 更新数据源
     * @param dataSourceConfig 数据源配置
     * @throws Exception 异常
     */
    void updateDataSource(DataSourceConfig dataSourceConfig) throws Exception;

    /**
     * 分页查询
     * @param dataSourceListRequest 参数
     * @return 分页结果
     */
    PageResults<List<DataSourceList>> getPageList(DataSourceListRequest dataSourceListRequest);

    /**
     * 获取不分页列表
     * @return 列表
     */
    List<DataSourceList> getDataSourceList();

    /**
     * 根据id查询
     * @param dataSourceId 数据源id
     * @return 数据源
     */
    DataSourceConfig getById(Long dataSourceId) throws DataOpenPlatformException;

    /**
     * 加载所有数据源
     */
    void  loadAllDataSource() throws Exception;

    /**
     * 获取支持的数据源类型列表
     * @return 数据源
     */
    List<DataSourceInfoModel> getSupportDataSourceList();
}
