package com.github.desperado2.data.open.api.user.manage.controller;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.DataResult;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.model.RoleRequestModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleResponseModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleSearchModel;
import com.github.desperado2.data.open.api.user.manage.service.IRoleService;
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
@RequestMapping(value =  Constants.BASE_PATH + "/role")
@Api(tags = "角色管理")
@Validated
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation(value = "新增角色", notes = "[角色]")
    public DataResult<Void> add(@RequestBody @Valid RoleRequestModel roleRequestModel) throws DataOpenPlatformException {
        roleService.add(roleRequestModel);
        return new DataResult<>();
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "修改角色", notes = "[角色]")
    public DataResult<Void> update(@RequestBody @Valid RoleRequestModel roleRequestModel) throws DataOpenPlatformException {
        roleService.update(roleRequestModel);
        return new DataResult<>();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除角色", notes = "[角色]")
    public DataResult<Void> delete(@PathVariable("id")Long id) throws DataOpenPlatformException {
        roleService.delete(id);
        return new DataResult<>();
    }

    @PostMapping("/list")
    @ApiOperation(value = "角色列表", notes = "[角色]")
    public DataResult<PageResults<List<RoleResponseModel>>> list(@RequestBody RoleSearchModel roleSearchModel) throws DataOpenPlatformException {
        PageResults<List<RoleResponseModel>> listPageResults = roleService.pageList(roleSearchModel);
        return new DataResult<>(listPageResults);
    }

}
