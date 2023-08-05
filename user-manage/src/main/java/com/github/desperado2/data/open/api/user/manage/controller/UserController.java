package com.github.desperado2.data.open.api.user.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.model.UpdateRoleModel;
import com.github.desperado2.data.open.api.user.manage.model.UserListModel;
import com.github.desperado2.data.open.api.user.manage.model.UserSearchModel;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 * @author tu nan
 * @date 2023/2/13
 **/
@RestController
@RequestMapping(value =  Constants.BASE_PATH + "/user-manage")
@Api(tags = "用户管理")
@Validated
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation(value = "新增角色", notes = "[角色]")
    public DataResult<Void> add(@RequestBody @Valid UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        userService.add(updateRoleModel);
        return new DataResult<>();
    }

    @PostMapping("/list")
    @ApiOperation(value = "用户列表", notes = "[用户管理]")
    public DataResult<PageResults<List<UserListModel>>> list(@RequestBody UserSearchModel userSearchModel) throws DataOpenPlatformException {
        PageResults<List<UserListModel>> listPageResults = userService.pageList(userSearchModel);
        return new DataResult<>(listPageResults);
    }


    @PostMapping("/update-role")
    @ApiOperation(value = "修改角色", notes = "[用户管理]")
    public DataResult<String> updateRole(@RequestBody UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        userService.updateRole(updateRoleModel);
        return new DataResult<>();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户", notes = "[用户管理]")
    public DataResult<String> updateUser(@RequestBody UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        userService.updateUser(updateRoleModel);
        return new DataResult<>();
    }

}
