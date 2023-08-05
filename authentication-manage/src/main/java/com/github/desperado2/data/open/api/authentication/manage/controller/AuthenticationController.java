package com.github.desperado2.data.open.api.authentication.manage.controller;

import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretModel;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretQueryRequest;
import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证信息
 * @author tu nan
 * @date 2023/2/15
 **/
@RestController
@RequestMapping(Constants.BASE_PATH + "/authentication")
@Api(tags = "认证信息管理")
public class AuthenticationController {

    @Autowired
    private IKeySecretService keySecretService;

    @PostMapping("list-by-user")
    @ResponseBody
    @ApiOperation(value = "查询秘钥列表", notes = "[接口申请]")
    public DataResult<List<KeySecretModel>> list() throws DataOpenPlatformException {
        List<KeySecretModel> keySecretByUser = keySecretService.getKeySecretByUser(null);
        return new DataResult<>(keySecretByUser);
    }


    @GetMapping("list-by-user/{userId}")
    @ResponseBody
    @ApiOperation(value = "根据用户查询秘钥列表", notes = "[接口申请]")
    @ApiImplicitParam(name = "userId", value = "用户id")
    public DataResult<List<KeySecretModel>> listByUser(@PathVariable("userId") Long userId) throws DataOpenPlatformException {
        List<KeySecretModel> keySecretByUser = keySecretService.getKeySecretByUser(userId);
        return new DataResult<>(keySecretByUser);
    }

    @GetMapping("secret/{key}")
    @ResponseBody
    @ApiOperation(value = "根据key查询秘钥", notes = "[接口申请]")
    public DataResult<String> selectSecretByKey(@PathVariable("key") String key){
        String secret = keySecretService.getSecretByAppKey(key);
        return new DataResult<>(secret);
    }

    @PutMapping("reset")
    @ResponseBody
    @ApiOperation(value = "重置用户的Key和secret", notes = "[接口申请]")
    public DataResult<String> reset() throws DataOpenPlatformException {
        String secret = keySecretService.reset(null);
        return new DataResult<>(secret);
    }

    @PutMapping("reset/{userId}")
    @ResponseBody
    @ApiOperation(value = "通过用户id重置用户的Key和secret", notes = "[接口申请]")
    @ApiImplicitParam(name = "userId", value = "用户id")
    public DataResult<String> resetByUserId(@PathVariable("userId") Long userId) throws DataOpenPlatformException {
        String secret = keySecretService.reset(userId);
        return new DataResult<>(secret);
    }

    @PostMapping("list-all")
    @ResponseBody
    @ApiOperation(value = "查询全部秘钥", notes = "[接口申请]")
    public DataResult<PageResults<KeySecretModel>> list(@RequestBody KeySecretQueryRequest keySecretQueryRequest) throws DataOpenPlatformException {
        keySecretService.listAll(keySecretQueryRequest);
        return new DataResult<>();
    }
}
