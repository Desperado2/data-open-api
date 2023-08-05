package com.github.desperado2.data.open.api.log.manage.event;



import com.github.desperado2.data.open.api.log.manage.model.LogEventModel;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 * @author tu nan
 * @date 2023/2/13
 **/
public class LogEvent  extends ApplicationEvent {


    public LogEvent(LogEventModel logEventModel) {
        super(logEventModel);
    }
}
