package com.github.desperado2.data.open.api.validator;


import com.github.desperado2.data.open.api.config.EndPointProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 自定义格式校验器
 * @author tu nan
 * @date 2023/4/12
 **/
public class CustomPropertyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EndPointProperty.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EndPointProperty endPointProperty = (EndPointProperty) target;
        // 判断swagger
        if(endPointProperty.getOpenSwagger()){
            throw new IllegalArgumentException("线上请关闭swagger，请配置open.data.platform.open-swagger=false");
        }
        // 判断Redis
        if(StringUtils.isBlank(endPointProperty.getRedisHost()) || endPointProperty.getRedisPort().equals(-1)){
            // 未配置redis
            if(StringUtils.isNotBlank(endPointProperty.getCacheType()) && endPointProperty.getCacheType().equals("redis")){
                throw new IllegalArgumentException("未配置REDIS，请配置open.data.platform.cache.type=local");
            }
            if(endPointProperty.getAccessLimiterOpen() != null && endPointProperty.getAccessLimiterOpen()
                && StringUtils.isNotBlank(endPointProperty.getAccessLimiterType()) && endPointProperty.getAccessLimiterType().equals("redis")){
                throw new IllegalArgumentException("未配置REDIS，请配置open.data.platform.access.limiter.type=local");
            }
        }
        // 判断必须配置信息
        if(StringUtils.isBlank(endPointProperty.getOpenApiBaseRegisterPath())){
            throw new IllegalArgumentException("配置open.data.platform.open-api.base-register-path不能为空");
        }else if(!endPointProperty.getOpenApiBaseRegisterPath().trim().startsWith("/")){
            throw new IllegalArgumentException("配置open.data.platform.open-api.base-register-path必须以/开头");
        }else if(endPointProperty.getOpenApiBaseRegisterPath().trim().endsWith("/")){
            throw new IllegalArgumentException("配置open.data.platform.open-api.base-register-path必须不能以/结尾");
        }

        if(StringUtils.isBlank(endPointProperty.getOpenApiServiceAddress())){
            throw new IllegalArgumentException("配置open.data.platform.open-api.service-address不能为空");
        }else if (!endPointProperty.getOpenApiServiceAddress().trim().startsWith("http")){
            throw new IllegalArgumentException("配置open.data.platform.open-api.service-address不是正确的URL");
        }
    }
}
