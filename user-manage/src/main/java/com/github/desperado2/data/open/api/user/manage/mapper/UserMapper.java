package com.github.desperado2.data.open.api.user.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
