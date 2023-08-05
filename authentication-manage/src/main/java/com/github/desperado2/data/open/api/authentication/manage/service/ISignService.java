package com.github.desperado2.data.open.api.authentication.manage.service;


import com.github.desperado2.data.open.api.authentication.manage.model.SignResultModel;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;

import java.util.Map;

/**
 * 签名服务
 * @author tu nan
 * @date 2023/3/17
 **/
public interface ISignService {

    SignResultModel signByUser(Map<String, Object> data) throws DataOpenPlatformException;

}
