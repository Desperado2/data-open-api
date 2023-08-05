package com.github.desperado2.data.open.api.authentication.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.desperado2.data.open.api.authentication.manage.entity.KeySecret;
import org.apache.ibatis.annotations.Mapper;


/**
 * 对外接口访问秘钥表 Mapper 接口
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
@Mapper
public interface KeySecretMapper extends BaseMapper<KeySecret> {

}
