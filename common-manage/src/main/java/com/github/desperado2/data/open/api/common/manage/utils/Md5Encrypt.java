package com.github.desperado2.data.open.api.common.manage.utils;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 * @author tu nan
 * @date 2021/3/11
 **/
public class Md5Encrypt {

    private static final char[] DIGITS_DATA = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 对字符串进行MD5算法加密
     * @param text 要加密的字符串
     * @return 加密后的值
     */
    public static String md5Hexdigest(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(text.getBytes("GBK"));
        } catch (Exception e) {
            System.out.println(e);
        }
        assert msgDigest != null;
        return new String(encodeHex(msgDigest.digest())).toUpperCase();
    }


    /**
     * 对字符串进行MD5算法加密
     * @param text 要加密的字符串
     * @param encode 编码
     * @return 加密后的值
     */
    public static String md5Hexdigest(String text, String encode) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(text.getBytes(encode));
        } catch (Exception e) {
            System.out.println(e);
        }
        assert msgDigest != null;
        return new String(encodeHex(msgDigest.digest())).toUpperCase();
    }


    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_DATA[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_DATA[0x0F & data[i]];
        }
        return out;
    }
}
