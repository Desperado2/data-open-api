package com.github.desperado2.data.open.api.engine.manage.function;

/**
 * 函数接口，实现此接口可自动注册到脚本执行上下文中
 */
public interface IFunction {
    String getVarName();
}
