package com.github.desperado2.data.open.api.authentication.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.authentication.manage.entity.KeySecret;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretModel;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretQueryRequest;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretRequest;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;

import java.util.List;

/**
 * <p>
 * 防伪对外接口访问秘钥表 服务类
 * </p>
 *
 * @author jingjing.mu
 * @since 2021-01-27
 */
public interface IKeySecretService extends IService<KeySecret>{


    /**
     * 新增记录
     * @param keySecretRequest 记录
     * @throws DataOpenPlatformException 已存在异常
     */
    void add(KeySecretRequest keySecretRequest) throws DataOpenPlatformException;

    /**
     * 生成秘钥
     * @param userId 用户id
     */
    void generateKeySecret(Long userId);

    /**
     * 修改记录
     * @param keySecretRequest 记录
     * @throws DataOpenPlatformException 不存在异常
     */
    void update(KeySecretRequest keySecretRequest) throws DataOpenPlatformException;

    /**
     * 根据AppKey查询记录
     * @param appKey appKey
     * @return 返回记录值
     */
    KeySecretModel getKeySecretByAppKey(String appKey);

    /**
     * 获取所有可用的签名
     * @return 签名
     */
    List<KeySecretModel> getKeySecretAllEnable();


    /**
     * 根据AppKey查询记录
     * @param appKey appKey
     * @return 返回记录值
     */
    String getSecretByAppKey(String appKey);

    /**
     * 重置用户的key和secret
     * @return 结果
     */
    String reset(Long userId) throws DataOpenPlatformException;

    /**
     * 根据组织查询记录
     * @param userId 组织id
     * @return 返回记录值
     */
    List<KeySecretModel> getKeySecretByUserId(Long userId);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return 返回记录值
     */
    KeySecret keySecretByUserId(Long userId);

    /**
     * 根据组织查询记录
     * @return 返回记录值
     */
    List<KeySecretModel> getKeySecretByUser(Long userId) throws DataOpenPlatformException;


    /**
     * 分页查询
     * @param keySecretQueryRequest 分页参数
     * @return 结果
     */
    PageResults<List<KeySecretModel>> listAll(KeySecretQueryRequest keySecretQueryRequest);
}
