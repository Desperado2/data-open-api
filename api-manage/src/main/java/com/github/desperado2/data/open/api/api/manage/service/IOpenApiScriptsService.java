package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiScripts;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsRequest;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import org.springframework.http.ResponseEntity;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiScriptsService extends IService<OpenApiScripts> {

    /**
     * 新增API
     * @param openApiScripts 请求参数
     */
    Long add(OpenApiScriptsRequest openApiScripts) throws DataOpenPlatformException;

    /**
     * 更新
     * @param openApiScripts 请求参数
     * @throws DataOpenPlatformException 异常信息
     */
    void update(OpenApiScriptsRequest openApiScripts) throws DataOpenPlatformException;

    /**
     * 根据id查询
     * @param id APIid
     * @return API详情
     */
    OpenApiScriptsRequest getByApiId(Long id) throws DataOpenPlatformException;

    /**
     * 更新状态
     * @param id API id
     * @param status 状态
     */
    void updateStatus(Long id, Integer status) throws DataOpenPlatformException;

    /**
     * 执行
     * @param openApiScripts 参数
     */
    ResponseEntity execute(OpenApiScriptsRequest openApiScripts) throws Exception;

    /**
     * 冒烟测试
     * @param id 参数
     */
    ResponseEntity smokeTest(Long id) throws Exception;

    /**
     * 发布
     * @param id id
     */
    void publish(Long id) throws DataOpenPlatformException, NoSuchMethodException;


    /**
     * 下线
     * @param id apiId
     * @throws DataOpenPlatformException
     */
    void offline(Long id) throws DataOpenPlatformException, NoSuchMethodException;
}
