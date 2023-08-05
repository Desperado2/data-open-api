package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApi;
import com.github.desperado2.data.open.api.api.manage.model.*;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiService extends IService<OpenApi> {

    /**
     * 新增API
     * @param openApiRequestModel 请求参数
     */
    void add(OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException;

    /**
     * 更新
     * @param openApiRequestModel 请求参数
     * @throws DataOpenPlatformException 异常信息
     */
    void update(OpenApiRequestModel openApiRequestModel) throws DataOpenPlatformException;

    /**
     * 分类是否存在API
     * @param classifyId 分类id
     * @return 存在
     */
    Boolean isExistByClassify(Long classifyId);

    /**
     * 分页查询
     * @param openApiListRequestModel 查询参数
     * @return 接口数据
     */
    PageResults<List<OpenApiListResponseModel>>  pageList(OpenApiListRequestModel openApiListRequestModel) throws DataOpenPlatformException;

    /**
     * API 列表
     * @return 列表
     */
    List<IndexApiList> indexList();

    /**
     * 正常状态的API数量
     * @return API数量
     */
    Integer normalCount();

    /**
     * 查询某个分类下面的API
     * @param classifyId 分类id
     * @return API列表
     */
    IndexApiList indexListByClassify(Long classifyId, Integer page) throws DataOpenPlatformException;

    /**
     * 根据搜索关键词搜索接口
     * @param searchWord 搜索关键词
     * @return 接口列表
     */
    List<OpenApiResponseModel> indexListBySearch(String searchWord);

    /**
     * 根据id查询
     * @param id APIid
     * @return API详情
     */
    OpenApiResponseModel get(Long id) throws DataOpenPlatformException;

    IndexOpenApiResponseModel indexDetail(Long id) throws DataOpenPlatformException;

    /**
     * 更新状态
     * @param id API id
     * @param status 状态
     */
    void updateStatus(Long id, Integer status) throws DataOpenPlatformException, NoSuchMethodException;

    /**
     * 更新开放状态
     * @param id id
     * @param openApplyStatus 开放状态
     * @throws DataOpenPlatformException
     * @throws NoSuchMethodException
     */
    void updateOpenApplyStatus(Long id, Integer openApplyStatus, String notOpenApplyReason) throws DataOpenPlatformException, NoSuchMethodException;
}
