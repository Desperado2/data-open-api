package com.github.desperado2.data.open.api.user.manage.service.impl;


import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.common.manage.config.OpenApiProperties;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.utils.*;
import com.github.desperado2.data.open.api.lock.manage.enums.CheckEntityEnum;
import com.github.desperado2.data.open.api.lock.manage.enums.LockType;
import com.github.desperado2.data.open.api.lock.manage.factory.LockFactory;
import com.github.desperado2.data.open.api.lock.manage.model.BaseLock;
import com.github.desperado2.data.open.api.lock.manage.service.AbstractBaseLockService;
import com.github.desperado2.data.open.api.user.manage.config.UserConfig;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.enums.UserStatusEnum;
import com.github.desperado2.data.open.api.user.manage.model.UserRegisterModel;
import com.github.desperado2.data.open.api.user.manage.service.IRegisterService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Locale;

/**
 * 注册服务
 * @author tu nan
 * @date 2023/2/10
 **/

@Service
public class RegisterServiceImpl extends AbstractBaseLockService implements IRegisterService {



    private static final CheckEntityEnum entity = CheckEntityEnum.USER;

    private final SendEMailUtils sendEMailUtils;

    private final IUserService userService;

    private final ServerUtils serverUtils;

    private final IKeySecretService keySecretService;

    private final UserConfig userConfig;

    private final OpenApiProperties openApiProperties;

    public RegisterServiceImpl(SendEMailUtils sendEMailUtils, IUserService userService, ServerUtils serverUtils, IKeySecretService keySecretService, UserConfig userConfig, OpenApiProperties openApiProperties) {
        this.sendEMailUtils = sendEMailUtils;
        this.userService = userService;
        this.serverUtils = serverUtils;
        this.keySecretService = keySecretService;
        this.userConfig = userConfig;
        this.openApiProperties = openApiProperties;
    }


    @Override
    public void register(UserRegisterModel userRegisterModel) throws DataOpenPlatformException {
        // 注册逻辑
        String email = userRegisterModel.getEmail();
        // 判断邮箱是否合法
        if(userConfig.getEmailSuffix() != null){
            if(Arrays.stream(userConfig.getEmailSuffix().split(",")).noneMatch(it -> email.endsWith(it.trim()))){
                throw new DataOpenPlatformException("不支持的邮箱,支持的邮箱后缀为:"+ userConfig.getEmailSuffix());
            }
        }
        // 判断密码是否合法
        PasswordCheckUtils.CheckResult check = PasswordCheckUtils.check(userRegisterModel.getPassword());
        if(!check.getPass()){
            throw new DataOpenPlatformException(check.getErrorMsg());
        }

        // 判断邮箱是否存在
        User existUser = userService.selectByEmail(email);
        if(existUser != null){
            throw new DataOpenPlatformException(email + "已经被注册");
        }
        // 邮箱加锁
        BaseLock emailLock = getLock(entity, email, null);
        if(emailLock != null && !emailLock.getLock()) {
            // 加锁失败
            throw new DataOpenPlatformException(email + "已经被注册");
        }
        // 产生用户
        try {
            User user = new User();
            // 密码加密
            user.setPassword(AESUtils.encrypt(userRegisterModel.getPassword(), null));
            user.setEmail(userRegisterModel.getEmail());
            user.setName(genUsername(userRegisterModel.getName()));
            // 添加用户
            userService.saveUser(user);
            // 发送激活邮件
            //sendMail(user);
        }finally {
            releaseLock(emailLock);
        }
    }


    @Override
    public void activateUserNoLogin(String token, HttpServletRequest request) throws DataOpenPlatformException {
        // 解密获取信息
        String tokenStr = AESUtils.decrypt(token, null);
        // 获取信息
        String[] split = tokenStr.split(" ");
        if(split.length != 3){
            throw new DataOpenPlatformException("无效的token");
        }
        String email = split[0];
        if(StringUtils.isBlank(email)){
            throw new DataOpenPlatformException("无效的token");
        }
        // 查询用户
        User user = userService.selectByEmail(email);
        if(user == null){
            throw new DataOpenPlatformException("无效的token");
        }
        if(!user.getStatus().equals(UserStatusEnum.NOT_ACTIVATED.getCode())){
            throw new DataOpenPlatformException("该用户已激活，不需要去重新激活");
        }
        // 获取激活锁
        BaseLock lock = LockFactory.getLock("ACTIVATE" + Constants.AT_SYMBOL + email.toUpperCase(), 5, LockType.REDIS);
        if (!lock.getLock()) {
            throw new DataOpenPlatformException("当前用户正在激活中，请稍后再试");
        }
        String password = split[1];
        try {
            // 验证密码
            if(user.getPassword().equals(password)){
                // 密码相同  激活
               userService.activeUser(user);
               // 生成对接秘钥信息
                keySecretService.generateKeySecret(user.getId());
            }else{
                throw new DataOpenPlatformException("无效的token");
            }
        }finally {
            releaseLock(lock);
        }
    }

    @Override
    public void activateUserNoLogin(Long userId) throws DataOpenPlatformException {
        // 查询用户
        User user = userService.getById(userId);
        if(user == null){
            throw new DataOpenPlatformException("用户不存在");
        }
        if(!user.getStatus().equals(UserStatusEnum.NOT_ACTIVATED.getCode())){
            throw new DataOpenPlatformException("该用户已激活，不需要去重新激活");
        }
        // 获取激活锁
//        BaseLock lock = LockFactory.getLock("ACTIVATE" + Constants.AT_SYMBOL + user.getEmail().toUpperCase(), 5, LockType.REDIS);
//        if (!lock.getLock()) {
//            throw new DataOpenPlatformException("当前用户正在激活中，请稍后再试");
//        }
        // 激活
        // 生成密码
        String password = generalPassword();
        // 密码加密
        user.setPassword(AESUtils.encrypt(Md5Encrypt.md5Hexdigest(password).toUpperCase(Locale.ROOT), null));
        userService.activeUser(user);
        // 生成对接秘钥信息
        keySecretService.generateKeySecret(user.getId());
       // 发送邮件
        sendMail(user, password);
    }



    /**
     * 生成随机用户名
     * @param username 原始用户名
     * @return 随机用户名
     */
    private String genUsername(String username){
        if(username == null || StringUtils.isBlank(username)){
            username = NameUtils.getRandomNickname();
        }
        return username;
    }

    /**
     * 发送激活邮件
     * @param user 用户
     * @return 返回值
     * @throws DataOpenPlatformException 发送失败
     */
    private boolean sendMail(User user, String password) throws DataOpenPlatformException {
        String name = user.getName();
        String email = user.getEmail();
        String serviceFrontAddress = openApiProperties.getServiceFrontAddress();
        if(StringUtils.isBlank(serviceFrontAddress)){
            serviceFrontAddress = serverUtils.getHost();
        }
        return sendEMailUtils.sendMail2(email, name, serviceFrontAddress, password);
    }


    /**
     * 生成符合条件的密码
     * @return 密码
     */
    private String generalPassword(){
        while(true){
            String password = StrGeneralUtils.getPassword(10, true, true, true);
            PasswordCheckUtils.CheckResult check = PasswordCheckUtils.check(password);
            if(check.getPass()){
                return password;
            }
        }
    }
}
