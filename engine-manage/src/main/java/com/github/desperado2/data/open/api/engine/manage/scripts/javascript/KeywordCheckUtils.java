package com.github.desperado2.data.open.api.engine.manage.scripts.javascript;


import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 关键词检测
 * @author tu nan
 * @date 2023/3/30
 **/
public class KeywordCheckUtils {

    private static final Logger logger = LoggerFactory.getLogger(KeywordCheckUtils.class);

    private static final Set<String> PUBLIC_BLACK_LIST = Sets.newHashSet(
            // Java 全限定类名
            "java.io.File", "java.io.RandomAccessFile", "java.io.FileInputStream", "java.io.FileOutputStream",
            "java.lang.Class", "java.lang.ClassLoader", "java.lang.Runtime", "java.lang.System", "System.getProperty",
            "java.lang.Thread", "java.lang.ThreadGroup", "java.lang.reflect.AccessibleObject", "java.net.InetAddress",
            "java.net.DatagramSocket", "java.net.DatagramSocket", "java.net.Socket", "java.net.ServerSocket",
            "java.net.MulticastSocket", "java.net.MulticastSocket", "java.net.URL", "java.net.HttpURLConnection",
            "java.security.AccessControlContext", "java.lang.ProcessBuilder",
            //反射关键字
            "invoke","newinstance",
            // JavaScript 方法
            "eval", "new function",
            //引擎特性
            "Java.type","importPackage","importClass","JavaImporter"
    );


    public static boolean isSafeJavScriptScript(String script) throws Exception {
        // 去除注释
        String removeComment = script.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*[\n\r\u2029\u2028])", " ");
        //去除特殊字符
        removeComment =removeComment.replaceAll("[\u2028\u2029\u00a0\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\ufeff]","");
        // 去除空格
        String removeWhitespace = removeComment.replaceAll( "\\s+", "");
        // 多个空格替换为一个
        String oneWhiteSpace = removeComment.replaceAll( "\\s+", " ");
        Set<String> insecure = PUBLIC_BLACK_LIST.stream().filter(s -> StringUtils.containsIgnoreCase(removeWhitespace, s) ||
                StringUtils.containsIgnoreCase(oneWhiteSpace, s)).collect(Collectors.toSet());

        if (!CollectionUtils.isEmpty(insecure)) {
            logger.error("存在不安全的关键字:{}", insecure);
            return false;
        }
        return true;
    }
}
