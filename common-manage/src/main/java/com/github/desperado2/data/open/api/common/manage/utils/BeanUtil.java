package com.github.desperado2.data.open.api.common.manage.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * bean拷贝工具
 * @author tu nan
 * @date 2023/2/28
 **/
public class BeanUtil {

    static Logger log = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 实体类对象之间相同属性名值copy
     *
     * @param source 原类对象
     * @param target 目标类
     * @return 返回目标对象
     */
    public static <T> T sourceToTarget(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject);
        } catch (Exception e) {
            log.error("convert error ", e);
        }
        return targetObject;
    }

    /**
     * 实体类对象之间相同属性名值copy
     *
     * @param sourceList 原类对象
     * @param target 目标类
     * @return 返回目标对象
     */
    public static <T> List<T> sourceToTargetList(List<?> sourceList, Class<T> target) {
        if (sourceList == null || sourceList.size() == 0) {
            return null;
        }
        List<T> result = new ArrayList<>();
        try {
            for (Object source : sourceList) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject);
                result.add(targetObject);
            }
        } catch (Exception e) {
            log.error("convert error ", e);
        }
        return result;
    }
}
