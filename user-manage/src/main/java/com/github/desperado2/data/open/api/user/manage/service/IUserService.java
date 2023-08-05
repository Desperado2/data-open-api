package com.github.desperado2.data.open.api.user.manage.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.model.UpdateRoleModel;
import com.github.desperado2.data.open.api.user.manage.model.UserListModel;
import com.github.desperado2.data.open.api.user.manage.model.UserSearchModel;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 用户管理接口
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IUserService extends IService<User> {

    /**
     * 注册
     * @param updateRoleModel 用户信息
     */
    void add(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException;


    /**
     * 注册
     * @param user 用户信息
     */
    void saveUser(User user);


    /**
     * 通过邮件查询
     * @param email 邮件
     */
    User selectByEmail(String email);

    /**
     * 集合用户
     * @param user 用户
     */
    void activeUser(User user);


    /**
     * 分页搜索
     * @param userSearchModel 搜索条件
     * @return 返回值
     */
    PageResults<List<UserListModel>> pageList(@RequestBody UserSearchModel userSearchModel) throws DataOpenPlatformException;


    /**
     * 更新角色
     * @param updateRoleModel 更新角色
     */
    void updateRole(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException;


    /**
     * 更新用户
     * @param updateRoleModel 参数
     * @throws DataOpenPlatformException 异常
     */
    void updateUser(UpdateRoleModel updateRoleModel) throws DataOpenPlatformException;

    /**
     * 根据角色判断用户是否存在
     * @param roleId 角色id
     */
    Boolean userExistByRole(Long roleId);

    /**
     * 正常状态的用户数
     * @return 用户数
     */
    Integer normalCount();
}
