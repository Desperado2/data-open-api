package com.github.desperado2.data.open.api.engine.manage.scripts;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.ExternalException;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceManager;
import com.github.desperado2.data.open.api.engine.manage.enums.ExecuteType;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.script.Bindings;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脚本执行器接口，实现此接口可自定义脚本执行引擎
 */
public interface IScriptParse {

    Object runScript(ExecuteType executeType, ApiExecuteEnvironmentEnum environmentEnum, String script, ApiInfo apiInfo, ApiParams apiParams) throws Throwable;

    Object engineEval(String script, Bindings bindings) throws Throwable;


    default Map<String, Object> buildScriptParams(ApiParams apiParams, Bindings bindings) {
        bindings.put("pathVar",apiParams.getPathVar());
        bindings.put("param",apiParams.getParam());
        bindings.put("body",apiParams.getBody());
        bindings.put("request",apiParams.getRequest());
        Map<String, Object> paramMap = new LinkedHashMap<>();

        if (!CollectionUtils.isEmpty(apiParams.getBody())){
            apiParams.getBody().forEach((key,value)->{
                if (StringUtils.isEmpty(key))return;
                paramMap.put(key,typeTransform(value));
                bindings.put(key, typeTransform(value));
            });
        }

        if (!CollectionUtils.isEmpty(apiParams.getParam())){
            apiParams.getParam().forEach((key,value)->{
                if (StringUtils.isEmpty(key))return;
                paramMap.put(key,typeTransform(value));
                bindings.put(key, typeTransform(value));
            });
        }

        if (!CollectionUtils.isEmpty(apiParams.getPathVar())){
            apiParams.getPathVar().forEach((key,value)->{
                if (StringUtils.isEmpty(key))return;
                paramMap.put(key,typeTransform(value));
                bindings.put(key, typeTransform(value));
            });
        }
        return paramMap;
    }

    /**
     * 参数类型转换
     * @param value 参数
     * @return 新类型参数
     */
    default Object typeTransform(Object value){
        if(value instanceof Double){
            if(Math.abs(((Double) value) - Math.round(((Double) value))) < Double.MIN_VALUE){
                return ((Double) value).intValue();
            }
        }
        if(value instanceof String && !((String) value).startsWith("0")) {
            value = ((String) value).trim();
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher isNum = pattern.matcher((CharSequence) value);
            if(isNum.matches()){
                try {
                    return Short.parseShort(value.toString());
                }catch (Exception ignored){}
                try {
                    return Integer.parseInt(value.toString());
                }catch (Exception ignored){}

                try {
                    return Long.parseLong(value.toString());
                }catch (Exception ignored){}
            }
            pattern = Pattern.compile("^([1-9][0-9]*|0)(\\.[0-9]*)?$");
            Matcher isDecimal = pattern.matcher((CharSequence) value);
            if(isDecimal.matches()){
                try {
                    return Float.parseFloat(value.toString());
                }catch (Exception ignored){}

                try {
                    return Double.parseDouble(value.toString());
                }catch (Exception ignored){}
            }
        }
        return value;
    }

    default DataSourceDialect getDataSource(ApiExecuteEnvironmentEnum environmentEnum, DataSourceManager dataSourceManager,ApiInfo apiInfo) throws ExternalException {
        if(ApiExecuteEnvironmentEnum.TEST == environmentEnum){
            return  dataSourceManager.getDialectMap().get(apiInfo.getTestDatasource());
        }
        if(ApiExecuteEnvironmentEnum.PRE == environmentEnum){
            return  dataSourceManager.getDialectMap().get(apiInfo.getPreDatasource());
        }
        if(ApiExecuteEnvironmentEnum.PROD == environmentEnum){
            return  dataSourceManager.getDialectMap().get(apiInfo.getProdDatasource());
        }
        throw new ExternalException(ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getCode(),
                ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getName(), apiInfo.getPath(),
                apiInfo.getMethod());
    }

    default String getDataSourceCode(ApiExecuteEnvironmentEnum environmentEnum, DataSourceManager dataSourceManager,ApiInfo apiInfo) throws ExternalException {
        if(ApiExecuteEnvironmentEnum.TEST == environmentEnum){
            return  apiInfo.getTestDatasource();
        }
        if(ApiExecuteEnvironmentEnum.PRE == environmentEnum){
            return  apiInfo.getPreDatasource();
        }
        if(ApiExecuteEnvironmentEnum.PROD == environmentEnum){
            return apiInfo.getProdDatasource();
        }
        throw new ExternalException(ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getCode(),
                ExternalResultCodeEnum.UNKNOWN_DATASOURCE.getName(), apiInfo.getPath(),
                apiInfo.getMethod());
    }
}
