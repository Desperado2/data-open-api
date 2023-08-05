//package com.github.desperado2.open.data.platform.engine.manage.scripts.groovy;
//
//
//
//
//import groovy.lang.GroovyInterceptable;
//
//import java.util.Arrays;
//import java.util.GregorianCalendar;
//import java.util.List;
//
///**
// * 拦截器
// * @author tu nan
// * @date 2023/3/30
// **/
//public class GroovyNotSupportInterceptor extends GroovyInterceptable {
//
//    public static final List<String> defaultMethodBlacklist = Arrays.asList("getClass", "class", "wait", "notify", "notifyAll", "invokeMethod", "finalize");
//
//
//    /**
//     * 静态方法拦截
//     * @param invoker
//     * @param receiver
//     * @param method
//     * @param args
//     * @return
//     * @throws Throwable
//     */
//    @Override
//    public Object onStaticCall(GroovyInterceptor.Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
//        if (receiver == System.class && "exit".equals(method)) {
//            // System.exit(0)
//            throw new SecurityException("No call on System.exit() please");
//        } else if (receiver == Runtime.class) {
//            // 通过Java的Runtime.getRuntime().exec()方法执行shell, 操作服务器…
//            throw new SecurityException("No call on RunTime please");
//        } else if (receiver == Class.class && "forName".equals(method)) {
//            // Class.forName
//        }
//        return super.onStaticCall(invoker, receiver, method, args);
//    }
//
//    /**
//     * 普通方法拦截
//     * @param invoker
//     * @param receiver
//     * @param method
//     * @param args
//     * @return
//     * @throws Throwable
//     */
//    @Override
//    public Object onMethodCall(GroovyInterceptor.Invoker invoker, Object receiver, String method, Object... args) throws Throwable {
//        if (defaultMethodBlacklist.contains(method)) {
//            // 方法列表黑名单
//            throw new SecurityException("Not support method: " + method);
//        }
//        return super.onMethodCall(invoker, receiver, method, args);
//    }
//
//    @Override
//    public Object onGetProperty(Invoker invoker, Object receiver, String property) throws Throwable {
//        if ("class".contains(property)) {
//            throw new SecurityException("Not support clz.class");
//        }
//        return super.onGetProperty(invoker, receiver, property);
//    }
//}
