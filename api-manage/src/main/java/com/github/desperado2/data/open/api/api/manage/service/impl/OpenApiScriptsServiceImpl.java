package com.github.desperado2.data.open.api.api.manage.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.desperado2.data.open.api.api.manage.entity.OpenApiScripts;
import com.github.desperado2.data.open.api.api.manage.enums.ApiCustomResponseStructureEnum;
import com.github.desperado2.data.open.api.api.manage.enums.ApiScriptsStatusEnum;
import com.github.desperado2.data.open.api.api.manage.mapper.OpenApiScriptsMapper;
import com.github.desperado2.data.open.api.api.manage.model.OpenApiScriptsRequest;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiScriptsReleaseService;
import com.github.desperado2.data.open.api.api.manage.service.IOpenApiScriptsService;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.common.manage.enums.ApiExecuteEnvironmentEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.enums.ScriptTypeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.utils.BeanUtil;
import com.github.desperado2.data.open.api.engine.manage.enums.ExecuteType;
import com.github.desperado2.data.open.api.engine.manage.model.ApiParams;
import com.github.desperado2.data.open.api.engine.manage.result.DefaultResultWrapper;
import com.github.desperado2.data.open.api.engine.manage.scripts.ScriptExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口实现
 * @author tu nan
 * @date 2023/2/9
 **/
@Service
public class OpenApiScriptsServiceImpl extends ServiceImpl<OpenApiScriptsMapper, OpenApiScripts> implements IOpenApiScriptsService {

    private final UserInfoProvider userInfoProvider;

    private final ScriptExecutor scriptExecutor;

    private final IOpenApiScriptsReleaseService openApiScriptsReleaseService;

    public OpenApiScriptsServiceImpl(UserInfoProvider userInfoProvider,  ScriptExecutor scriptExecutor,IOpenApiScriptsReleaseService openApiScriptsReleaseService) {
        this.userInfoProvider = userInfoProvider;
        this.scriptExecutor = scriptExecutor;
        this.openApiScriptsReleaseService = openApiScriptsReleaseService;
    }


