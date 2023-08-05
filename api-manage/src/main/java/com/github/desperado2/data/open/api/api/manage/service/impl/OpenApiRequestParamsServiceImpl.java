package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiRequestParams;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiRequestParamsMapper;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiRequestParamsRequestModel;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiRequestParamService;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiRequestParamsServiceImpl extends ServiceImpl<OpenApiRequestParamsMapper, OpenApiRequestParams> implements IOpenApiRequestParamService {

    @Override
    public void add(OpenApiRequestParamsRequestModel openApiRequestParamsRequestModel) {
        OpenApiRequestParams openApiRequestParams = request2DatabaseTransform(openApiRequestParamsRequestModel);
        this.getBaseMapper().insert(openApiRequestParams);
    }

    @Override
    public void batchAdd(List<OpenApiRequestParamsRequestModel> list) {
        List<OpenApiRequestParams> requestParamsList = list.stream().map(this::request2DatabaseTransform).collect(Collectors.toList());
        this.saveBatch(requestParamsList);
    }

    @Override
    public void batchUpdate(Long apiId, List<OpenApiRequestParamsRequestModel> openApiRequestParamsRequestModelList) {
        // 批量更新
        if(openApiRequestParamsRequestModelList == null || openApiRequestParamsRequestModelList.isEmpty()){
            return;
        }
        List<OpenApiRequestParamsRequestModel> needAdd = openApiRequestParamsRequestModelList.stream().filter(it -> it.getId() == null).collect(Collectors.toList());
        List<OpenApiRequestParamsRequestModel> needUpdate = openApiRequestParamsRequestModelList.stream().filter(it -> it.getId() != null).collect(Collectors.toList());
        List<Long> needUpdateIdList = needUpdate.stream().map(OpenApiRequestParamsRequestModel::getId).collect(Collectors.toList());
        // 查询数据库
        List<OpenApiRequestParams> apiResponseList = getApiRequestParamsByApiId(apiId);
        // 获取要删除的数据
        List<Long> needDeleteIds = apiResponseList.stream().map(OpenApiRequestParams::getId).filter(id -> !needUpdateIdList.contains(id)).collect(Collectors.toList());
        //新增
        List<OpenApiRequestParams> addList = needAdd.stream().map(it -> {
            OpenApiRequestParams openApiResponse = BeanUtil.sourceToTarget(it, OpenApiRequestParams.class);
            openApiResponse.setApiId(apiId);
            return openApiResponse;
        }).collect(Collectors.toList());
        if(addList.size() > 0){
            this.saveBatch(addList);
        }
        // 更新
        List<OpenApiRequestParams> updateList = needUpdate.stream().map(it -> BeanUtil.sourceToTarget(it, OpenApiRequestParams.class)).collect(Collectors.toList());
        if(updateList.size() > 0){
            this.updateBatchById(updateList);
        }
        // 删除
        if(needDeleteIds.size() > 0){
            this.getBaseMapper().deleteBatchIds(needDeleteIds);
        }
    }

    @Override
    public List<OpenApiRequestParams> getApiRequestParamsByApiId(Long apiId) {
        return this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiRequestParams>()
                .eq(OpenApiRequestParams::getApiId, apiId));
    }


    /**
     * 类型转换
     * @param openApiRequestParamsRequestModel 请求模型
     * @return 数据库模型
     */
    private OpenApiRequestParams request2DatabaseTransform(OpenApiRequestParamsRequestModel openApiRequestParamsRequestModel){
        OpenApiRequestParams openApiRequestParams = new OpenApiRequestParams();
        openApiRequestParams.setApiId(openApiRequestParamsRequestModel.getApiId());
        openApiRequestParams.setName(openApiRequestParamsRequestModel.getName());
        openApiRequestParams.setDescription(openApiRequestParamsRequestModel.getDescription());
        openApiRequestParams.setType(openApiRequestParamsRequestModel.getType());
        openApiRequestParams.setExampleValue(openApiRequestParamsRequestModel.getExampleValue());
        return openApiRequestParams;
    }
}
