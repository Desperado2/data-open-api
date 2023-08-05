package com.github.desperado2.data.open.api.api.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiClassify;
import com.github.desperado2.data.open.api.api.manage.model.IndexApiClassifyResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListRequest;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyRequestModel;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;

import java.util.List;

/**
 * 接口
 *
 * @author tu nan
 * @date 2023/2/9
 **/
public interface IOpenApiClassifyService extends IService<OpenApiClassify> {


    /**
     * 新增分类
     * @param openApiClassifyRequestModel 请求参数
     */
    void add(OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException;

    /**
     * 更新分类
     * @param openApiClassifyRequestModel 请求参数
     */
    void update(OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException;


    /**
     * 删除
     * @param id id
     */
    void delete(Long id) throws DataOpenPlatformException;

    /**
     * 分页查询
     * @param openApiClassifyRequestModel 查询参数
     * @return 分页列表
     */
    PageResults<List<OpenApiClassifyListResult>> pageList(OpenApiClassifyListRequest openApiClassifyRequestModel) throws DataOpenPlatformException;


    /**
     * 正常的列表
     * @return 列表
     */
    List<IndexApiClassifyResult> normalList();

    /**
     * 根据id正常的列表
     * @param id 分类id
     * @return 列表
     */
    List<IndexApiClassifyResult> normalListById(Long id);


    /**
     * 根据分类名称模糊查询分类列表
     * @param name 分类名称
     * @return 分类列表
     */
    List<OpenApiClassify> getClassifyByNameLike(String name);

    /**
     * 根据id列表查询
     * @param idList id列表
     * @return 分类列表
     */
    List<OpenApiClassify> getClassifyByIdList(List<Long> idList);

}
