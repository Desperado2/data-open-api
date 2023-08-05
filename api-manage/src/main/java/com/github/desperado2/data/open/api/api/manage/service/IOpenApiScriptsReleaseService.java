package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiScripts;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiScriptsRelease;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsReleaseList;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsRequest;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiScriptsReleaseService extends IService<OpenApiScriptsRelease> {

    /**
     * 新增API
     * @param openApiScripts 请求参数
     */
    void add(OpenApiScripts openApiScripts) throws DataOpenPlatformException, NoSuchMethodException;

    /**
     * 下架
     * @param openApiScripts 请求参数
     */
    void offline(OpenApiScripts openApiScripts) throws DataOpenPlatformException, NoSuchMethodException;

    /**
     * 获取历史记录列表
     * @param apiId id
     */
    List<OpenApiScriptsReleaseList> apiHistory(Long apiId) throws DataOpenPlatformException;

    /**
     * 获取记录详情
     * @param id 记录id
     */
    OpenApiScriptsRequest apiHistoryInfo(Long id) throws DataOpenPlatformException;
}
