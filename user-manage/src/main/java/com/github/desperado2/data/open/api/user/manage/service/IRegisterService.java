package com.github.desperado2.data.open.api.user.manage.service;


import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.user.manage.model.UserRegisterModel;

import javax.servlet.http.HttpServletRequest;

/**
 * 注册接口
 * @author tu nan
 * @date 2023/2/10
 **/
public interface IRegisterService {

    /**
     * 注册
     * @param userRegisterModel 注册用户
     * @throws DataOpenPlatformException 失败
     */
    void register(UserRegisterModel userRegisterModel) throws DataOpenPlatformException;

    /**
     * 激活
     * @param token token
     * @param request 请求
     */
    void activateUserNoLogin(String token, HttpServletRequest request) throws DataOpenPlatformException;


    void activateUserNoLogin(Long userId) throws DataOpenPlatformException;
}
