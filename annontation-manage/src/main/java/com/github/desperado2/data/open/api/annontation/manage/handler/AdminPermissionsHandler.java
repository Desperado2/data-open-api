package com.github.desperado2.data.open.api.annontation.manage.handler;


import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 权限拦截器
 * @author tu nan
 * @date 2023/5/22
 **/
@Aspect
@Component
public class AdminPermissionsHandler {

    private final UserInfoProvider userInfoProvider;

    public AdminPermissionsHandler(UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }


    // 切入点
    @Pointcut("@annotation(com.github.desperado2.data.open.api.annontation.manage.annontation.AdminPermissions)")
    public void cutPoint(){}

    // 前置拦截
    @Before("cutPoint()")
    public void beforePointCut(JoinPoint joinPoint) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
    }
}
