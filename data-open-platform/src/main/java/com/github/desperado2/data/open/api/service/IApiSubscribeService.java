package com.github.desperado2.data.open.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.entity.ApiSubscribe;
import com.github.desperado2.data.open.api.model.*;

import java.util.List;

/**
 * 订阅接口
 *
 * @author tu nan
 * @date 2023/2/13
 **/
public interface IApiSubscribeService extends IService<ApiSubscribe> {

    /**
     * 订阅
     * @param apiSubscribeRequestModel 订阅请求
     */
    void add(ApiSubscribeRequestModel apiSubscribeRequestModel) throws DataOpenPlatformException;

    /**
     * 审批
     * @param apiSubscribeApprovalRequestModel 订阅审批信息
     */
    void subscribe(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException;

    /**
     * 取消订阅
     * @param subscribeId 订阅id
     * @return 状态
     * @throws DataOpenPlatformException 不存在
     */
    String cancelSubscribe(Long subscribeId) throws DataOpenPlatformException;

    /**
     * 禁用
     * @param apiSubscribeApprovalRequestModel 订阅审批信息
     */
    void disable(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException;

    /**
     * 审核
     * @param apiSubscribeApprovalRequestModel 订阅审批信息
     * @throws DataOpenPlatformException
     */
    void approval(ApiSubscribeApprovalRequestModel apiSubscribeApprovalRequestModel) throws DataOpenPlatformException;


    /**
     * 通过用户查询订阅列表
     * @param apiSubscribeSearchModel 订阅列表
     * @return 列表
     */
    PageResults<List<ApiSubscribeSearchResponseModel>> listByUser(ApiSubscribeSearchModel apiSubscribeSearchModel);

    /**
     * 通过用户查询订阅列表
     * @param apiSubscribeSearchAllModel 订阅列表
     * @return 列表
     */
    PageResults<List<ApiSubscribeSearchResponseModel>> listAll(ApiSubscribeSearchAllModel apiSubscribeSearchAllModel) throws DataOpenPlatformException;

    /**
     * 查询所有可用的订阅
     * @return 订阅列表
     */
    List<ApiSubscribe> getAllEnableList();

    /**
     * 正常的订阅数量
     * @return 数量
     */
    Integer normalCount();

    Integer subscribeCountByUserId(Long userId);

    Integer normalCountByUserId(Long userId);

    Integer noNormalCountByUserId(Long userId);

    Integer approvalingCountByUserId(Long userId);
}
