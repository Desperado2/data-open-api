package com.github.desperado2.data.open.api.user.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.user.manage.entity.Role;
import com.github.desperado2.data.open.api.user.manage.model.RoleRequestModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleResponseModel;
import com.github.desperado2.data.open.api.user.manage.model.RoleSearchModel;

import java.util.List;

/**
 * 角色操作接口
 *
 * @author tu nan
 * @date 2023/2/10
 **/
public interface IRoleService extends IService<Role> {

    /**
     * 新增角色
     * @param roleRequestModel 请求数据模型
     */
    void add(RoleRequestModel roleRequestModel) throws DataOpenPlatformException;

    /**
     * 更新角色
     * @param roleRequestModel 请求数据模型
     */
    void update(RoleRequestModel roleRequestModel) throws DataOpenPlatformException;


    /**
     * 删除
     * @param id id
     */
    void delete(Long id) throws DataOpenPlatformException;

    /**
     * 获取默认角色
     * @return 默认角色
     */
    Role getDefaultRole();

    /**
     * 分页查询
     * @param roleSearchModel 模型
     * @return 数据
     */
    PageResults<List<RoleResponseModel>> pageList(RoleSearchModel roleSearchModel) throws DataOpenPlatformException;
}
