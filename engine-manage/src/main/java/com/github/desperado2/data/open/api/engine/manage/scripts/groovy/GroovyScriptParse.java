package com.github.desperado2.data.open.api.engine.manage.scripts.groovy;//package com.github.desperado2.open.data.platform.api.manage.extend.scripts;

import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.engine.manage.ApiInfoContent;
import com.github.desperado2.data.open.api.engine.manage.function.IFunction;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.scripts.IScriptParse;
import groovy.lang.GroovyClassLoader;
import groovy.transform.ConditionalInterrupt;
import groovy.transform.ThreadInterrupt;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.codehaus.groovy.syntax.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.*;

@Service("groovyScriptExecutor")
public class GroovyScriptParse implements IScriptParse {

    @Autowired
    private ApiInfoContent apiInfoContent;

    @Autowired
    private ApplicationContext context;

    private Collection<IFunction> functionList;

    private ScriptEngineManager factory = new ScriptEngineManager();

    private ScriptEngine engine = null;

    @PostConstruct
    public void init(){
        //初始化引擎
        // 创建SecureASTCustomizer
        final SecureASTCustomizer secure = new SecureASTCustomizer();
        // 禁止使用闭包
        secure.setClosuresAllowed(true);

        // 添加关键字黑名单 while和goto
        List<Integer> tokensBlacklist = new ArrayList<>();
        tokensBlacklist.add(Types.KEYWORD_WHILE);
        tokensBlacklist.add(Types.KEYWORD_GOTO);
        secure.setTokensBlacklist(tokensBlacklist);

        // 设置直接导入检查
        secure.setIndirectImportCheckEnabled(true);

        // 添加导入黑名单，用户不能导入JSONObject
        List<String> list = new ArrayList<>();
        list.add("com.alibaba.fastjson.JSONObject");
        secure.setImportsBlacklist(list);

        // statement 黑名单，不能使用while循环块
        List<Class<? extends Statement>> statementBlacklist = new ArrayList<>();
        statementBlacklist.add(WhileStatement.class);
        secure.setStatementsBlacklist(statementBlacklist);

        // 添加表达式检测
        secure.addExpressionCheckers(new NoSupportExpression());

        // 自定义CompilerConfiguration，设置AST
        final CompilerConfiguration config = new CompilerConfiguration();
        //添加线程中断拦截器，可拦截循环体（for,while）、方法和闭包的首指令
        config.addCompilationCustomizers(secure, new ASTTransformationCustomizer(ThreadInterrupt.class));

        // 添加线程中断拦截器，可中断超时线程
        // TimedInterrupt是线程监控的，不适合缓存脚本超时判断
        Map<String, Object> timeoutArgs = new HashMap<>();
        timeoutArgs.put("value", new ClosureExpression(
                Parameter.EMPTY_ARRAY,
                new ExpressionStatement(
                        new MethodCallExpression(
                                new ClassExpression(ClassHelper.make(ConditionInterceptor.class)),
                                "checkTimeout",
                                new ConstantExpression(30))
                )
        ));
        config.addCompilationCustomizers(new ASTTransformationCustomizer(timeoutArgs, ConditionalInterrupt.class));
        // 沙盒环境
//        config.addCompilationCustomizers(new SandboxTransformer());
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(this.getClass().getClassLoader(), config);
        engine = new GroovyScriptEngineImpl(groovyClassLoader);
//        new GroovyNotSupportInterceptor().register();
        //加载函数
        functionList = context.getBeansOfType(IFunction.class).values();
    }

    @Override
    public Object runScript(ApiExecuteEnvironmentEnum environmentEnum, String script, ApiInfo apiInfo, ApiParams apiParams) throws Throwable {
        try {
            //注入变量
            apiInfoContent.setApiInfo(apiInfo);
            apiInfoContent.setApiParams(apiParams);
            Bindings bindings = new SimpleBindings();
            apiInfoContent.setEngineBindings(bindings);
            for(IFunction function : functionList){
                bindings.put(function.getVarName(),function);
            }
            //注入属性变量
            apiInfoContent.setApiExecuteEnvironmentEnum(environmentEnum);
            apiInfoContent.setRequestParams(buildScriptParams(apiParams, bindings));
            //手动开启事务
            ThreadLocalUtils.setStartTime();
            ThreadLocalUtils.set("scriptName", apiInfo.getName());
            return this.engineEval(script,bindings);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Object engineEval(String script,Bindings bindings) throws Throwable {
        return engine.eval(script,bindings);
    }
}
