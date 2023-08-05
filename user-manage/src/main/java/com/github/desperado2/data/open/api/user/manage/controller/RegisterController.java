package com.github.desperado2.data.open.api.user.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.user.manage.model.UserRegisterModel;
import com.github.desperado2.data.open.api.user.manage.service.IRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 注册
 * @author tu nan
 * @date 2021/3/17
 **/
@RestController
@RequestMapping(value =  Constants.BASE_PATH + "/user")
@Api(tags = "用户注册")
@Validated
public class RegisterController {

    private final IRegisterService registerService;

    public RegisterController(IRegisterService registerService) {
        this.registerService = registerService;
    }


    @PostMapping("/register")
    @ResponseBody
    @ApiOperation(value = "注册", notes = "[注册]")
    public DataResult<Void> register(@RequestBody @Valid UserRegisterModel userRegisterModel) throws  DataOpenPlatformException {
        registerService.register(userRegisterModel);
        return new DataResult<>();
    }
//
//    @GetMapping("/active/{token}")
//    @ResponseBody
//    @ApiOperation(value = "激活", notes = "[激活]")
//    public DataResult<Void> active(@PathVariable("token") String token, HttpServletRequest request) throws  DataOpenPlatformException {
//        registerService.activateUserNoLogin(token, request);
//        return new DataResult<>();
//    }

}
