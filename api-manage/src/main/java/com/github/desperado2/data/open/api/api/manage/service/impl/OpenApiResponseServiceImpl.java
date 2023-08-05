package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiResponse;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiResponseMapper;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiResponseRequestModel;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiResponseService;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiResponseServiceImpl extends ServiceImpl<OpenApiResponseMapper, OpenApiResponse> implements IOpenApiResponseService {

    @Override
    public void add(OpenApiResponseRequestModel openApiResponseRequestModel) {
        OpenApiResponse openApiResponse = request2DatabaseTransform(openApiResponseRequestModel);
        this.getBaseMapper().insert(openApiResponse);
    }

    @Override
    public void batchAdd(List<OpenApiResponseRequestModel> openApiResponseRequestModelList) {
        List<OpenApiResponse> responseList = openApiResponseRequestModelList.stream().map(this::request2DatabaseTransform).collect(Collectors.toList());
        this.saveBatch(responseList);
    }

    @Override
    @Transactional
    public void batchUpdate(Long apiId, List<OpenApiResponseRequestModel> openApiResponseRequestModelList) {
        // 批量更新
        if(openApiResponseRequestModelList == null || openApiResponseRequestModelList.isEmpty()){
            return;
        }
        List<OpenApiResponseRequestModel> needAdd = openApiResponseRequestModelList.stream().filter(it -> it.getId() == null).collect(Collectors.toList());
        List<OpenApiResponseRequestModel> needUpdate = openApiResponseRequestModelList.stream().filter(it -> it.getId() != null).collect(Collectors.toList());
        List<Long> needUpdateIdList = needUpdate.stream().map(OpenApiResponseRequestModel::getId).collect(Collectors.toList());
        // 查询数据库
        List<OpenApiResponse> apiResponseList = getApiResponseByApiId(apiId);
        // 获取要删除的数据
        List<Long> needDeleteIds = apiResponseList.stream().map(OpenApiResponse::getId).filter(id -> !needUpdateIdList.contains(id)).collect(Collectors.toList());
        //新增
        List<OpenApiResponse> addList = needAdd.stream().map(it -> {
            OpenApiResponse openApiResponse = BeanUtil.sourceToTarget(it, OpenApiResponse.class);
            openApiResponse.setApiId(apiId);
            return openApiResponse;
        }).collect(Collectors.toList());
        if(addList.size() > 0){
            this.saveBatch(addList);
        }

        // 更新
        List<OpenApiResponse> updateList = needUpdate.stream().map(it -> BeanUtil.sourceToTarget(it, OpenApiResponse.class)).collect(Collectors.toList());
        if(updateList.size() > 0){
            this.updateBatchById(updateList);
        }
        // 删除
        if(needDeleteIds.size() > 0){
            this.getBaseMapper().deleteBatchIds(needDeleteIds);
        }
    }

    @Override
    public List<OpenApiResponse> getApiResponseByApiId(Long apiId) {
        return this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiResponse>()
                .eq(OpenApiResponse::getApiId, apiId));
    }


    /**
     * 请求模型转为数据库模型
     * @param openApiResponseRequestModel 请求模型
     * @return 数据库模型
     */
    private OpenApiResponse request2DatabaseTransform(OpenApiResponseRequestModel openApiResponseRequestModel){
        OpenApiResponse openApiResponse = new OpenApiResponse();
        openApiResponse.setApiId(openApiResponseRequestModel.getApiId());
        openApiResponse.setName(openApiResponseRequestModel.getName());
        openApiResponse.setDescription(openApiResponseRequestModel.getDescription());
        openApiResponse.setType(openApiResponseRequestModel.getType());
        openApiResponse.setExampleValue(openApiResponseRequestModel.getExampleValue());
        return openApiResponse;
    }
}
