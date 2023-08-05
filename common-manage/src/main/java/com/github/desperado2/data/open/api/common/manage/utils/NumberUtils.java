package com.github.desperado2.data.open.api.common.manage.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 判断是否为数字工具类
 * @author tu nan
 * @date 2023/7/6
 **/
public class NumberUtils {

    public static Boolean getStartIndex(Object data){
        if(data == null){
            return false;
        }
        if(data instanceof Integer){
            return true;
        }
        if(data instanceof String){
            // 是否为数字
            if(StringUtils.isNumeric(data.toString())){
                return true;
            }
            // 小数取整数部分
            String newStr = data.toString().split("\\.")[0];
            if(StringUtils.isNumeric(newStr)){
                return true;
            }
        }
        return false;
    }
}
