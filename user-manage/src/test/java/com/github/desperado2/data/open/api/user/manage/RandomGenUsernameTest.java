package com.github.desperado2.data.open.api.user.manage;


import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import com.github.desperado2.data.open.api.common.manage.utils.StrGeneralUtils;

import java.util.Locale;

/**
 * 随机生成用户名
 * @author tu nan
 * @date 2023/2/13
 **/
public class RandomGenUsernameTest {

    public static String genRandomUsername(Integer len){
      return "";
    }

    public static void main(String[] args) {
        String password = StrGeneralUtils.getPassword(8, true, true, true);
        password = "lsywc@" + password;
        System.out.println(password);
        // 密码加密
        System.out.println(AESUtils.encrypt(Md5Encrypt.md5Hexdigest(password).toUpperCase(Locale.ROOT), null));

    }
}
