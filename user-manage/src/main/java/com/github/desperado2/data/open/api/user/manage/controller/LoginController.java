package com.github.desperado2.data.open.api.user.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.user.manage.model.UserLoginModel;
import com.github.desperado2.data.open.api.user.manage.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录
 * @author tu nan
 * @date 2021/3/17
 **/
@RestController
@RequestMapping(value =  Constants.BASE_PATH + "/user")
@Api(tags = "用户登录")
@Validated
public class LoginController {

    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "登录", notes = "[登录]")
    public DataResult<String> login(@RequestBody @Valid UserLoginModel loginRequest) throws DataOpenPlatformException {
        String token = loginService.login(loginRequest);
        return new DataResult<>(token);
    }

    @ApiOperation(value = "登出", notes = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ApiImplicitParam(name = "token", value = "token", defaultValue = "7a98807f761e470c99d78c76e919e79c", required = true, paramType = "header")
    public DataResult<String> logout(@ApiIgnore WebRequest webRequest) {
        String logout = loginService.logout(webRequest);
        return new DataResult<>(logout);
    }


    @ApiOperation(value = "获取可获取验证码的token的接口", notes = "获取可获取验证码的token的接口")
    @RequestMapping(value = "/verification-code-token", method = RequestMethod.GET)
    public DataResult<String> getVerificationCodeToken() {
        String codeToken = loginService.getVerificationCodeToken();
        return new DataResult<>(codeToken);
    }


    @ApiOperation(value = "获取字母验证码", notes = "获取字母验证码")
    @RequestMapping(value = "/verification-code/{verificationCode}", method = RequestMethod.GET)
    public DataResult<String> codeVerificationCode( @ApiIgnore HttpServletResponse servletResponse,
                                         @PathVariable(value = "verificationCode") String verificationCode) {
        String result = loginService.codeVerificationCode(servletResponse, verificationCode);
        return new DataResult<>(result);
    }


    @ApiOperation(value = "获取当前用户的信息", notes = "获取当前用户的信息")
    @RequestMapping(value = "/user-info", method = RequestMethod.GET)
    public DataResult<BaseUserModel> getUser() {
        BaseUserModel result = loginService.getUser();
        return new DataResult<>(result);
    }
}