    @Override
    public Long add(OpenApiScriptsRequest openApiScriptsRequest) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        //保存数据
        OpenApiScripts openApiScripts = BeanUtil.sourceToTarget(openApiScriptsRequest, OpenApiScripts.class);
        openApiScripts.setApiRequestHeader(JSON.toJSONString(openApiScriptsRequest.getApiRequestHeader()));
        openApiScripts.setApiRequestBody(JSON.toJSONString(openApiScriptsRequest.getApiRequestBody()));
        openApiScripts.setApiResponseHeader(JSON.toJSONString(openApiScriptsRequest.getApiResponseHeader()));
        openApiScripts.setApiResponseBody(JSON.toJSONString(openApiScriptsRequest.getApiResponseBody()));
        openApiScripts.setApiResponseFormat(JSON.toJSONString(openApiScriptsRequest.getApiResponseFormat()));
        openApiScripts.setApiOption(JSON.toJSONString(openApiScriptsRequest.getApiOption()));
        openApiScripts.setApiScriptStatus(ApiScriptsStatusEnum.SAVE.getCode());
        openApiScripts.setCreateTime(new Date());
        openApiScripts.setUpdateTime(new Date());
        this.getBaseMapper().insert(openApiScripts);
        return openApiScripts.getId();
    }

    @Override
    public void update(OpenApiScriptsRequest openApiScriptsRequest) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        // 修改
        OpenApiScripts openApiScripts = BeanUtil.sourceToTarget(openApiScriptsRequest, OpenApiScripts.class);
        openApiScripts.setApiRequestHeader(JSON.toJSONString(openApiScriptsRequest.getApiRequestHeader()));
        openApiScripts.setApiRequestBody(JSON.toJSONString(openApiScriptsRequest.getApiRequestBody()));
        openApiScripts.setApiResponseHeader(JSON.toJSONString(openApiScriptsRequest.getApiResponseHeader()));
        openApiScripts.setApiResponseBody(JSON.toJSONString(openApiScriptsRequest.getApiResponseBody()));
        openApiScripts.setApiResponseFormat(JSON.toJSONString(openApiScriptsRequest.getApiResponseFormat()));
        openApiScripts.setApiOption(JSON.toJSONString(openApiScriptsRequest.getApiOption()));
        openApiScripts.setUpdateTime(new Date());
        this.getBaseMapper().updateById(openApiScripts);
    }

    @Override
    public OpenApiScriptsRequest getByApiId(Long id) throws DataOpenPlatformException {
        OpenApiScripts openApiScripts = this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiScripts>()
                .eq(OpenApiScripts::getApiId, id).last(" limit 1"));
        OpenApiScriptsRequest openApiScriptsRequest = null;
        if(openApiScripts != null){
            openApiScriptsRequest = BeanUtil.sourceToTarget(openApiScripts, OpenApiScriptsRequest.class);
            openApiScriptsRequest.setApiRequestHeader(JSONArray.parseObject(openApiScripts.getApiRequestHeader(), List.class));
            openApiScriptsRequest.setApiRequestBody(JSON.parseObject(openApiScripts.getApiRequestBody()));
            openApiScriptsRequest.setApiResponseHeader(JSONArray.parseObject(openApiScripts.getApiResponseHeader(), List.class));
            openApiScriptsRequest.setApiResponseBody(JSON.parseObject(openApiScripts.getApiResponseBody()));
            openApiScriptsRequest.setApiResponseFormat(JSON.parseObject(openApiScripts.getApiResponseFormat()));
            openApiScriptsRequest.setApiOption(JSON.parseObject(openApiScripts.getApiOption()));
        }
        return openApiScriptsRequest;
    }

    @Override
    public void updateStatus(Long id, Integer status) throws DataOpenPlatformException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApiScripts openApiScripts = new OpenApiScripts();
        openApiScripts.setId(id);
        openApiScripts.setApiScriptStatus(status);
        this.getBaseMapper().updateById(openApiScripts);
    }

    @Override
    @Transactional
    public ResponseEntity execute(OpenApiScriptsRequest openApiScripts) throws Exception {
        Integer customResultStructure = -1;
        try {

            BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
            String roleCode = loginUserInfo.getRoleCode();
            if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
                throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
            }
            Map<String, Object> apiRequestBody = openApiScripts.getApiRequestBody();
            customResultStructure = openApiScripts.getCustomResultStructure();
            String apiScript = openApiScripts.getApiScript();
            Object mapList = null;
            ApiInfo apiInfo = ApiInfo.Builder.builder().script(apiScript)
                    .testDatasource(openApiScripts.getTestDatasourceCode())
                    .preDatasource(openApiScripts.getPreDatasourceCode())
                    .prodDatasource(openApiScripts.getProdDatasourceCode())
                    .build();
            String executeEnv =openApiScripts.getApiRunEnvironment();
            ApiExecuteEnvironmentEnum environment = getEnvironmentByPath(executeEnv);
            ApiParams apiParams = ApiParams.builder().param(apiRequestBody).build();
            if(ScriptTypeEnum.GROOVY.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.GROOVY).runScript(ExecuteType.SYS ,environment, apiScript, apiInfo, apiParams);
            }else if(ScriptTypeEnum.SQL.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.SQL).runScript(ExecuteType.SYS ,environment, apiScript, apiInfo, apiParams);
            }else if(ScriptTypeEnum.JAVASCRIPT.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.JAVASCRIPT).runScript(ExecuteType.SYS ,environment, apiScript, apiInfo, apiParams);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DefaultResultWrapper().throwable(new Throwable("无效的执行引擎"), null,null, null));
            }

            if(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure)){
                JSONObject apiResponseFormat = openApiScripts.getApiResponseFormat();
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DefaultResultWrapper().wrapper(mapList, null,null, apiResponseFormat));
            }
            return  ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().wrapper(mapList, null,null, null));

        }catch (Exception e){
            if(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure)){
                JSONObject apiResponseFormat = openApiScripts.getApiResponseFormat();
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DefaultResultWrapper().throwable(e, null,null, apiResponseFormat));
            }
            e.printStackTrace();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().throwable(e, null,null, null));
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().throwable(e, null,null, null));
        }
    }

    @Override
    @Transactional
    public ResponseEntity smokeTest(Long id) throws Exception {
        Integer customResultStructure = -1;
        JSONObject apiResponseFormat = null;
        try {
            BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
            String roleCode = loginUserInfo.getRoleCode();
            if (!RoleCodeEnum.ADMIN.getCode().equals(roleCode)) {
                throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
            }
            OpenApiScripts openApiScripts = this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiScripts>()
                    .eq(OpenApiScripts::getApiId, id).last(" limit 1"));
            apiResponseFormat = JSONObject.parseObject(openApiScripts.getApiResponseFormat());
            JSONObject jsonObject = JSONObject.parseObject(openApiScripts.getApiRequestBody());
            customResultStructure = openApiScripts.getCustomResultStructure();
            // 根据id查询
            String apiScript = openApiScripts.getApiScript();
            Object mapList = null;
            ApiInfo apiInfo = ApiInfo.Builder.builder().script(apiScript)
                    .testDatasource(openApiScripts.getTestDatasourceCode())
                    .preDatasource(openApiScripts.getPreDatasourceCode())
                    .prodDatasource(openApiScripts.getProdDatasourceCode())
                    .build();
            String executeEnv =openApiScripts.getApiRunEnvironment();
            ApiExecuteEnvironmentEnum environment = getEnvironmentByPath(executeEnv);
            ApiParams apiParams = ApiParams.builder().param(jsonObject).build();
            if(ScriptTypeEnum.GROOVY.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.GROOVY).runScript(ExecuteType.SYS ,environment,apiScript, apiInfo, apiParams);
            }else if(ScriptTypeEnum.SQL.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.SQL).runScript(ExecuteType.SYS ,environment, apiScript, apiInfo, apiParams);
            }else if(ScriptTypeEnum.JAVASCRIPT.getCode().equals(openApiScripts.getApiScriptType())){
                mapList = scriptExecutor.getScriptExecutor(ScriptTypeEnum.JAVASCRIPT).runScript(ExecuteType.SYS ,environment, apiScript, apiInfo, apiParams);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DefaultResultWrapper().throwable(new Throwable("无效的执行引擎"), null,null, null));
            }

            if(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure)){
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DefaultResultWrapper().wrapper(mapList, null,null, apiResponseFormat));
            }
            return  ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().wrapper(mapList, null,null, null));
    }catch (Exception e){
        if(ApiCustomResponseStructureEnum.OPEN.getCode().equals(customResultStructure)){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().throwable(e, null,null, apiResponseFormat));
        }
        e.printStackTrace();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new DefaultResultWrapper().throwable(e, null,null, null));
    } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new DefaultResultWrapper().throwable(new Throwable("无效的执行器"), null,null, null));
        }
    }

    @Override
    @Transactional
    public void publish(Long id) throws DataOpenPlatformException, NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApiScripts openApiScripts = this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiScripts>()
                .eq(OpenApiScripts::getApiId, id).last(" limit 1"));
        // 添加发布日志
        openApiScriptsReleaseService.add(openApiScripts);
        // 更新状态
        openApiScripts.setApiScriptStatus(ApiScriptsStatusEnum.PUBLISH.getCode());
        this.getBaseMapper().updateById(openApiScripts);
    }

    @Override
    @Transactional
    public void offline(Long id) throws DataOpenPlatformException, NoSuchMethodException {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        String roleCode = loginUserInfo.getRoleCode();
        if(!RoleCodeEnum.ADMIN.getCode().equals(roleCode)){
            throw new DataOpenPlatformException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorMessage(), ErrorCodeEnum.ACCESS_FORBIDDEN.getHttpCode());
        }
        OpenApiScripts openApiScripts = this.getBaseMapper().selectOne(new LambdaQueryWrapper<OpenApiScripts>()
                .eq(OpenApiScripts::getApiId, id).last(" limit 1"));
        // 下线
        openApiScriptsReleaseService.offline(openApiScripts);
        // 更新状态
        openApiScripts.setApiScriptStatus(ApiScriptsStatusEnum.OFFLINE.getCode());
        this.getBaseMapper().updateById(openApiScripts);
    }

    /**
     * 获取执行环境
     * @param executeEnv 执行环境
     * @return 返回值
     * @throws DataOpenPlatformException 异常
     */
    private ApiExecuteEnvironmentEnum getEnvironmentByPath(String executeEnv) throws DataOpenPlatformException {
        if(StringUtils.isBlank(executeEnv)){
            throw new DataOpenPlatformException("未知的环境，executeEnv=" + executeEnv);
        }
        if(executeEnv.toLowerCase().equals(ApiExecuteEnvironmentEnum.TEST.getCode())){
            return ApiExecuteEnvironmentEnum.TEST;
        }
        if(executeEnv.toLowerCase().equals(ApiExecuteEnvironmentEnum.PRE.getCode())){
            return ApiExecuteEnvironmentEnum.PRE;
        }
        if(executeEnv.toLowerCase().equals(ApiExecuteEnvironmentEnum.PROD.getCode())){
            return ApiExecuteEnvironmentEnum.PROD;
        }
        throw new DataOpenPlatformException("未知的环境，executeEnv=" + executeEnv);
    }
}
