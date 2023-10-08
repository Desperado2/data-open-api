package com.github.desperado2.data.open.api.udf.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.desperado2.data.open.api.udf.manage.entity.UdfDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据库操作
 *
 * @author tu nan
 * @date 2023/5/22
 **/
@Mapper
public interface UdfDefinitionMapper extends BaseMapper<UdfDefinition> {

    @Select("select id, name, type from t_udf_definition where status = 0")
    List<UdfDefinition> getTypeList();
}
