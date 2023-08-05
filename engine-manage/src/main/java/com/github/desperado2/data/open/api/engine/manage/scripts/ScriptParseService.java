package com.github.desperado2.data.open.api.engine.manage.scripts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.desperado2.data.open.api.datasource.manage.datasource.DataSourceDialect;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.enums.ParamScope;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.model.ArrVar;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author tu nan
 * @date 2023/3/13
 **/
@Service
public class ScriptParseService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private IScriptParse scriptParse;

    @Autowired
    private ApiInfoContent apiInfoContent;

    private Set<String> scopeSet = Stream.of(ParamScope.values()).map(ParamScope::name).collect(Collectors.toSet());


    public BoundSql getSql(String sqlScript, Map<String, Object> paramMap){
        // 替换SQL的参数
        Configuration configuration = sqlSessionFactory.getConfiguration();
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, "<script>" + sqlScript + "</script>", Map.class);
        return sqlSource.getBoundSql(paramMap);
    }

    /**
     * 构建获取请求域中的参数
     * @param apiParams
     * @param specifyParams
     * @param varName
     * @return
     */
    public Object buildRequestScopeParamItem(ApiParams apiParams, Map<String,Object> specifyParams, String varName){
        String[] paramArr = varName.split("\\.");

        if (specifyParams != null){
            return buildObjectValue(specifyParams,paramArr,0,varName);
        }

        Object value = null;
        if (scopeSet.contains(paramArr[0])){
            switch (ParamScope.valueOf(paramArr[0])){
                case pathVar:value = buildValueOfPathVar(apiParams.getPathVar(),paramArr[1]);break;
                case param:value = buildValueOfParameter(apiParams.getParam(),paramArr,1);break;
                case body:value = buildValueOfBody(apiParams.getBody(),paramArr,1);break;
                case cookie:value = buildValueOfCookie(apiParams.getCookie(),apiParams.getRequest(),paramArr,1);break;
                case header:value = buildValueOfHeader(apiParams.getHeader(),paramArr,1);break;
                case session:value = buildValueOfSession(apiParams.getSession(),paramArr,1);break;
            }
        }else {
            value = buildValueOfScriptContent(apiInfoContent.getEngineBindings() == null?null:apiInfoContent.getEngineBindings(),paramArr,0);
            if (value == null){
                value = buildValueOfPathVar(apiParams.getPathVar(),paramArr[0]);
            }
            if (value == null) {
                value = buildValueOfParameter(apiParams.getParam(), paramArr,0);
            }
            if(value == null){
                value = buildValueOfBody(apiParams.getBody(),paramArr, 0);
            }
            if(value == null){
                value = buildValueOfCookie(apiParams.getCookie(),apiParams.getRequest(), paramArr,0);
            }
            if(value == null){
                value = buildValueOfHeader(apiParams.getHeader(),paramArr,0);
            }
            if(value == null){
                value = buildValueOfSession(apiParams.getSession(),paramArr,0);
            }
        }
        return value;
    }

    /**
     * 构建上下文域中参数 (通过脚本引擎自动构建)
     * @param specifyParams
     * @param scriptLanguage
     * @return
     */
    public Object buildContentScopeParamItem(Map<String,Object> specifyParams, String scriptLanguage) {
        Bindings bindings = specifyParams!= null?new SimpleBindings(specifyParams):apiInfoContent.getEngineBindings();
        try{
            //变量与脚本提取区分
            if (Pattern.matches("^\\w+$", scriptLanguage)){
                return bindings.get(scriptLanguage);
            }else {
                return scriptParse.engineEval(scriptLanguage,bindings);
            }
        }catch (Throwable e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Object buildValueOfScriptContent(Bindings bindings, String[] paramArr, int index) {
        if (bindings == null)return null;
        return buildObjectValue(bindings,paramArr,index,paramArr[index]);
    }

    public Object buildValueOfSession(Map<String,Object> session,String[] paramArr,int index) {
        if (session == null){
            return null;
        }
        return buildObjectValue(session,paramArr,index,paramArr[index]);
    }

    private Object buildValueOfHeader(Map<String,String> header,String[] paramArr,int index) {
        String varName = paramArr[index].toLowerCase();
        if (header == null){
            return null;
        }
        return buildObjectValue(header,paramArr,index,varName);
    }

    private Object buildValueOfCookie(Map<String,Object> cookie, HttpServletRequest request, String[] paramArr, int index) {

        String varName = paramArr[index];
        Object value  = null;
        if (cookie != null){
            value = cookie.get(varName);
        }

        if (request != null && value == null){
            Cookie[] cookies = request.getCookies();
            if (cookies == null)return null;
            for(Cookie item : cookies){
                if(item.getName().equals(varName)){
                    value = item.getValue();
                    break;
                }
            }
        }

        if (paramArr.length-1 > index){
            return buildObjectValue(value,paramArr,index+1,paramArr[index + 1]);
        }
        return value;
    }

    private Object buildValueOfBody(Map<String,Object> body, String[] paramArr,int index) {
        if (body == null)return null;
        return buildObjectValue(body,paramArr,index,paramArr[index]);
    }

    private Object buildValueOfParameter(Map<String,Object> params, String[] paramArr,int index) {
        if (params == null)return null;
        return buildObjectValue(params,paramArr,index,paramArr[index]);
    }

    private Object buildObjectValue(Object inputParam,String[] paramArr,int index,String varName){

        if (inputParam == null){
            return null;
        }

        Map params = null;
        if (inputParam instanceof Map){
            params = (Map) inputParam;
        }else if(inputParam instanceof String){
            try {
                params = objectMapper.readValue(inputParam.toString(), Map.class);
            } catch (IOException e) {
                throw new IllegalArgumentException(inputParam+" Cannot be cast to Map.class");
            }
        }else{
            try {
                params = objectMapper.readValue(objectMapper.writeValueAsBytes(inputParam), Map.class);
            } catch (IOException e) {
                throw new IllegalArgumentException(inputParam+"Cannot be cast to Map.class");
            }
        }

        Object value = null;
        ArrVar arrVar = isArrVar(varName);
        if (arrVar != null){
            Object collection = params.get(arrVar.getVarName());
            if (collection == null){
                throw new IllegalArgumentException("The "+arrVar.getVarName()+" parameter is null");
            }
            if (!(collection instanceof Collection)){
                throw new IllegalArgumentException("The "+arrVar.getVarName()+" parameter is not an array");
            }

            Collection list = ((Collection)collection);
            if (arrVar.getIndex() >=list.size()){
                throw new IllegalArgumentException("The parameter "+arrVar.getVarName()+" exceeds the array length");
            }
            value = list.toArray()[arrVar.getIndex()];
        }else{
            value = params.get(varName);
        }

        if (paramArr.length-1 > index){
            return buildObjectValue(value,paramArr,index+1,paramArr[index+1]);
        }

        return value;
    }

    private ArrVar isArrVar(String varName){
        boolean isArrVar = varName.matches(".+\\[\\d+\\]$");
        if (!isArrVar)return null;
        String varNameFinal = varName.substring(0,varName.indexOf("["));
        Integer index = Integer.valueOf(varName.substring(varName.indexOf("[")+1,varName.length()-1));
        return new ArrVar(varNameFinal,index);
    }

    private Object buildValueOfPathVar(Map<String,String> pathVars, String varName) {
        if (pathVars == null)return null;
        return pathVars.get(varName);
    }

    private String buildSourceValue(Object val) {
        if (val == null)return null;
        return val.toString();
    }

    public String buildValue(Object val,DataSourceDialect sourceDialect) {
        if (val == null)return null;
        StringBuilder valStr = new StringBuilder();
        if (val instanceof Collection){
            valStr.append(((Collection)val).stream().map(item->buildStrValue(item,sourceDialect)).collect(Collectors.joining(",")));
        }else {
            valStr.append(buildStrValue(val,sourceDialect));
        }
        return valStr.toString();
    }

    public String buildFormatValue(Object val, DataSourceDialect sourceDialect) {
        if (val == null)return "null";
        StringBuilder valStr = new StringBuilder();
        if (val instanceof Collection){
            valStr.append(((Collection)val).stream().map(item->buildStrValue(item,sourceDialect)).collect(Collectors.joining(",")));
        }else {
            valStr.append(buildStrValue(val,sourceDialect));
        }
        return valStr.toString();
    }

    private String buildStrValue(Object val, DataSourceDialect sourceDialect){
        if (val == null)return null;
        if (val instanceof Number){
            return val.toString();
        }
        if (val instanceof Boolean){
            return val.toString();
        }
        return "'"+transcoding(val.toString(),sourceDialect)+"'";
    }

    public String transcoding(String input, DataSourceDialect sourceDialect){
        if (sourceDialect == null){
            return input;
        }
        return sourceDialect.transcoding(input);
    }
}
