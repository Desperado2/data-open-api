package com.github.desperado2.data.open.api.authentication.manage.service.impl;


import com.github.desperado2.data.open.api.authentication.manage.entity.KeySecret;
import com.github.desperado2.data.open.api.authentication.manage.enums.KeySecretFieldEnum;
import com.github.desperado2.data.open.api.authentication.manage.model.SignResultModel;
import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.authentication.manage.service.ISignService;
import com.github.desperado2.data.open.api.authentication.manage.utils.SignUtils;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.utils.AESUtils;
import com.github.desperado2.data.open.api.common.manage.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 签名服务
 * @author tu nan
 * @date 2023/3/17
 **/
@Service
public class SignServiceImpl implements ISignService {

    private static final String SIGN_TIME_LENGTH = "yyyyMMddHHmmss";

    private final IKeySecretService keySecretService;

    private final UserInfoProvider userInfoProvider;

    public SignServiceImpl(IKeySecretService keySecretService, UserInfoProvider userInfoProvider) {
        this.keySecretService = keySecretService;
        this.userInfoProvider = userInfoProvider;
    }


    @Override
    public SignResultModel signByUser(Map<String, Object> data) throws DataOpenPlatformException {
        Long id = userInfoProvider.getLoginUserInfo().getId();
        KeySecret keySecret = keySecretService.keySecretByUserId(id);
        if(keySecret == null){
            throw new DataOpenPlatformException("签名秘钥不存在");
        }
        // 签名
        String signTime = DateUtils.dateFormat(new Date(), SIGN_TIME_LENGTH);
        data.put(KeySecretFieldEnum.APP_KEY.getCode(), keySecret.getAppKey());
        if(!data.containsKey(KeySecretFieldEnum.SIGN_TIME.getCode())) {
            data.put(KeySecretFieldEnum.SIGN_TIME.getCode(), signTime);
        }
        return SignUtils.sign(data, AESUtils.decrypt(keySecret.getAppSecret(), null));
    }
}
