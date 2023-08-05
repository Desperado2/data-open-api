package com.github.desperado2.data.open.api.common.manage.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * DES加密类
 * @author tu nan
 * @date 2021/3/11
 **/
public class EncryptUtil {

    private static final String KEY = "DOA-open";

    /**
     * 加密
     *
     * @param content 待加密内容
     * @return 加密结果
     */
    public static String encrypt(byte[] content) {
        try {
            DESKeySpec desKey = new DESKeySpec(KEY.getBytes(StandardCharsets.US_ASCII));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv2 = new IvParameterSpec(KEY.getBytes(StandardCharsets.US_ASCII));
            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv2);
            byte[] bytes = cipher.doFinal(content);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     * @param encryptText 解密文本
     * @return 解密结果
     */
    public static String desDecrypt(byte[] encryptText) {
        try {
            encryptText = Base64.getDecoder().decode(encryptText);
            DESKeySpec desKey = new DESKeySpec(KEY.getBytes(StandardCharsets.US_ASCII));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv2 = new IvParameterSpec(KEY.getBytes(StandardCharsets.US_ASCII));
            cipher.init(Cipher.DECRYPT_MODE, securekey, iv2);
            return new String(cipher.doFinal(encryptText));
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }




    public static void main(String[] args) throws Exception {
        String msg = "e3fb09c9126cd8bc12399e56a35162c4e3fb09c9126cd8bc12399e56a35162c4";
        String encrypt = encrypt(msg.getBytes());
        System.out.println(encrypt);
        System.out.println(desDecrypt(encrypt.getBytes()));
    }
}
