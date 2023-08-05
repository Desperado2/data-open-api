package com.github.desperado2.data.open.api.alert.manage.service.impl.dingding;

import com.github.desperado2.data.open.api.alert.manage.service.IAlertService;
import com.github.desperado2.data.open.api.alert.manage.service.config.DingtalkConfig;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.MessageTypeEnums;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 告警服务接口
 *
 * @author tu nan
 * @date 2023/2/13
 **/
@Component("dingDingAlertServiceImpl")
@ConditionalOnBean(value ={DingtalkConfig.class, DingtalkSendService.class})
public class DingDingAlertServiceImpl implements IAlertService {

    private static final Logger logger = LoggerFactory.getLogger(DingDingAlertServiceImpl.class);

    private final DingtalkSendService dingtalkSendService;

    public DingDingAlertServiceImpl(DingtalkSendService dingtalkSendService) {
        this.dingtalkSendService = dingtalkSendService;
    }

    @Override
    public void alert(String message, MessageTypeEnums messageTypeEnums) {
        try {
            String msg = "系统通知:\n" + message;
            dingtalkSendService.send(msg, messageTypeEnums);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | ApiException e) {
            logger.error("钉钉消息发送失败", e);
        }
    }
}
