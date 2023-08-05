package com.github.desperado2.data.open.api.datasource.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.common.manage.utils.GenerateId;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceStatusEnum;
import com.github.desperado2.data.open.api.datasource.manage.enums.DataSourceTypeEnum;
import com.github.desperado2.data.open.api.datasource.manage.factory.IDataSourceDialectDriver;
import com.github.desperado2.data.open.api.datasource.manage.mapper.DatasourceConfigMapper;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceInfoModel;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceList;
import com.github.desperado2.data.open.api.datasource.manage.model.DataSourceListRequest;
import com.github.desperado2.data.open.api.datasource.manage.service.IDataSourceConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据源服务
 * @author tu nan
 * @date 2023/3/9
 **/
@Service
public class DataSourceConfigServiceImpl extends ServiceImpl<DatasourceConfigMapper, DataSourceConfig> implements IDataSourceConfigService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfigServiceImpl.class);

    private final DataSourceManager dataSourceManager;

    public DataSourceConfigServiceImpl(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    @Override
    public void addDatasource(DataSourceConfig dataSourceConfig) throws Exception {
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
        if(dataSourceConfig.getType() == null || dataSourceTypeEnum == null){
            throw new DataOpenPlatformException("不支持的数据源类型");
        }
        dataSourceConfig.setDriver(dataSourceTypeEnum.getDriver());
        dataSourceConfig.setCode(generateCode(dataSourceConfig));
        dataSourceConfig.setEnabled(DataSourceStatusEnum.ENABLE.getCode());
        dataSourceConfig.setCreateTime(new Date());
        dataSourceConfig.setUpdateTime(new Date());
        // 密码加密
        String password = dataSourceConfig.getPassword();
        String encrypt = AESUtils.encrypt(password, dataSourceTypeEnum.getName());
        dataSourceConfig.setPassword(encrypt);
        this.getBaseMapper().insert(dataSourceConfig);
        // 添加数据源
        dataSourceConfig.setPassword(password);
        loadDatasourceConfig(dataSourceConfig);
    }

    @Override
    public void testDataSourceConfig(DataSourceConfig dataSourceConfig) throws Exception {
        Integer type = dataSourceConfig.getType();
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(type);
        if(dataSourceTypeEnum == null){
            throw new DataOpenPlatformException("未知的数据源类型");
        }
        IDataSourceDialectDriver factory = (IDataSourceDialectDriver) (Class.forName(dataSourceTypeEnum.getDriver()).newInstance());
        DataSourceDialect dialect = factory.factory(dataSourceConfig);
        dialect.close();
    }

    @Override
    public void testDataSourceConfigById(Long id) throws Exception {
        DataSourceConfig dataSourceConfig = this.getBaseMapper().selectById(id);
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
        dataSourceConfig.setPassword(AESUtils.decrypt(dataSourceConfig.getPassword(), dataSourceTypeEnum.getName()));
        testDataSourceConfig(dataSourceConfig);
    }

    @Override
    public void changeStatusById(Long id, Integer status) throws Exception {
        DataSourceConfig dataSourceConfig = this.getBaseMapper().selectById(id);
        if(dataSourceConfig != null){
            dataSourceConfig.setEnabled(status);
            DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
            dataSourceConfig.setPassword(AESUtils.decrypt(dataSourceConfig.getPassword(), dataSourceTypeEnum.getName()));
            // 关闭
            if(DataSourceStatusEnum.ENABLE.getCode().equals(status)){
                loadDatasourceConfig(dataSourceConfig);
            }else if(DataSourceStatusEnum.DISABLE.getCode().equals(status)){
                closeDataSourceConfig(dataSourceConfig);
            }
            this.updateById(dataSourceConfig);
        }
    }

    @Override
    public void reloadAllDataSource() throws Exception {
        Set<String> oldDBList = dataSourceManager.getDialectMap().keySet();
        for (String oldDBCode : oldDBList) {
            if(!dataSourceManager.getDialectMap().get(oldDBCode).isDynamic()){
                continue;
            }
            closeDataSourceConfig(new DataSourceConfig(oldDBCode));
        }
        // 获取数据源列表
        List<DataSourceConfig> dataSourceConfigs = this.getBaseMapper().selectList(null);
        for (DataSourceConfig dataSourceConfig : dataSourceConfigs) {
            DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
            dataSourceConfig.setPassword(AESUtils.decrypt(dataSourceConfig.getPassword(), dataSourceTypeEnum.getName()));
            loadDatasourceConfig(dataSourceConfig);
        }
    }

    @Override
    public void updateDataSource(DataSourceConfig dataSourceConfig) throws Exception {
        Long id = dataSourceConfig.getId();
        if(id == null){
            throw new DataOpenPlatformException("数据源id不能为空");
        }
        DataSourceConfig oldDataSourceConfig = this.getBaseMapper().selectById(id);
        if(oldDataSourceConfig == null){
            throw new DataOpenPlatformException("数据源不存在");
        }
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
        if(dataSourceConfig.getType() == null || dataSourceTypeEnum == null){
            throw new DataOpenPlatformException("不支持的数据源类型");
        }
        String code = oldDataSourceConfig.getCode();
        closeDataSourceConfig(oldDataSourceConfig);
        dataSourceConfig.setUpdateTime(new Date());
        dataSourceConfig.setDriver(dataSourceTypeEnum.getDriver());
        // 更新数据库
        BeanUtils.copyProperties(dataSourceConfig, oldDataSourceConfig);

        String password = oldDataSourceConfig.getPassword();
        String encrypt = AESUtils.encrypt(password, dataSourceTypeEnum.getName());
        oldDataSourceConfig.setPassword(encrypt);
        this.getBaseMapper().updateById(oldDataSourceConfig);
        // 新增连接
        oldDataSourceConfig.setPassword(AESUtils.decrypt(oldDataSourceConfig.getPassword(), dataSourceTypeEnum.getName()));
        oldDataSourceConfig.setCode(code);
        loadDatasourceConfig(oldDataSourceConfig);
    }

    @Override
    public PageResults<List<DataSourceList>> getPageList(DataSourceListRequest dataSourceListRequest) {
        // 查询参数
        LambdaQueryWrapper<DataSourceConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dataSourceListRequest.getName())){
            lambdaQueryWrapper.like(DataSourceConfig::getName, dataSourceListRequest.getName());
        }
        if(dataSourceListRequest.getType() != null){
            lambdaQueryWrapper.eq(DataSourceConfig::getType, dataSourceListRequest.getType());
        }
        if(dataSourceListRequest.getEnabled() != null){
            lambdaQueryWrapper.eq(DataSourceConfig::getEnabled, dataSourceListRequest.getEnabled());
        }
        // 分页参数
        IPage<DataSourceConfig> pageInfo = new Page<>(dataSourceListRequest.getCurrent(), dataSourceListRequest.getPageSize());
        IPage<DataSourceConfig> dataSourceConfigIPage = this.getBaseMapper().selectPage(pageInfo, lambdaQueryWrapper);
        PageResults<List<DataSourceList>> dataSourceListPageResults = new PageResults<>();
        dataSourceListPageResults.setPagination(new PageInfo(dataSourceListRequest.getCurrent(), dataSourceListRequest.getPageSize(),dataSourceConfigIPage.getTotal()));
        dataSourceListPageResults.setList(dataSourceConfigIPage.getRecords().stream().map(it -> BeanUtil.sourceToTarget(it, DataSourceList.class)).collect(Collectors.toList()));
        return dataSourceListPageResults;
    }

    @Override
    public List<DataSourceList> getDataSourceList() {
        List<DataSourceConfig> dataSourceConfigs = this.getBaseMapper().selectList(new LambdaQueryWrapper<DataSourceConfig>()
                .eq(DataSourceConfig::getEnabled, DataSourceStatusEnum.ENABLE.getCode()));
        return BeanUtil.sourceToTargetList(dataSourceConfigs, DataSourceList.class);
    }

    @Override
    public DataSourceConfig getById(Long dataSourceId) throws DataOpenPlatformException {
        DataSourceConfig dataSourceConfig = this.getBaseMapper().selectById(dataSourceId);
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dataSourceConfig.getType());
        if(dataSourceConfig.getType() == null || dataSourceTypeEnum == null){
            throw new DataOpenPlatformException("不支持的数据源类型");
        }
        dataSourceConfig.setPassword(AESUtils.decrypt(dataSourceConfig.getPassword(), dataSourceTypeEnum.getName()));
        return dataSourceConfig;
    }

    @Override
    public void loadAllDataSource() throws Exception {
        Set<String> oldDBList = dataSourceManager.getDialectMap().keySet();
        for (String oldDBCode : oldDBList ){
            if (!dataSourceManager.getDialectMap().get(oldDBCode).isDynamic()){
                continue;
            }
            try {
                closeDataSourceConfig(new DataSourceConfig(oldDBCode));
            }catch (Exception e){
                logger.error(String.format("数据源%s关闭失败", oldDBCode), e);
            }
        }
        List<DataSourceConfig> newDBList = this.getBaseMapper().selectList(null);
        for (DataSourceConfig dbConfig : newDBList){
            try {
                // 解密密码
                DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(dbConfig.getType());
                dbConfig.setPassword(AESUtils.decrypt(dbConfig.getPassword(), dataSourceTypeEnum.getName()));
                logger.info(String.format("注册数据源%s", dbConfig.getName()));
                loadDatasourceConfig(dbConfig);
            }catch (Exception e){
                logger.error(String.format("数据源[%s]加载失败", dbConfig.getName()), e);
            }
        }
    }

    @Override
    public List<DataSourceInfoModel> getSupportDataSourceList() {
        return Arrays.stream(DataSourceTypeEnum.values()).map(it -> {
            DataSourceInfoModel dataSourceInfoModel = new DataSourceInfoModel();
            dataSourceInfoModel.setCode(it.getCode());
            dataSourceInfoModel.setName(it.getName());
            dataSourceInfoModel.setIcon(it.getIcon());
            dataSourceInfoModel.setFormat(it.getFormat());
            return dataSourceInfoModel;
        }).collect(Collectors.toList());
    }


    /**
     * 加载数据源
     * @param dataSourceConfig 数据源信息
     * @throws Exception 异常
     */
    private void loadDatasourceConfig(DataSourceConfig dataSourceConfig) throws Exception {
        if(DataSourceStatusEnum.DISABLE.getCode().equals(dataSourceConfig.getEnabled())){
            return;
        }
        IDataSourceDialectDriver factory = (IDataSourceDialectDriver) (Class.forName(dataSourceConfig.getDriver()).newInstance());
        DataSourceDialect dialect = factory.factory(dataSourceConfig);
        dialect.setDynamic(true);
        dataSourceManager.getDialectMap().put(dataSourceConfig.getCode(), dialect);
    }


    /**
     * 管理数据源连接
     * @param dataSourceConfig 数据源
     */
    private void closeDataSourceConfig(DataSourceConfig dataSourceConfig){
        DataSourceDialect dataSourceDialect = dataSourceManager.getDialectMap().remove(dataSourceConfig.getCode());
        if(dataSourceDialect == null){
            return;
        }
        dataSourceDialect.close();
    }

    /**
     * 生成数据源编号
     * @return 数据源编号
     */
    private String generateCode(DataSourceConfig dataSourceConfig){
        Integer type = dataSourceConfig.getType();
        DataSourceTypeEnum dataSourceTypeEnum = DataSourceTypeEnum.getByCode(type);
        assert dataSourceTypeEnum != null;
        return dataSourceTypeEnum.getName() + "_" + GenerateId.get().toHexString();
    }
}
