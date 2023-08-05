package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiClassify;
import com.github.desperado2.data.open.api.api.manage.enums.ApiClassifyStatusEnum;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiClassifyMapper;
import com.github.desperado2.data.open.api.api.manage.model.IndexApiClassifyResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListRequest;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyListResult;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiClassifyRequestModel;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiClassifyService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiService;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.model.PageInfo;
import com.github.desperado2.data.open.api.common.manage.model.PageResults;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiClassifyServiceImpl extends ServiceImpl<OpenApiClassifyMapper, OpenApiClassify> implements IOpenApiClassifyService {

    private final UserInfoProvider userInfoProvider;

    private final IOpenApiService openApiService;

    public OpenApiClassifyServiceImpl(UserInfoProvider userInfoProvider, IOpenApiService openApiService) {
        this.userInfoProvider = userInfoProvider;
        this.openApiService = openApiService;
    }

    @Override
    public void add(OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApiClassify openApiClassify = new OpenApiClassify();
        openApiClassify.setCode(openApiClassifyRequestModel.getCode());
        openApiClassify.setName(openApiClassifyRequestModel.getName());
        openApiClassify.setStatus(openApiClassifyRequestModel.getStatus());
        openApiClassify.setDescription(openApiClassifyRequestModel.getDescription());
        openApiClassify.setShowIndex(openApiClassifyRequestModel.getShowIndex());
        openApiClassify.setCreateTime(new Date());
        openApiClassify.setUpdateTime(new Date());
        this.getBaseMapper().insert(openApiClassify);
    }

    @Override
    public void update(OpenApiClassifyRequestModel openApiClassifyRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 查询是否存在
        OpenApiClassify openApiClassify = this.getById(openApiClassifyRequestModel.getId());
        if(openApiClassify == null){
            throw new DataOpenPlatformException("分类不存在");
        }
        openApiClassify.setCode(openApiClassifyRequestModel.getCode());
        openApiClassify.setName(openApiClassifyRequestModel.getName());
        openApiClassify.setDescription(openApiClassifyRequestModel.getDescription());
        openApiClassify.setShowIndex(openApiClassifyRequestModel.getShowIndex());
        openApiClassify.setStatus(openApiClassifyRequestModel.getStatus());
        openApiClassify.setUpdateTime(new Date());
        this.getBaseMapper().updateById(openApiClassify);
    }

    @Override
    public void delete(Long id) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 查询是否存在
        OpenApiClassify openApiClassify = this.getById(id);
        if(openApiClassify == null){
            throw new DataOpenPlatformException("分类不存在");
        }
        // 查询是否有API关联
        if(openApiService.isExistByClassify(id)){
            throw new DataOpenPlatformException("存在关联API,不能删除");
        }
        // 删除
        this.getBaseMapper().deleteById(id);
    }

    @Override
    public PageResults<List<OpenApiClassifyListResult>> pageList(OpenApiClassifyListRequest openApiClassifyRequestModel) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        LambdaQueryWrapper<OpenApiClassify> wrapper = new LambdaQueryWrapper<>();
        String code = openApiClassifyRequestModel.getCode();
        if(StringUtils.isNotBlank(code)){
            wrapper.like(OpenApiClassify::getCode, code);
        }
        String name = openApiClassifyRequestModel.getName();
        if(StringUtils.isNotBlank(name)){
            wrapper.like(OpenApiClassify::getName, name);
        }
        Integer status = openApiClassifyRequestModel.getStatus();
        if(status != null){
            wrapper.eq(OpenApiClassify::getStatus, status);
        }
        wrapper.orderByDesc(OpenApiClassify::getCreateTime);
        IPage<OpenApiClassify> page = new Page<>(openApiClassifyRequestModel.getCurrent(), openApiClassifyRequestModel.getPageSize());
        IPage<OpenApiClassify> openApiClassifyIPage = this.getBaseMapper().selectPage(page, wrapper);
        PageResults<List<OpenApiClassifyListResult>> results = new PageResults<>();
        results.setPagination(new PageInfo(openApiClassifyIPage.getCurrent(),openApiClassifyIPage.getSize(), openApiClassifyIPage.getTotal()));
        results.setList(openApiClassifyIPage.getRecords().stream().map(it -> BeanUtil.sourceToTarget(it, OpenApiClassifyListResult.class)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public List<IndexApiClassifyResult> normalList() {
        List<OpenApiClassify> openApiClassifies = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiClassify>()
                .eq(OpenApiClassify::getStatus, ApiClassifyStatusEnum.ENABLE.getCode()));
        return BeanUtil.sourceToTargetList(openApiClassifies, IndexApiClassifyResult.class);
    }

    @Override
    public List<IndexApiClassifyResult> normalListById(Long id) {
        List<OpenApiClassify> openApiClassifies = this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiClassify>()
                .eq(OpenApiClassify::getId, id)
                .eq(OpenApiClassify::getStatus, ApiClassifyStatusEnum.ENABLE.getCode()));
        return BeanUtil.sourceToTargetList(openApiClassifies, IndexApiClassifyResult.class);
    }

    @Override
    public List<OpenApiClassify> getClassifyByNameLike(String name) {
        if(StringUtils.isNotBlank(name)){
            return this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiClassify>().like(OpenApiClassify::getName, name));
        }
        return new ArrayList<>();
    }

    @Override
    public List<OpenApiClassify> getClassifyByIdList(List<Long> idList) {
        if(idList.size() > 0){
            idList = idList.stream().distinct().collect(Collectors.toList());
            return this.getBaseMapper().selectList(new LambdaQueryWrapper<OpenApiClassify>().in(OpenApiClassify::getId, idList));
        }
        return new ArrayList<>();
    }
}
