package com.github.desperado2.data.open.api.common.manage.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.List;
import java.util.Objects;

/**
 * JSON格式化
 * @author tu nan
 * @date 2023/4/13
 **/
public class JsonUtils {

    public static String toJson(Object data) {
        return JSONObject.toJSONString(data);
    }

    public static <T> T toBean(String json, Class<T> t) {
        return JSONObject.parseObject(json, t);
    }

    public static <T> List<T> toList(String json, Class<T> t) {
        return JSONObject.parseArray(json, t);
    }

    public static <T> List<T> toList(Object object, Class<T> t) {
        return JSONObject.parseArray(JSON.toJSONString(object), t);
    }

    public static <T> T toBean(Object source, Class<T> t) {
        Objects.requireNonNull(source, "source can not be null");
        return JSONObject.parseObject(source instanceof String ? source.toString() : JSON.toJSONString(source), t);
    }

    public static <T> T toBean(Object source, Class<T> t, SerializeFilter serializeFilter) {
        Objects.requireNonNull(source, "source can not be null");
        return JSONObject.parseObject(source instanceof String ? source.toString() : JSON.toJSONString(source, new SerializeFilter[]{serializeFilter, filter}), t);
    }


    private static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if (v == null) {
                return "";
            }
            return v;
        }
    };
}
