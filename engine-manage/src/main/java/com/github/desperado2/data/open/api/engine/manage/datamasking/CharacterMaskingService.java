package com.github.desperado2.data.open.api.engine.manage.datamasking;


import com.github.desperado2.data.open.api.common.manage.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 字符掩盖脱敏实现
 * 采用指定字符掩盖指定区间的字符
 * 110101199001011234  mask(idCard, 6, 14, '*')  110101********34
 * 不指定值则从0开始，指定值则从指定位置开始。
 * 不指定结束位置则从0开始，指定则从指定位置结束。
 * 不指定掩盖字符，默认字符为*，指定则使用指定的字符。
 * 第一个参数如何为int 则默认为开始位置
 * 第二个参数如果为int 则默认为结束位置。
 * 第三个参数如果为char，则默认为掩盖字符。
 * @author tu nan
 * @date 2023/7/6
 **/
@Service("characterMaskingService")
public class CharacterMaskingService implements IDataMaskingService{

    @Override
    public Object mask(Object originalData, Object... args) {
        if(originalData == null){
            return null;
        }
        String str = String.valueOf(originalData);
        if (str == null || str.isEmpty()) {
            return str;
        }
        if(args.length == 0){
            return realMask(str, 0,str.length(),"*");
        }
        Integer startIndex = getStartIndex(str.length(), args);
        Integer endIndex = getEndIndex(str.length(), args);
        String maskChar = getMaskChar(args);
        int newStartIndex= Math.min(startIndex, endIndex);
        int newEndIndex= Math.max(startIndex, endIndex);
        return realMask(str, Math.min(newStartIndex, str.length()), Math.min(newEndIndex, str.length()), maskChar);
    }

    private String realMask(String originalData, int startIndex, int endIndex, String maskChar){
        char[] chars = originalData.toCharArray();
        char[] chars1 = new char[startIndex];
        char[] chars2 = new char[originalData.length() - endIndex];
        System.arraycopy(chars, 0, chars1, 0, startIndex);
        System.arraycopy(chars, endIndex, chars2, 0, originalData.length() - endIndex);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < (endIndex - startIndex); i++) {
            arrayList.add(maskChar);
        }
        return new String(chars1) + String.join("", arrayList) + new String(chars2);
    }

    private Integer getStartIndex(Integer charLength, Object... args){
        if(args.length == 0){
            return 0;
        }
        if(args[0] instanceof Integer){
            int i = Integer.parseInt(args[0].toString());
            return i >= 0 ? i: charLength + i;
        }
        if(args[0] instanceof String){
            // 是否为数字
            boolean negativeNumber  = ((String) args[0]).startsWith("-");
            if(StringUtils.isNumeric(args[0].toString())){
                int i = Integer.parseInt(args[0].toString());
                return negativeNumber ? charLength + i: i;
            }
            // 小数取整数部分
            String newStr = args[0].toString().split("\\.")[0];
            if(StringUtils.isNumeric(newStr)){
                int i = Integer.parseInt(newStr);
                return negativeNumber ? charLength + i: i;
            }
        }
        return 0;
    }

    private Integer getEndIndex(Integer charLength, Object... args){
        if(args.length < 2){
            return charLength;
        }
        if(args[1] instanceof Integer){
            int i = Integer.parseInt(args[1].toString());
            return i >= 0 ? i: charLength + i;
        }
        if(args[1] instanceof String){
            // 是否为数字
            boolean negativeNumber  = ((String) args[1]).startsWith("-");
            String substring = args[1].toString().substring(1);
            if(StringUtils.isNumeric(substring)){
                int i = Integer.parseInt(args[1].toString());
                return negativeNumber ? charLength + i: i;
            }
            // 小数取整数部分
            String newStr = substring.split("\\.")[0];
            if(StringUtils.isNumeric(newStr)){
                int i = Integer.parseInt(newStr);
                return negativeNumber ? charLength + i: i;
            }
        }
        return charLength;
    }

    private String getMaskChar(Object... args){
        if(args.length == 0){
            return "*";
        }
        if(args.length == 1){
            if(NumberUtils.getStartIndex(args[0])){
                return "*";
            }
            return args[0].toString();
        }
        if(args.length == 2){
            if(NumberUtils.getStartIndex(args[0])){
                if(NumberUtils.getStartIndex(args[1])){
                    return "*";
                }
                return args[1].toString();
            }
            return args[0].toString();
        }
        return args[2].toString();
    }

    public static void main(String[] args) {
        CharacterMaskingService characterMaskingService = new CharacterMaskingService();
        System.out.println(characterMaskingService.mask("12345678900", 3,6, '*'));
    }
}
