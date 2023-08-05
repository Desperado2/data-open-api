package com.github.desperado2.data.open.api.authentication.manage.controller;

import com.github.desperado2.data.open.api.authentication.manage.model.SignResultModel;
import com.github.desperado2.data.open.api.authentication.manage.service.ISignService;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证信息
 * @author tu nan
 * @date 2023/2/15
 **/
@RestController
@RequestMapping(Constants.BASE_PATH + "/sign-utils")
@Api(tags = "签名信息管理")
public class SignController {

    @Autowired
    private ISignService signService;

    @PostMapping("sign-by-user")
    @ResponseBody
    @ApiOperation(value = "根据用户签名", notes = "[签名信息]")
    public DataResult<SignResultModel> signByUser(@RequestBody Map<String, Object> data) throws DataOpenPlatformException {
        return new DataResult<>(signService.signByUser(data));
    }

}
