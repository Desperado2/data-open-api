package com.github.desperado2.data.open.api.alert.manage.service;

import com.github.desperado2.data.open.api.alert.manage.service.enmus.MessageTypeEnums;

/**
 * 告警服务接口
 *
 * @author tu nan
 * @date 2023/2/13
 **/
public interface IAlertService {

    void alert(String message, MessageTypeEnums messageTypeEnums);
}
