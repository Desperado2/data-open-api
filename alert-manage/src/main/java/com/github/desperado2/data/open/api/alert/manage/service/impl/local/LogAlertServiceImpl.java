package com.github.desperado2.data.open.api.alert.manage.service.impl.local;

import com.github.desperado2.data.open.api.alert.manage.service.IAlertService;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.MessageTypeEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 告警服务接口
 *
 * @author tu nan
 * @date 2023/2/13
 **/
@Service("logAlertServiceImpl")
public class LogAlertServiceImpl implements IAlertService {

    private final static Logger logger = LoggerFactory.getLogger("systemWarnLog");

    @Override
    public void alert(String message, MessageTypeEnums messageTypeEnums) {
        logger.warn("系统告警:" + message);
    }
}
