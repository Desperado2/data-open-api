package com.github.desperado2.data.open.api.engine.manage.function;


import com.github.desperado2.data.open.api.cache.manage.chche.IDBCache;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.ExternalException;
import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.interceptor.ISQLInterceptor;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptParseService;
import com.github.desperado2.data.open.api.engine.manage.service.EsScriptParseService;
import com.github.desperado2.data.open.api.engine.manage.utils.LogFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据库操作函数
 */
@SuppressWarnings("DuplicatedCode")
public class EsFunction implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(EsFunction.class);

    @Resource
    private DataSourceManager dataSourceManager;

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private ScriptParseService parseService;

    @Autowired
    private ISQLInterceptor sqlInterceptor;

    @Resource
    private EsScriptParseService esScriptParseService;

    @Resource
    private IDBCache dbCache;

    @Resource
    private LockFunction lockFunction;

    @Value("${open.data.platform.base.db-cache-timeout:60}")
    private Long cacheTime;

    @Value("${open.data.platform.base.db-cache-open:false}")
    private Boolean openCache;


    @Override
    public String getVarName() {
        return "es";
    }


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

    public Object search(String index, String script) throws Exception {
        //获取缓存对象
        String datasourceCode = getDatasourceCode(apiInfoContent.getApiExecuteEnvironmentEnum());
        return search(datasourceCode, index, script);
    }


    public Object search(String dataSourceCode, String index, String script) throws Exception {
        //获取缓存对象
        String cacheKey = null;
        if(openCache){
            cacheKey = getCacheKey(dataSourceCode, index, script);
            Object cache = queryCache(cacheKey);
            if(cache != null){
                return cache;
            }
        }
        StringBuilder sbScript = new StringBuilder(sqlInterceptor.before(script));
        Object result = null;
        long startTime = System.currentTimeMillis();
        ScriptContext scriptContext = null;
        try {
            // 替换变量
            StringBuilder parse = esScriptParseService.parse(new StringBuilder(script));
            DataSourceDialect dataSourceDialect =  dataSourceManager.getDialectMap().get(dataSourceCode);
            scriptContext = new ScriptContext(parse,null, dataSourceDialect, index);
            result = dataSourceDialect.findEs(scriptContext);
        }finally {
            dbFinal(startTime,sbScript,scriptContext);
            if(openCache){
                if(result != null){
                    dbCache.set(cacheKey, result, cacheTime);
                }
                lockFunction.peek(cacheKey);
            }
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
     * 构建缓存的KEY
     * @param dataSourceCode 数据源编码
     * @param script SQL脚本
     * @return KEY
     */
    private String getCacheKey(String dataSourceCode, String index, String script){
        Map<String, String> keyValues = new LinkedHashMap<>();
        if(dataSourceCode != null){
            keyValues.put("dataSourceCode", dataSourceCode);
        }
        if(index != null){
            keyValues.put("index", index);
        }
        keyValues.put("sqlScript", script);
        // 排序
        Map<String, Object> result = new LinkedHashMap<>();
        // 按照key的字典序升序排列
        keyValues.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        String key = StringUtils.join(Arrays.asList(result.entrySet().stream().map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
        return Md5Encrypt.md5Hexdigest(key, "utf-8");
    }



}
