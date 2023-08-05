package com.github.desperado2.data.open.api.user.manage.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.cache.manage.chche.IGeneralCache;
import com.github.desperado2.data.open.api.cache.manage.chche.IUserTokenCache;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.constants.RedisKey;
import com.github.desperado2.data.open.api.common.manage.enums.ErrorCodeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.UUIDUtils;
import com.github.desperado2.data.open.api.user.manage.entity.Role;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.enums.UserStatusEnum;
import com.github.desperado2.data.open.api.user.manage.model.CaptchaModel;
import com.github.desperado2.data.open.api.user.manage.model.UserLoginModel;
import com.github.desperado2.data.open.api.user.manage.service.ICaptchaService;
import com.github.desperado2.data.open.api.user.manage.service.ILoginService;
import com.github.desperado2.data.open.api.user.manage.service.IRoleService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录
 * @author tu nan
 * @date 2023/2/10
 **/
@Service
public class LoginServiceImpl implements ILoginService {

    /** 允许登录失败次数后跳出验证码 */
    public static final int LOGIN_FAIL_TOTAL = 5;

    private final static Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private ICaptchaService captchaService;

    @Autowired
    private IUserTokenCache userTokenCache;

    @Autowired
    private IGeneralCache generalCache;

    @Autowired
    private UserInfoProvider userInfoProvider;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Value("${open.data.platform.cookie.timeout:7200}")
    private Long cookieTimeout;


    @Override
    public String login(UserLoginModel userLoginModel) throws DataOpenPlatformException {
        String username = userLoginModel.getEmail();
        String password = userLoginModel.getPassword();
        String codeToken = userLoginModel.getCodeToken();
        String codeResult = userLoginModel.getCodeResult();
        String identifyingCodeRedisKey = null;
        if(StringUtils.isBlank(codeResult)){
            // 验证是否超过登录的最大失败次数
            isLoginHasCode(username);
        }else{
            // 校验验证码是否正确
            identifyingCodeRedisKey = checkIdentifyingCode(codeToken,codeResult);
        }
        // 查询账号信息是否存在
        User baseUserVO = userService.selectByEmail(username);
        if(baseUserVO != null && baseUserVO.getPassword() != null && baseUserVO.getPassword().equals(AESUtils.encrypt(password, null))){
            // 判断是否禁用
            if(UserStatusEnum.DEACTIVATE.getCode().equals(baseUserVO.getStatus())){
                throw new DataOpenPlatformException("该用户已被禁用");
            }
            //是否未激活
            if(UserStatusEnum.NOT_ACTIVATED.getCode().equals(baseUserVO.getStatus())){
                throw new DataOpenPlatformException("该用户未激活");
            }
            // 所选角色是否存在
            Role role = roleService.getById(baseUserVO.getRoleId());
            if(role == null){
                throw new DataOpenPlatformException("账号不存在，请仔细核对！");
            }
            baseUserVO.setPassword(null);
            // 生成token
            String token = UUIDUtils.getUUID();
            // 放入redis
            BaseUserModel baseUserModel = JSONObject.parseObject(JSON.toJSONString(baseUserVO), BaseUserModel.class);
            baseUserModel.setRoleCode(role.getCode());
            userTokenCache.set(token, baseUserModel, cookieTimeout);
            // 写入cookie
            userInfoProvider.addCookie("token", token, "/", cookieTimeout.intValue());

            // 消除错误统计次数
            String redisKey = RedisKey.USER_LOGIN_ERROR_COUNT_CODE + username;
            if(generalCache.exists(redisKey)){
                generalCache.remove(redisKey);
            }
            // 消除验证码
            if(identifyingCodeRedisKey != null){
                generalCache.remove(identifyingCodeRedisKey);
            }
            return token;
        }else {
            // 登录失败，记录错误次数
            String redisKey = RedisKey.USER_LOGIN_ERROR_COUNT_CODE + username;
            if(generalCache.exists(redisKey)){
                int errorCount = Integer.parseInt(generalCache.get(redisKey));
                errorCount += 1;
                generalCache.set(redisKey, String.valueOf(errorCount), 12 * 60 * 60L);
            }else{
                generalCache.set(redisKey, "1", 12 * 60 * 60L);
            }
            if(baseUserVO == null){
                throw new DataOpenPlatformException("账号不正确!");
            }else{
                throw new DataOpenPlatformException("密码不正确!");
            }
        }
    }

    @Override
    public String logout(WebRequest webRequest) {
        String token = (String) webRequest.getAttribute("super-token", RequestAttributes.SCOPE_REQUEST);
        userTokenCache.remove(token);
        // 删除cookies
        userInfoProvider.deleteCookies();
        return "退出登录成功";
    }

    @Override
    public String getVerificationCodeToken() {
        return UUIDUtils.getUUID();
    }

    @Override
    public String codeVerificationCode(HttpServletResponse servletResponse, String codeToken) {
        CaptchaModel captchaModel = captchaService.generateSimple();
        try {
            ImageIO.write(captchaModel.getBufferedImage(), "JPG", servletResponse.getOutputStream());
        }catch (IOException e){
            log.error("验证码生成错误",e);
        }
        saveIdentifyingCode(codeToken, captchaModel.getResult().toLowerCase(), 600L);
        return "生成成功";
    }

    @Override
    public BaseUserModel getUser() {
        return userInfoProvider.getLoginUserInfo();
    }

    /**
     * 保存验证码
     * @param codeToken 验证码token
     * @param codeResult 验证码
     * @param expire 过期时间
     */
    private void saveIdentifyingCode(String codeToken, String codeResult, long expire){
        generalCache.set(RedisKey.USER_LOGIN_IDENTIFYING_CODE +codeToken,codeResult,expire);
    }


    private String checkIdentifyingCode(String codeToken, String codeResult) throws DataOpenPlatformException {
        String redisKey = RedisKey.USER_LOGIN_IDENTIFYING_CODE + codeToken;
        if(!generalCache.exists(redisKey)){
            throw new DataOpenPlatformException("人机验证码密钥错误");
        }
        codeResult = codeResult.toLowerCase();
        if(!StringUtils.equalsIgnoreCase(codeResult,generalCache.get(redisKey))){
            throw new DataOpenPlatformException("验证码错误");
        }
        return redisKey;
    }


    /**
     * 判断是否超过最大次数
     * @param username 用户名
     * @throws DataOpenPlatformException 异常
     */
    private void isLoginHasCode(String username)  throws DataOpenPlatformException{
        String redisKey = RedisKey.USER_LOGIN_ERROR_COUNT_CODE + username;
        if(generalCache.exists(redisKey)){
            String loginErrorCount = generalCache.get(redisKey);
            if(Integer.parseInt(loginErrorCount) > LOGIN_FAIL_TOTAL){
                throw new DataOpenPlatformException(ErrorCodeEnum.LOGIN_NEED_IDENTIFYING_CODE.getErrorMessage());
            }
        }
    }
}
