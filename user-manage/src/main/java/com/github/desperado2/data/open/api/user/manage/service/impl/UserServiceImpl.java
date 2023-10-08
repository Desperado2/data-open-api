package com.github.desperado2.data.open.api.user.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.AlertServiceTypeEnum;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.MessageTypeEnums;
import com.github.desperado2.data.open.api.alert.manage.service.factory.AlertService;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.config.UserConfig;
import com.github.desperado2.data.open.api.user.manage.entity.Role;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.enums.UserStatusEnum;
import com.github.desperado2.data.open.api.user.manage.mapper.UserMapper;
import com.github.desperado2.data.open.api.user.manage.model.UpdateRoleModel;
import com.github.desperado2.data.open.api.user.manage.model.UserListModel;
import com.github.desperado2.data.open.api.user.manage.model.UserSearchModel;
import com.github.desperado2.data.open.api.user.manage.service.IRoleService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final IRoleService roleService;

    private final AlertService alertService;

    private final UserInfoProvider userInfoProvider;

    private final UserConfig userConfig;

    final static Pattern EMAIL_PARTERN = Pattern.compile("[a-zA-Z0-9]+[\\.]{0,1}[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+");

    public UserServiceImpl(IRoleService roleService, AlertService alertService, UserInfoProvider userInfoProvider, UserConfig userConfig) {
        this.roleService = roleService;
        this.alertService = alertService;
        this.userInfoProvider = userInfoProvider;
        this.userConfig = userConfig;
    }

    @Override
    public void add(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        // 填充数据
        String email = updateRoleModel.getEmail();
        // 判断邮箱是否合法
        if(EMAIL_PARTERN.matcher(email).matches()){
            throw new DataOpenPlatformException("邮箱格式不正确");
        }
        // 判断邮箱是否存在
        User existUser = selectByEmail(email);
        if(existUser != null){
            throw new DataOpenPlatformException(email + "已经被注册");
        }
        User user = new User();
        user.setName(updateRoleModel.getName());
        user.setEmail(updateRoleModel.getEmail());
        user.setStatus(UserStatusEnum.NOT_ACTIVATED.getCode());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        Role defaultRole = roleService.getDefaultRole();
        if(defaultRole != null){
            user.setRoleId(defaultRole.getId());
        }else{
            // 未找到默认角色告警
            throw new DataOpenPlatformException("注册未找到默认角色，请先指定默认角色");
        }
        this.save(user);
    }

    @Override
    public void saveUser(User user) {
        user.setStatus(UserStatusEnum.NOT_ACTIVATED.getCode());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        Role defaultRole = roleService.getDefaultRole();
        if(defaultRole != null){
            user.setRoleId(defaultRole.getId());
        }else{
            // 未找到默认角色告警
            alertService.getScriptExecutor(AlertServiceTypeEnum.DINGDING).alert("用户:" + user.getName() +"注册未找到默认角色，请尽快授予其默认角色", MessageTypeEnums.TEXT);
        }
        this.save(user);
    }

    @Override
    public User selectByEmail(String email) {
        return this.getOne(new QueryWrapper<User>().lambda().eq(User::getEmail, email)
                .last(" limit 1"));
    }

    @Override
    public void activeUser(User user) {
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        user.setUpdateTime(new Date());
        this.updateById(user);
    }


    @Override
    public PageResults<List<UserListModel>> pageList(UserSearchModel userSearchModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(userSearchModel.getEmail())){
            wrapper.eq(User::getEmail, userSearchModel.getEmail());
        }
        if(StringUtils.isNotBlank(userSearchModel.getName())){
            wrapper.eq(User::getName, userSearchModel.getName());
        }
        if(userSearchModel.getStatus() != null){
            wrapper.eq(User::getStatus, userSearchModel.getStatus());
        }
        IPage<User> page = new Page<>(userSearchModel.getCurrent(), userSearchModel.getPageSize());
        IPage<User> userIPage = this.getBaseMapper().selectPage(page, wrapper);
        List<UserListModel> userListModels = userIPage.getRecords().stream().map(it -> JSONObject.parseObject(JSONObject.toJSONString(it), UserListModel.class))
                .collect(Collectors.toList());

        return new PageResults<>(userListModels, new PageInfo(userSearchModel.getCurrent(), userSearchModel.getPageSize(), userIPage.getTotal()));
    }

    @Override
    public void updateRole(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 判断用户是否存在
        User oldUser = this.getBaseMapper().selectById(updateRoleModel.getId());
        if(oldUser == null){
            throw new DataOpenPlatformException("用户不存在");
        }
        // 判断角色是否存在
        Role role = roleService.getBaseMapper().selectById(updateRoleModel.getRoleId());
        if(role == null){
            throw new DataOpenPlatformException("角色不存在");
        }
        // 更新
        User user = new User();
        user.setId(oldUser.getId());
        user.setRoleId(role.getId());
        user.setUpdateTime(new Date());
        this.updateById(user);
    }

    @Override
    public void updateUser(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 判断用户是否存在
        User oldUser = this.getBaseMapper().selectById(updateRoleModel.getId());
        if(oldUser == null){
            throw new DataOpenPlatformException("用户不存在");
        }
        // 判断角色是否存在
        Role role = roleService.getBaseMapper().selectById(updateRoleModel.getRoleId());
        if(role == null){
            throw new DataOpenPlatformException("角色不存在");
        }
        // 更新
        User user = new User();
        user.setId(oldUser.getId());
        user.setRoleId(role.getId());
        user.setName(updateRoleModel.getName());
        user.setEmail(updateRoleModel.getEmail());
        user.setStatus(updateRoleModel.getStatus());
        user.setUpdateTime(new Date());
        this.updateById(user);
    }

    @Override
    public Boolean userExistByRole(Long roleId) {
        return this.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getRoleId, roleId).last("limit 1")) != null;
    }

    @Override
    public Integer normalCount() {
        return this.getBaseMapper().selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, UserStatusEnum.NORMAL.getCode()));
    }

}
