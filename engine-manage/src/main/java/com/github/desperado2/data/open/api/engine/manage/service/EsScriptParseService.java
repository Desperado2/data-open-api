package com.github.desperado2.data.open.api.engine.manage.service;

import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.model.ConditionMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ElasticSearch脚本变量解析器，用于解析变量#{}等
 */

@SuppressWarnings("DuplicatedCode")
@Service
public class EsScriptParseService {


    @Autowired
    private ApiInfoContent apiInfoContent;


    public StringBuilder parse(StringBuilder script) {
        return buildParams(script);
    }

    /**
     * 构建参数 #{}
     * @param script
     */
    private StringBuilder buildParams(StringBuilder script){
        int start = 0;
        ConditionMatcher matcher = null;
        //匹配参数#{}
        while ((matcher = buildParamCondition(script,"#{",start)) != null){
            String condition = matcher.getCondition();
            Object replaceValue = buildValue(condition);
            script = script.replace(matcher.getStart(), matcher.getEnd()+1, replaceValue.toString());
            start = matcher.getStart() + replaceValue.toString().length();
        }
        return script;
    }



    private ConditionMatcher buildParamCondition(StringBuilder script, String flag, int start){

        int startIf = script.indexOf(flag,start);

        if (startIf == -1) {
            return null;
        }

        int ifCloseIndex = -1;
        int quotationMark = 0;
        int bigBracket = 1;

        for(int i=startIf+flag.length();i<script.length();i++){
            char c = script.charAt(i);

            if (quotationMark > 0){
                if (c == '\\') {
                    i++;
                    continue;
                }
                if (c == '"'){
                    quotationMark --;
                }
                continue;
            }

            if (c == '"'){
                quotationMark ++;
                continue;
            }


            if (c == '{'){
                bigBracket ++ ;
            }

            if (c == '}'){
                bigBracket -- ;
            }

            if (c == '}' && bigBracket == 0){
                ifCloseIndex = i;
                break;
            }
        }

        if (ifCloseIndex == -1){
            throw new IllegalArgumentException("missed if close '}'");
        }

        return ConditionMatcher.Builder.builder()
                .condition(script.substring(startIf+flag.length(),ifCloseIndex))
                .start(startIf)
                .end(ifCloseIndex)
                .build();
    }

    private Object buildValue(String val) {
        if (val == null){
            return null;
        }
        Object value  = apiInfoContent.getRequestParams().get(val);
        if(value == null){
            value = apiInfoContent.getDbParamBinds().get(val);
        }
        if(value == null){
            return null;
        }
        if (value instanceof String){
            return value.toString();
        } if (value instanceof Collection){
            List<String> sb = new ArrayList<>();
            for (Object o : ((Collection<?>) value)) {
                if(o instanceof String){
                   sb.add("\"" + o.toString() + "\"");
                }else{
                    sb.add(o.toString());
                }
            }
            return "[" + String.join(",", sb) + "]";
        }else {
            return value;
        }
    }
}
