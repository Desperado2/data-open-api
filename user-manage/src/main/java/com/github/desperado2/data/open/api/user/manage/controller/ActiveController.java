package com.github.desperado2.data.open.api.user.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.utils.ServerUtils;
import com.github.desperado2.data.open.api.user.manage.service.IRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 注册
 * @author tu nan
 * @date 2021/3/17
 **/
@RestController
@RequestMapping(value =  Constants.BASE_PATH + "/user")
@Api(tags = "用户激活")
@Validated
public class ActiveController {

    private final IRegisterService registerService;

    public ActiveController(IRegisterService registerService) {
        this.registerService = registerService;
    }


    @GetMapping("/active/{userId}")
    @ResponseBody
    @ApiOperation(value = "激活", notes = "[激活]")
    public DataResult<String> active(@PathVariable("userId") Long userId) throws DataOpenPlatformException {
        registerService.activateUserNoLogin(userId);
        return new DataResult<>();
    }

}
