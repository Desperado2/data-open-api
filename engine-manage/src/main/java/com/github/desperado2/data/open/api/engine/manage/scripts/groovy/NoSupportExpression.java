package com.github.desperado2.data.open.api.engine.manage.scripts.groovy;


import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 不支持的表达式
 * @author tu nan
 * @date 2023/3/30
 **/
public class NoSupportExpression implements SecureASTCustomizer.ExpressionChecker {

    private final Logger logger = LoggerFactory.getLogger(NoSupportExpression.class);

    public static final List<String> defaultMethodBlacklist = Arrays.asList("getClass", "class", "wait", "notify", "notifyAll", "invokeMethod", "finalize");

    @Override
    public boolean isAuthorized(Expression expression) {
        if (expression instanceof MethodCallExpression) {
            MethodCallExpression mc = (MethodCallExpression) expression;
            String className = mc.getReceiver().getText();
            String method = mc.getMethodAsString();
            if(!isSafe(className, method)){
                logger.info("no safe expression: classname:{}, method:{}", className, method);
                return false;
            }
        }
        return true;
    }


    public Boolean isSafe(String className, String method){
        if ((className.equals("System") || className.equals("java.lang.System")) && "exit".equals(method)) {
            // System.exit(0)
            throw new SecurityException("No call on System.exit() please");
        } else if (className.equals("Runtime") || className.equals("java.lang.Runtime")) {
            // 通过Java的Runtime.getRuntime().exec()方法执行shell, 操作服务器…
            throw new SecurityException("No call on RunTime please");
        } else if ((className.equals("Class") || className.equals("java.lang.Class")) && "forName".equals(method)) {
            // Class.forName
            throw new SecurityException("No call on Class.forName please");
        } else if (defaultMethodBlacklist.contains(method)) {
            // 方法列表黑名单
            throw new SecurityException("Not support method: " + method);
        }
        return true;
    }
}
