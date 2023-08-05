package com.github.desperado2.data.open.api.authentication.manage;


import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author tu nan
 * @date 2023/2/28
 **/
public class Test {

    public static void main(String[] args) {
        // 生成新的key
        String key = "dfd167aa7b284e7fa09b4a9905465cb2";
        // 生成信息的secret
        String secret = "37dff6fc19ee4ab78a19a9c9a3ce7713";
        // 加密
        // 按照key的字典序升序排列
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", 1);
        map.put("provinceName", "浙江省");
        map.put("appKey", key);
        map.put("signTime", 20230316185500L);

        Map<String, Object> result = new LinkedHashMap<>();
        map.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        String param = StringUtils.join(Arrays.asList(result.entrySet().stream().map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");


        String signResult = Md5Encrypt.md5Hexdigest(param + secret, "utf-8").toLowerCase();

        System.out.println(signResult);
    }
}
