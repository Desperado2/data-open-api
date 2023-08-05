package com.github.desperado2.data.open.api.user.manage.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleStatusEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.entity.Role;
import com.github.desperado2.data.open.api.user.manage.mapper.RoleMapper;
import com.github.desperado2.data.open.api.user.manage.model.RoleRequestModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleResponseModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleSearchModel;
import com.github.desperado2.data.open.api.user.manage.service.IRoleService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务类
 * @author tu nan
 * @date 2023/2/10
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final UserInfoProvider userInfoProvider;

    private final IUserService userService;

    public RoleServiceImpl(UserInfoProvider userInfoProvider, @Lazy IUserService userService) {
        this.userInfoProvider = userInfoProvider;
        this.userService = userService;
    }

    @Override
    public void add(RoleRequestModel roleRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        Role defaultRole = getDefaultRole();
        if(defaultRole != null){
            throw new DataOpenPlatformException("默认角色已存在");
        }
        Role role = new Role();
        role.setCode(roleRequestModel.getCode());
        role.setName(roleRequestModel.getName());
        role.setStatus(RoleStatusEnum.NORMAL.getCode());
        role.setDefaultRole(roleRequestModel.getDefaultRole());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        this.save(role);
    }

    @Override
    public void update(RoleRequestModel roleRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }

        Role role = this.getBaseMapper().selectById(roleRequestModel.getId());
        if(role == null){
            throw new DataOpenPlatformException("角色不存在");
        }
        // 原本为， 非默认  改为默认
        if(role.getDefaultRole().equals(0) && roleRequestModel.getDefaultRole().equals(1)){
            Role defaultRole = getDefaultRole();
            if(defaultRole != null){
                throw new DataOpenPlatformException("默认角色已存在");
            }
        }else{
            // 原本为 默认  改为 非默认
            if(roleRequestModel.getDefaultRole().equals(0)){
                throw new DataOpenPlatformException("必须存在一个默认角色");
            }
        }
        role.setCode(roleRequestModel.getCode());
        role.setName(roleRequestModel.getName());
        role.setStatus(roleRequestModel.getStatus());
        role.setDefaultRole(roleRequestModel.getDefaultRole());
        role.setUpdateTime(new Date());
        this.updateById(role);
    }

    @Override
    public void delete(Long id) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        Role role = this.getById(id);
        if(role == null){
            throw new DataOpenPlatformException("角色不存在");
        }
        if(role.getDefaultRole().equals(1)){
            throw new DataOpenPlatformException("默认角色不能删除");
        }
        // 查询是否被使用
        Boolean isExist = userService.userExistByRole(id);
        if(isExist){
            throw new DataOpenPlatformException("存在关联用户，不能删除");
        }
        this.getBaseMapper().deleteById(id);
    }

    @Override
    public Role getDefaultRole() {
        return this.getOne(new QueryWrapper<Role>().lambda().eq(Role::getDefaultRole, 1)
                .last(" limit 1"));
    }

    @Override
    public PageResults<List<RoleResponseModel>> pageList(RoleSearchModel roleSearchModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        Integer pageSize = roleSearchModel.getPageSize();
        Integer current = roleSearchModel.getCurrent();
        LambdaQueryWrapper<Role> wapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(roleSearchModel.getName())){
            wapper.eq(Role::getName, roleSearchModel.getName());
        }
        wapper.orderByDesc(Role::getCreateTime);
        IPage<Role> pageList = this.getBaseMapper().selectPage(new Page<>(current, pageSize), wapper);
        PageResults<List<RoleResponseModel>> result = new PageResults<>();
        // 转换结果
        PageInfo pageInfo = new PageInfo(current, pageSize, pageList.getTotal());
        List<RoleResponseModel> responseModelList = pageList.getRecords().stream()
                .map(it -> JSONObject.parseObject(JSONObject.toJSONString(it), RoleResponseModel.class))
                .collect(Collectors.toList());
        result.setPagination(pageInfo);
        result.setList(responseModelList);
        return result;
    }
}
