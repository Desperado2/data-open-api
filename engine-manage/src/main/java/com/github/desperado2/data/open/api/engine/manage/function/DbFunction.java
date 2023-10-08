package com.github.desperado2.data.open.api.engine.manage.function;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.cache.manage.chche.IDBCache;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.ExternalException;
import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.datasource.manage.utils.MyBatisUtils;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.datamasking.DataMaskingEnum;
import com.github.desperado2.data.open.api.engine.manage.datamasking.DataMaskingExecutor;
import com.github.desperado2.data.open.api.engine.manage.datamasking.IDataMaskingService;
import com.github.desperado2.data.open.api.engine.manage.function.IFunction;
import com.github.desperado2.data.open.api.engine.manage.function.LockFunction;
import com.github.desperado2.data.open.api.engine.manage.interceptor.ISQLInterceptor;
import com.github.desperado2.data.open.api.engine.manage.model.CacheParams;
import com.github.desperado2.data.open.api.engine.manage.model.MaskParams;
import com.github.desperado2.data.open.api.engine.manage.model.PageList;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptParseService;
import com.github.desperado2.data.open.api.engine.manage.utils.LogFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库操作函数
 */
@SuppressWarnings("DuplicatedCode")
public class DbFunction implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(DbFunction.class);


    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private ScriptParseService parseService;

    @Autowired
    private ISQLInterceptor sqlInterceptor;

    @Autowired
    private IDBCache dbCache;

    @Autowired
    private DataMaskingExecutor dataMaskingExecutor;

    @Autowired
    private LockFunction lockFunction;

    @Override
    public String getVarName() {
        return "db";
    }


    /**
     * 查询数量
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public Long count(String script) throws Exception {
        String datasourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return count(datasourceCode, script);
    }

    /**
     * 查询数量
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public Long pageCount(String script) throws Exception {
        String countSQL = MyBatisUtils.getCountSQL(script);
        String datasourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return count(datasourceCode, countSQL);
    }

    /**
     * 查询数量
     * @param dataSourceCode 数据源编码
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public Long count(String dataSourceCode, String script) throws Exception {
        List<Map> list = selectList(dataSourceCode, script);
        if (CollectionUtils.isEmpty(list))return 0L;
        if (list.size()>1){
            return (long) list.size();
        }
        Object[] fieldValues = list.get(0).values().toArray();
        if (fieldValues.length>1 || !(fieldValues[0] instanceof Number)){
            return 1L;
        }
        return Long.valueOf(fieldValues[0].toString());
    }


    /**
     * 查询一条记录
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public Map<String,Object> selectOne(String script) throws Exception {
        String datasourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return selectOne(datasourceCode, script);
    }

    /**
     * 查询一条记录
     * @param dataSourceCode 数据编码
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public Map<String,Object> selectOne(String dataSourceCode, String script) throws Exception {
        List<Map> list = selectList(dataSourceCode, script);
        if (list.size() == 0)return null;
        return list.get(0);
    }


    /**
     * SQL查询日志
     * @param startTime 开始时间
     * @param sbScript 脚本
     * @param scriptContext 上下文
     */
    private void dbFinal(long startTime, StringBuilder sbScript, ScriptContext scriptContext){
        if (scriptContext == null){
            return;
        }
        long diff = System.currentTimeMillis() - startTime;
        String logScript = LogFormatUtils.sqlParam(sbScript, parseService,scriptContext);
        if (apiInfoContent.getIsDebug()){
            apiInfoContent.putLog(String.format("Elapsed Time:%sms , execute script: %s",diff,logScript));
        }
        log.debug("Elapsed Time:{}ms , execute script: {}",diff,logScript);
        sqlInterceptor.after(sbScript.toString());
    }

    /**
     * 查询数据库列表
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public List<Map> selectList(String script) throws Exception {
        String datasourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return selectList(datasourceCode, script);
    }

    /**
     * 查询数据库列表
     * @param dataSourceCode 数据源编码
     * @param script SQL脚本
     * @return 数据
     * @throws Exception SQL执行异常
     */
    public List<Map> selectList(String dataSourceCode, String script) throws Exception {
        LinkedHashMap<String, Object> sqlParam = getSqlParam(script);
        String cacheKey = null;
        List<Map> result = null;
        CacheParams cacheParams = parseCacheParams();
        Boolean isLocalTest = apiInfoContent.getIsLocalTest();
        if(!isLocalTest && cacheParams.getEnableCache() && cacheParams.getCacheValidity() > 0){
            // 走缓存
            cacheKey = getCacheKey(dataSourceCode, sqlParam, script,null, null);
            Object cacheData = queryCache(cacheKey);
            if(cacheData != null){
                return JSONArray.parseArray(cacheData.toString(), Map.class);
            }
        }
        try {
            // 走数据库
            result = realQueryFromDatabase(dataSourceCode,script, sqlParam);
        }finally {
            if(!isLocalTest && cacheParams.getEnableCache() && cacheParams.getCacheValidity() > 0){
                if(result != null){
                    dbCache.set(cacheKey, result, cacheParams.getCacheValidity());
                }
                lockFunction.peek(cacheKey);
            }
        }
        return result;
    }

    /**
     * 真正查询数据库
     * @param dataSourceCode 数据源编码
     * @param script 数据库SQL
     * @param sqlParam SQL绑定参数
     * @return 数据结果
     */
    private List<Map> realQueryFromDatabase(String dataSourceCode, String script, LinkedHashMap<String, Object> sqlParam) throws Exception {
        DataSourceDialect dataSourceDialect = dataSourceManager.getDialectMap().get(dataSourceCode);
        long startTime = System.currentTimeMillis();
        StringBuilder sbScript = new StringBuilder(sqlInterceptor.before(script));
        List<Map> result = null;
        ScriptContext scriptContext = null;
        try {
            scriptContext = new ScriptContext(new StringBuilder().append(script),
                    sqlParam,dataSourceDialect);
            result = dataSourceDialect.find(scriptContext);
            // 进行字符脱敏
            LinkedHashMap<String, MaskParams> dataMaskingBinds = apiInfoContent.getDataMaskingBinds();
            if(dataMaskingBinds != null && dataMaskingBinds.size() > 0){
                result = result.stream().map(it -> toMaskingData(it, dataMaskingBinds)).collect(Collectors.toList());
            }
        }finally {
            dbFinal(startTime,sbScript,scriptContext);
        }
        return result;
    }


    /**
     * 获取数据库缓存
     * @param cacheKey 缓存KEY
     * @return 缓存值
     * @throws InterruptedException 线程中断异常
     */
    private Object queryCache(String cacheKey) throws InterruptedException {
        Object list = dbCache.get(cacheKey);
        if(list != null){
            return list;
        }
        synchronized (DbFunction.class){
            lockFunction.put(cacheKey, 1L);
            list = dbCache.getList(cacheKey);
        }
        if(list != null){
            lockFunction.peek(cacheKey);
            return list;
        }
        return null;
    }


    /**
     * 分页查询
     * @param script SQL脚本
     * @param page 分页 当前页
     * @param pageSize 页大小
     * @return 分页数据
     * @throws Exception 数据库查询异常
     */
    public PageList selectPage(String script, Integer page, Integer pageSize) throws Exception {
        String  dataSourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return selectPage(dataSourceCode, script, page, pageSize);
    }


    /**
     * 分页查询
     * @param dataSourceCode 数据源编码
     * @param script SQL脚本
     * @param page 分页 当前页
     * @param pageSize 页大小
     * @return 分页数据
     * @throws Exception 数据库查询异常
     */
    public PageList selectPage(String dataSourceCode, String script, Integer page, Integer pageSize) throws Exception {
        //获取缓存对象
        LinkedHashMap<String, Object> sqlParam = getSqlParam(script);
        String cacheKey = null;
        List<Map> result = null;
        Boolean isLocalTest = apiInfoContent.getIsLocalTest();
        CacheParams cacheParams = parseCacheParams();
        if(!isLocalTest && cacheParams.getEnableCache() && cacheParams.getCacheValidity() > 0){
            // 走缓存
            cacheKey = getCacheKey(dataSourceCode,sqlParam, script, page, pageSize);
            Object cacheData = queryCache(cacheKey);
            if(cacheData != null){
                return JSONObject.parseObject(cacheData.toString(),PageList.class);
            }
        }
        PageList pageList = new PageList(page, pageSize);
        try {
            DataSourceDialect dataSourceDialect = dataSourceManager.getDialectMap().get(dataSourceCode);
            // 查询count
            Long count = pageCount(script);
            pageList.setTotalSize(count.intValue());
            // 判断是否还有值
            if(count > pageList.getOffset()){
                script = script + dataSourceDialect.getLimitSql(page, pageSize);
                result = realQueryFromDatabase(dataSourceCode,script, sqlParam);
                pageList.setDataList(result);
            }
        }finally {
            if(!isLocalTest && cacheParams.getEnableCache() && cacheParams.getCacheValidity() > 0){
                if(result != null){
                    dbCache.set(cacheKey, pageList, cacheParams.getCacheValidity());
                }
                lockFunction.peek(cacheKey);
            }
        }
        return pageList;
    }


    /**
     * 根据环境获取数据源
     * @param environmentEnum 环境枚举
     * @return 数据源
     * @throws ExternalException 不存在异常
     */
    private String getDatasourceCode(ApiExecuteEnvironmentEnum environmentEnum) throws ExternalException {
        switch (environmentEnum){
            case TEST:
                return apiInfoContent.getApiInfo().getTestDatasource();
            case PRE:
                return apiInfoContent.getApiInfo().getPreDatasource();
            case PROD:
                return  apiInfoContent.getApiInfo().getProdDatasource();
        }
        throw new ExternalException(ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getCode(),
                ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getName(), apiInfoContent.getApiInfo().getPath(),
                apiInfoContent.getApiInfo().getMethod());
    }

    /**
     * 数据脱敏
     * @param map 数据
     * @param dataMaskingBinds 脱敏配置
     * @return 脱敏之后的数据
     */
    protected Map<String,Object> toMaskingData(Map map, LinkedHashMap<String, MaskParams> dataMaskingBinds){
        if(map == null){
            return null;
        }
        Map<String,Object> result = new HashMap<>(map.size());
        for(Object key : map.keySet()){
            if(!dataMaskingBinds.containsKey(key.toString())){
                result.put(key.toString(), map.get(key));
                continue;
            }
            MaskParams maskParams = dataMaskingBinds.get(key.toString());
            DataMaskingEnum executorBeanNameByCode = DataMaskingEnum.getExecutorBeanNameByCode(maskParams.getName());
            if(executorBeanNameByCode == null){
                result.put(key.toString(), map.get(key));
                continue;
            }
            IDataMaskingService scriptExecutor = dataMaskingExecutor.getScriptExecutor(executorBeanNameByCode);
            if(scriptExecutor == null){
                result.put(key.toString(), map.get(key));
                continue;
            }
            Object maskValue = scriptExecutor.mask(map.get(key), maskParams.getParam());
            result.put(key.toString(), maskValue);
        }
        return result;
    }

    /**
     * 解析SQL脚本 获取参数
     * @param script SQL脚本
     * @return 参数与值
     */
    private LinkedHashMap<String, Object> getSqlParam(String script){
        List<String> parameterMappingList = MyBatisUtils.getParameterMappingList(script);
        LinkedHashMap<String, Object> sqlParma = new LinkedHashMap<>();
        // 请求参数
        if(apiInfoContent.getRequestParams() != null){
            for (Object key : apiInfoContent.getRequestParams().keySet()) {
                if(parameterMappingList.contains(key.toString())){
                    sqlParma.put(key.toString(), apiInfoContent.getRequestParams().get(key));
                }else if(script.contains(key.toString())){
                    sqlParma.put(key.toString(), apiInfoContent.getRequestParams().get(key));
                }
            }
        }


        // SQL 绑定参数
        if(apiInfoContent.getDbParamBinds() != null){
            for (Map.Entry<String, Object> entry : apiInfoContent.getDbParamBinds().entrySet()) {
                if(parameterMappingList.contains(entry.getKey())){
                    sqlParma.put(entry.getKey(), entry.getValue());
                }else if(script.contains(entry.getKey())){
                    sqlParma.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return sqlParma;
    }


    /**
     * 构建缓存的KEY
     * @param dataSourceCode 数据源编码
     * @param parameterMappingList 参数映射列表
     * @param script SQL脚本
     * @param page 分页
     * @param pageSize 分页大小
     * @return KEY
     */
    private String getCacheKey(String dataSourceCode, LinkedHashMap<String, Object> parameterMappingList, String script, Integer page, Integer pageSize){
        Map<String, String> keyValues = new LinkedHashMap<>();
        if(dataSourceCode != null){
            keyValues.put("dataSourceCode", dataSourceCode);
        }
        if(page != null){
            keyValues.put("page", page.toString());
        }
        if(pageSize != null){
            keyValues.put("pageSize", pageSize.toString());
        }
        keyValues.put("sqlScript", script);
        if(parameterMappingList != null){
            for (Map.Entry<String, Object> entry : parameterMappingList.entrySet()) {
                keyValues.put("db_" + entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        if(apiInfoContent.getDataMaskingBinds() != null){
            for (Object key : apiInfoContent.getRequestParams().keySet()) {
                keyValues.put("dm_" + key.toString(), String.valueOf(apiInfoContent.getRequestParams().get(key)));
            }
        }
        // 排序
        Map<String, Object> result = new LinkedHashMap<>();
        // 按照key的字典序升序排列
        keyValues.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        String key = StringUtils.join(Arrays.asList(result.entrySet().stream().map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
        return Md5Encrypt.md5Hexdigest(key, "utf-8");
    }

    /**
     * 解析缓存参数
     * @return 缓存参数
     */
    private CacheParams parseCacheParams(){
        if(apiInfoContent.getApiInfo() == null){
            return new CacheParams(false, 0L);
        }
        JSONObject optionData = apiInfoContent.getApiInfo().getOptionData();
        if(optionData == null){
            return new CacheParams(false, 0L);
        }
        CacheParams cacheParams = new CacheParams(false, 120L);
        if(optionData.containsKey("enableCache")){
            cacheParams.setEnableCache(optionData.getBoolean("enableCache"));
        }
        if(optionData.containsKey("cacheValidity")){
            cacheParams.setCacheValidity(optionData.getLong("cacheValidity"));
        }
        return cacheParams;
    }
}
