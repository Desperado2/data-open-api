
package com.github.desperado2.data.open.api.common.manage.utils;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * UUID工具类
 * @author tu nan
 * @date 2023/2/10
 **/
public class LogIdConvert extends ClassicConverter {

    public LogIdConvert() {}

    @Override
    public String convert(ILoggingEvent event) {
        return LogUtil.getLogId();
    }
}
