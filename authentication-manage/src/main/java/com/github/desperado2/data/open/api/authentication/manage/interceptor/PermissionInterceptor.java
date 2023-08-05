package com.github.desperado2.data.open.api.authentication.manage.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.desperado2.data.open.api.authentication.manage.enums.KeySecretFieldEnum;
import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretModel;
import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiInfoCache;
import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.config.RequestWrapper;
import com.github.desperado2.data.open.api.common.manage.enums.ExternalResultCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.ExternalException;
import com.github.desperado2.data.open.api.common.manage.model.BaseKeySecretModel;
import com.github.desperado2.data.open.api.common.manage.model.ExternalResult;
import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.DateUtils;
import com.github.desperado2.data.open.api.common.manage.utils.Md5Encrypt;
import com.github.desperado2.data.open.api.common.manage.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对外接口权限校验拦截器
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerInterceptorAdapter.class);

    private final IKeySecretService keySecretService;

    private final IKeySecretCache keySecretCache;

    private final IApiApplyInfoCache apiApplyInfoCache;

    private final IApiInfoCache apiInfoCache;

    private final OpenApiProperties openApiProperties;

    /**
     * 签名超时时长，默认时间为2分钟，ms
     */
    private static final int SIGN_EXPIRED_TIME = 2 * 60 * 1000;

    /**
     * 时间戳格式
     */
    private static final String SIGN_TIME_LENGTH = "yyyyMMddHHmmss";

    public PermissionInterceptor(IKeySecretService keySecretService, IKeySecretCache keySecretCache, IApiApplyInfoCache apiApplyInfoCache, IApiInfoCache apiInfoCache, OpenApiProperties openApiProperties) {
        this.keySecretService = keySecretService;
        this.keySecretCache = keySecretCache;
        this.apiApplyInfoCache = apiApplyInfoCache;
        this.apiInfoCache = apiInfoCache;
        this.openApiProperties = openApiProperties;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pattern = RequestUtils.buildPattern(request);
        String method = request.getMethod();
        try {
            RequestWrapper requestWrapper = new RequestWrapper(request);
            // 校验权限
            log.info("调用地址：{}", pattern);
            if(pattern.startsWith(openApiProperties.getBaseRegisterPath())){
                pattern = pattern.replaceFirst(openApiProperties.getBaseRegisterPath(), "");
            }
            ApiInfo apiInfo = apiInfoCache.get(ApiInfo.Builder.builder().path(pattern).method(method).build());
            if(apiInfo == null){
                log.error("API不存在，地址为{}:",pattern);
                throw new ExternalException(ExternalResultCodeEnum.API_NOT_EXISTS.getCode(), ExternalResultCodeEnum.API_NOT_EXISTS.getName(), pattern, method);

            }
            String body = null;
            if(RequestMethod.GET.name().equals(request.getMethod())){
                JSONObject object = new JSONObject();
                for (Map.Entry<String, String[]> stringEntry : requestWrapper.getParameterMap().entrySet()) {
                    object.put(stringEntry.getKey(), stringEntry.getValue()[0]);
                }
                body = object.toJSONString();
            }else if(RequestMethod.POST.name().equals(request.getMethod())){
                body = requestWrapper.getBody();
            }
            boolean verifySign = verifySign(body, apiInfo, response);
            if(verifySign){
                return  verificationSign(body,response);
            }
            return false;
        }catch (Exception e){
            log.error("外部接口鉴权异常:",e);
            if(e instanceof ExternalException){
                throw e;
            } else {
                throw new ExternalException(ExternalResultCodeEnum.SIGN_ERROR.getCode(), ExternalResultCodeEnum.SIGN_ERROR.getName(), pattern, method);
            }
        }
    }


    /**
     * 验证签名
     * @param body 请求参数
     * @return 返回值
     */
    private boolean verifySign(String body, ApiInfo apiInfo, HttpServletResponse response) {
        Assert.notNull(body,"请求参数不能为空");
        // 获取接口签名
        BaseKeySecretModel baseKeySecretParam = JSONObject.parseObject(body, BaseKeySecretModel.class);
        String appKey = baseKeySecretParam.getAppKey();
        String signTime = baseKeySecretParam.getSignTime();
        String sign = baseKeySecretParam.getSign();

        // 校验是否申请
        log.info("appKey:{}", appKey);
        KeySecretInfo keySecretInfo = keySecretCache.get(KeySecretInfo.Builder.builder().appKey(appKey).build());
        if(keySecretInfo == null){
            log.error("秘钥找不到");
            for (KeySecretInfo secretInfo : keySecretCache.getAll()) {
                log.info("appkeya:{}", secretInfo.getAppKey());
            }
            returnMsg(ExternalResultCodeEnum.FORBID,response);
            return false;
        }
        ApiApplyInfo apiApplyInfo = apiApplyInfoCache.get(ApiApplyInfo.Builder.builder().apiId(apiInfo.getId())
                .userId(keySecretInfo.getUserId()).build());
        if(apiApplyInfo == null){
            log.error("订阅信息找不到");
            returnMsg(ExternalResultCodeEnum.FORBID,response);
            return false;
        }
        // 校验时间
        if (StringUtils.isBlank(signTime) || !StringUtils.isNumeric(signTime) || signTime.length() != SIGN_TIME_LENGTH.length()) {
            returnMsg(ExternalResultCodeEnum.TIMESTAMP_NOT_LICIT,response);
            return false;
        }

        Date date = DateUtils.dateFromString(signTime, SIGN_TIME_LENGTH);
        if(date == null){
            returnMsg(ExternalResultCodeEnum.TIMESTAMP_NOT_LICIT,response);
            return false;
        }
        long ts = date.getTime();
        // 禁止超时签名
        if (System.currentTimeMillis() - ts > SIGN_EXPIRED_TIME) {
            returnMsg(ExternalResultCodeEnum.SIGN_EXPIRE,response);
            return false;
        }

        // appKey为空
        if(StringUtils.isBlank(appKey)){
            returnMsg(ExternalResultCodeEnum.APPKEY_ERROR,response);
            return false;
        }

        // 签名为空
        if(StringUtils.isBlank(sign)){
            returnMsg(ExternalResultCodeEnum.SIGN_ERROR,response);
            return false;
        }
        return true;
    }


    /**
     * 计算签名
     * @param body 参数
     * @return 返回值
     */
    private boolean verificationSign(String body,HttpServletResponse response) {
        Map<String,Object> map = JSONObject.parseObject(body, new TypeReference<Map<String,Object>>(){});
        // 获取接口签名
        BaseKeySecretModel baseKeySecretParam = JSONObject.parseObject(body, BaseKeySecretModel.class);
        String appKey = baseKeySecretParam.getAppKey();
        String sign = baseKeySecretParam.getSign();

        // 查询appKey是否存在
        KeySecretInfo keySecretInfo = keySecretCache.get(KeySecretInfo.Builder.builder().appKey(appKey).build());
        if(keySecretInfo == null){
            KeySecretModel keySecret = keySecretService.getKeySecretByAppKey(appKey);
            if(keySecret != null){
                keySecretInfo = KeySecretInfo.Builder.builder()
                        .id(keySecret.getId())
                        .appKey(keySecret.getAppKey())
                        .appSecret(keySecret.getAppSecret())
                        .userId(keySecret.getUserId()).build();
                keySecretCache.put(keySecretInfo);
            }
        }
        if(keySecretInfo == null){
            returnMsg(ExternalResultCodeEnum.APPKEY_ERROR,response);
            return false;
        }

        // map 剔除sign参数
        map.remove(KeySecretFieldEnum.SIGN.getCode());

        // 计算签名
        Map<String, Object> result = new LinkedHashMap<>();
        // 按照key的字典序升序排列
        map.entrySet().stream().filter(it -> it.getValue() != null)
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        String param = StringUtils.join(Arrays.asList(result.entrySet().stream().map(it -> it.getKey() + "=" + String.valueOf(it.getValue())).toArray()),"&");
        // 计算签名
        String sb = param + AESUtils.decrypt(keySecretInfo.getAppSecret(), null);
        String signResult = Md5Encrypt.md5Hexdigest(sb, "utf-8").toLowerCase();
        if(sign.equals(signResult)){
            return true;
        }
        // 签名不正确
        returnMsg(ExternalResultCodeEnum.SIGN_ERROR,response);
        return false;
    }


    /**
     * 返回异常信息
     * @param externalResultCodeEnum 异常信息
     * @param response 响应
     */
    public void returnMsg(ExternalResultCodeEnum externalResultCodeEnum,HttpServletResponse response){
        ExternalResult<String> externalResult = new ExternalResult<>(externalResultCodeEnum.getCode(),externalResultCodeEnum.getName(),null);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        String jsonObject = JSONObject.toJSONString(externalResult);
        try {
            out = response.getWriter();
            out.append(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
