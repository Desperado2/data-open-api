package com.github.desperado2.data.open.api.engine.manage.result;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回值常量
 * @author tu nan
 * @date 2023/3/15
 **/
public class ReturnValueTransform {

    /**
     * 错误消息
     */
    private static final String message = "@message";

    /**
     * 错误码
     */
    private static final String returnCode = "@returnCode";

    /**
     * 数据
     */
    private static final String results = "@results";

    /**
     * 数据量
     */
    private static final String dataSize = "@dataSize";

    public static JSONObject parseReturnValue(JSONObject apiResponseFormat, ResultWrapper resultWrapper){
        // 转换数据
        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.results))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> apiResponseFormat.put(dataKey, resultWrapper.getResults()));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.message))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> apiResponseFormat.put(dataKey, resultWrapper.getReturnCode()));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.returnCode))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> apiResponseFormat.put(dataKey, resultWrapper.getReturnCode()));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.dataSize))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> apiResponseFormat.put(dataKey, resultWrapper.getDataSize()));

        return apiResponseFormat;
    }



    /**
     * 错误消息
     */
    private static final String NAME = "name";

    /**
     * 错误码
     */
    private static final String TYPE = "type";

    /**
     * 数据
     */
    private static final String EXAMPLE_VALUE = "exampleValue";

    /**
     * 数据量
     */
    private static final String DESCRIPTION = "description";

    public static List<JSONObject> transformPublicResult(JSONObject apiResponseFormat){

        List<JSONObject> publicResponseModel = new ArrayList<>();
        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.results))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> publicResponseModel.add(
                        new JSONObject(new LinkedHashMap<String, Object>(){{
                            put(NAME, "results");
                            put(TYPE, "Object");
                            put(EXAMPLE_VALUE, "{}");
                            put(DESCRIPTION, "返回的业务数据");
                        }})));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.message))
                .map(Map.Entry::getKey).findFirst().ifPresent(it -> publicResponseModel.add(
                        new JSONObject(new LinkedHashMap<String, Object>(){{
                            put("name", "message");
                            put("type", "String");
                            put("exampleValue", "success");
                            put("description", "成功或者失败的描述");
                        }})));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.returnCode))
                .map(Map.Entry::getKey).findFirst().ifPresent(it -> publicResponseModel.add(
                        new JSONObject(new LinkedHashMap<String, Object>(){{
                            put("name", "returnCode");
                            put("type", "String");
                            put("exampleValue", "E000000");
                            put("description", "结果码");
                        }})));

        apiResponseFormat.entrySet().stream()
                .filter(it -> it.getValue().toString().equals(ReturnValueTransform.dataSize))
                .map(Map.Entry::getKey).findFirst().ifPresent(dataKey -> publicResponseModel.add(
                        new JSONObject(new LinkedHashMap<String, Object>(){{
                            put("name", "dataSize");
                            put("type", "Integer");
                            put("exampleValue", "0");
                            put("description", "返回的业务数据数据量");
                        }})));
        return publicResponseModel;
    }
}
