package com.github.desperado2.data.open.api.common.manage.utils;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.enums.MailContentTypeEnum;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.MailContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件工具类
 * @author tu nan
 * @date 2023/2/10
 **/
@Component
public class SendEMailUtils {


    @Autowired
    private MailUtils mailUtils;


    public boolean sendMail(String email, String name, String host, String token) throws DataOpenPlatformException {
        //校验邮箱
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("username", name);
        content.put("host", host);
        content.put("token", token);

        MailContent mailContent = MailContent.MailContentBuilder.builder()
                .withSubject(Constants.USER_ACTIVATE_EMAIL_SUBJECT)
                .withTo(email)
                .withMainContent(MailContentTypeEnum.TEMPLATE)
                .withTemplate(Constants.USER_ACTIVATE_EMAIL_TEMPLATE)
                .withTemplateContent(content)
                .build();
        mailUtils.sendMail(mailContent, null);
        return true;
    }


    public boolean sendMail2(String email, String name, String host, String password) throws DataOpenPlatformException {
        //校验邮箱
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("username", name);
        content.put("host", host);
        content.put("password", password);
        content.put("email", email);
        content.put("openDate", DateUtils.getNowString());

        MailContent mailContent = MailContent.MailContentBuilder.builder()
                .withSubject(Constants.USER_OPEN_EMAIL_SUBJECT)
                .withTo(email)
                .withMainContent(MailContentTypeEnum.TEMPLATE)
                .withTemplate(Constants.USER_OPEN_EMAIL_TEMPLATE)
                .withTemplateContent(content)
                .build();
        mailUtils.sendMail(mailContent, null);
        return true;
    }
}
