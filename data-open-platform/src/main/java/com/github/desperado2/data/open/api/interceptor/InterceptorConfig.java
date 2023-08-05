package com.github.desperado2.data.open.api.interceptor;


import com.github.desperado2.data.open.api.authentication.manage.interceptor.PermissionInterceptor;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.log.manage.interceptor.LogInterceptor;
import com.github.desperado2.data.open.api.security.manage.interceptor.AbstractAccessLimiterInterceptor;
import com.github.desperado2.data.open.api.user.manage.interceptor.LoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 * @author tu nan
 * @date 2023/2/14
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

    // 外部接口授权拦截器
    private final PermissionInterceptor permissionInterceptor;

    // 外部请求日志拦截器
    private final LogInterceptor logInterceptor;

    // 外部请求限流拦截器
    private final AbstractAccessLimiterInterceptor abstractAccessLimiterInterceptor;

    // 登录拦截器
    private final LoginInterceptor loginInterceptor;

    private final OpenApiProperties openApiProperties;


    @Value("${open.data.platform.base.open-swagger:false}")
    private Boolean openSwagger;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/ui/");
        registry.addViewController("/ui/").setViewName("forward:/ui/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        if(openSwagger){
            registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        registry.addResourceHandler("/ui/**").addResourceLocations("file:ui/");
    }

    public InterceptorConfig(PermissionInterceptor permissionInterceptor,
                             LogInterceptor logInterceptor,
                             AbstractAccessLimiterInterceptor abstractAccessLimiterInterceptor,
                             LoginInterceptor loginInterceptor, OpenApiProperties openApiProperties) {
        this.permissionInterceptor = permissionInterceptor;
        this.logInterceptor = logInterceptor;
        this.abstractAccessLimiterInterceptor = abstractAccessLimiterInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.openApiProperties = openApiProperties;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 开放接口
        String[] pathPatterns = {
                openApiProperties.getBaseRegisterPath()+"/new-search/v1/**",
                openApiProperties.getBaseRegisterPath()+"/**",
        };
        registry.addInterceptor(logInterceptor).addPathPatterns(pathPatterns);
        registry.addInterceptor(permissionInterceptor).addPathPatterns(pathPatterns);
        registry.addInterceptor(abstractAccessLimiterInterceptor.handlerInterceptorAdapter()).addPathPatterns(pathPatterns);

        // 无需登录接口
        String[] swaggerPathPatterns = new String[]{
                //swagger
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html",
                "/v2/**",
                "/doc.html",
                "/error",
                "/",
                "/ui",
                "/ui/**",
                //静态资源
                "/webjars/**",
                Constants.BASE_PATH + "/swagger-resources/configuration/ui",
                Constants.BASE_PATH + "/swagger-resources",
                Constants.BASE_PATH + "/swagger-resources/configuration/security",
                Constants.BASE_PATH + "/swagger-ui.html",
                Constants.BASE_PATH + "/v2/**",
                Constants.BASE_PATH + "/v3/**",
                Constants.BASE_PATH + "/doc.html",
                Constants.BASE_PATH + "/webjars/**",

                Constants.BASE_PATH + "/user/login",
                Constants.BASE_PATH + "/user/logout",
                Constants.BASE_PATH + "/user/verification-code-token",
                Constants.BASE_PATH + "/user/verification-code/**",
                Constants.BASE_PATH + "/user/register",
                Constants.BASE_PATH + "/user/active/**",
                openApiProperties.getBaseRegisterPath() + "/**",
                openApiProperties.getBaseRegisterPath() + "/**/**",
                "/open-data-platform/classify/index-list",
                "/open-data-platform/api/index-list",
                "/open-data-platform/api/index-list/**",
                "/open-data-platform/api/index/**",
                "/open-data-platform/api/index/*",
                "/open-data-platform/api/search/*",
        };

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns(pathPatterns).excludePathPatterns(swaggerPathPatterns);
    }

    public Boolean getOpenSwagger() {
        return openSwagger;
    }

    public void setOpenSwagger(Boolean openSwagger) {
        this.openSwagger = openSwagger;
    }
}
