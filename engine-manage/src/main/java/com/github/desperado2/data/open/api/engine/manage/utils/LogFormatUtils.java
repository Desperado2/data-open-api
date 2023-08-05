package com.github.desperado2.data.open.api.engine.manage.utils;

import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * 日志格式化工具
 * @author tu nan
 * @date 2023/3/18
 **/
public class LogFormatUtils {
    private final static Logger log = LoggerFactory.getLogger(LogFormatUtils.class);

    public static String sqlParam(StringBuilder script, ScriptParseService parseService, ScriptContext scriptContext){
        try {
            StringBuilder res = new StringBuilder();
            for (Map.Entry<String, Object> entry : scriptContext.getBindParams().entrySet()) {
                String itemStr = script.toString();
                String replacement = java.util.regex.Matcher.quoteReplacement(parseService.buildFormatValue(entry.getValue(),scriptContext.getDataSourceDialect()));
                itemStr = itemStr.replaceAll(":"+entry.getKey(),replacement);
                res.append("\r\n").append(itemStr);
            }
            return res.toString();
        }catch (Exception e){
            return "sql param format error:" + script;
        }
    }

    private static String buildValue(Object value) {
        if (value == null){
            return "null";
        }
        if (value instanceof String){
            return "'"+value.toString()+"'";
        }
        if (value instanceof Collection){
            Collection list = (Collection)(value);
        }
        return value.toString();
    }
}
