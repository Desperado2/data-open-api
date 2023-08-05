package com.github.desperado2.data.open.api.engine.manage.datamasking;

/**
 * 数据脱敏接口
 *
 * @author tu nan
 * @date 2023/7/6
 **/
public interface IDataMaskingService {

    Object mask(Object originalData, Object... args);
}
