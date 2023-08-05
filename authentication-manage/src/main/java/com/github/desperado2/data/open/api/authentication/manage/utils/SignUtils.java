package com.github.desperado2.data.open.api.authentication.manage.utils;

import com.github.desperado2.data.open.api.authentication.manage.model.SignResultModel;
import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 签名工具
 * @author tu nan
 * @date 2023/3/17
 **/
public class SignUtils {



    /**
     * 签名
     * @param data 待签名数据
     * @param secret appSecret
     * @return 签名结果
     */
    public static SignResultModel sign(Map<String, Object> data, String secret){
        // 计算签名
        Map<String, Object> result = new LinkedHashMap<>();
        // 按照key的字典序升序排列
        data.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        String param = StringUtils.join(Arrays.asList(result.entrySet().stream().map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
        String signResult = Md5Encrypt.md5Hexdigest(param + secret, "utf-8").toLowerCase();
        return new SignResultModel(param + "APPSECRET", signResult);
    }
}
