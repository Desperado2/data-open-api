package com.github.desperado2.data.open.api.alert.manage.service.enmus;


/**
 * 消息类型
 * @author tu nan
 * @date 2023/2/14
 **/
public enum MessageTypeEnums {

    TEXT("text"),
    MARKDOWN("markdown");

    private final String type;

    MessageTypeEnums(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
