package com.github.desperado2.data.open.api.user.manage.service;

import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.user.manage.model.UserLoginModel;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 *
 * @author tu nan
 * @date 2023/2/10
 **/
public interface ILoginService {

    /**
     * 登录
     * @param userLoginModel 登录参数
     * @return token
     * @throws DataOpenPlatformException 不存在
     */
    String login(UserLoginModel userLoginModel) throws DataOpenPlatformException;

    /**
     * 登出
     * @param webRequest 参数
     * @return 结果
     */
    String logout(WebRequest webRequest);

    /**
     * 获取验证码token
     * @return token
     */
    String getVerificationCodeToken();

    /**
     * 获取验证码
     * @param servletResponse 响应
     * @param codeToken 验证码token
     * @return 结果
     */
    String codeVerificationCode(HttpServletResponse servletResponse, String codeToken);

    /**
     * 获取当前登录的用户信息
     * @return 用户信息
     */
    BaseUserModel getUser();
}
