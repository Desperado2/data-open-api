package com.github.desperado2.data.open.api.common.manage.utils;


import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.common.manage.model.MailContent;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MailUtils {

    private final static Logger log = LoggerFactory.getLogger(MailUtils.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.fromAddress:}")
    private String fromAddress;

    @Value("${spring.mail.nickname}")
    private String nickName;

    private static final String MAIL_TEXT_KEY = "text";
    private static final String MAIL_HTML_KEY = "html";

    /**
     * 邮箱格式
     */
    public static final String REG_EMAIL_FORMAT = "^[a-z_0-9.-]{1,64}@([a-z0-9-]{1,200}.){1,5}[a-z]{1,6}$";
    public static final Pattern PATTERN_EMAIL_FORMAT = Pattern.compile(REG_EMAIL_FORMAT);


    public void sendMail(MailContent mailContent, Logger customLogger) throws DataOpenPlatformException {
        Stopwatch watch = Stopwatch.createStarted();
        if (mailContent == null) {
            if (customLogger != null) {
                customLogger.error("Email content is null");
            }
            throw new DataOpenPlatformException("Email content is null");
        }

        String from = StringUtils.isEmpty(fromAddress) ? mailUsername : fromAddress;

        String displayName = nickName;
        if (!StringUtils.isEmpty(mailContent.getFrom())) {
            Matcher matcher = PATTERN_EMAIL_FORMAT.matcher(mailContent.getFrom());
            if (!matcher.find()) {
                if (customLogger != null) {
                    customLogger.error("Unknown email sending address: {}", mailContent.getFrom());
                }
                throw new DataOpenPlatformException("Unknown email sending address: " + mailContent.getFrom());
            }
            from = mailContent.getFrom();
        }

        if (!StringUtils.isEmpty(mailContent.getNickName())) {
            displayName = mailContent.getNickName();
        }

        if (StringUtils.isEmpty(mailContent.getSubject())) {
            if (customLogger != null) {
                customLogger.error("Email subject cannot be empty");
            }
            throw new DataOpenPlatformException("Email subject cannot be empty");
        }

        if (null == mailContent.getTo() || mailContent.getTo().length < 1) {
            if (customLogger != null) {
                log.error("Email receiving address(to) cannot be empty");
            }
            throw new DataOpenPlatformException("Email receiving address cannot be empty");
        }

        boolean multipart = false;
        boolean emptyAttachments = CollectionUtils.isEmpty(mailContent.getAttachments());
        String mailContentTemplate = null;
        String contentHtml = null;
        Context context = new Context();
        switch (mailContent.getMailContentType()) {
            case TEXT:
                if (StringUtils.isEmpty(mailContent.getContent()) && emptyAttachments) {
                    throw new DataOpenPlatformException("Email content cannot be empty");
                }
                if (!emptyAttachments) {
                    multipart = true;
                }
                context.setVariable(MAIL_TEXT_KEY, mailContent.getContent());
                mailContentTemplate = Constants.EMAIL_DEFAULT_TEMPLATE;
                break;
            case HTML:
                if (StringUtils.isEmpty(mailContent.getHtmlContent()) && emptyAttachments) {
                    throw new DataOpenPlatformException("Email content cannot be empty");
                }
                if (!emptyAttachments) {
                    multipart = true;
                }
                context.setVariable(MAIL_HTML_KEY, mailContent.getHtmlContent());
                mailContentTemplate = Constants.EMAIL_DEFAULT_TEMPLATE;
                break;
            case TEMPLATE:
                if (StringUtils.isEmpty(mailContent.getTemplate()) && emptyAttachments) {
                    throw new DataOpenPlatformException("Email content cannot be empty");
                }
                if (!CollectionUtils.isEmpty(mailContent.getTemplateContent())) {
                    mailContent.getTemplateContent().forEach(context::setVariable);
                }
                mailContentTemplate = mailContent.getTemplate();
                multipart = true;
                break;
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, multipart);

            messageHelper.setFrom(from, displayName);
            messageHelper.setSubject(mailContent.getSubject());
            messageHelper.setTo(mailContent.getTo());
            if (null != mailContent.getCc() && mailContent.getCc().length > 0) {
                messageHelper.setCc(mailContent.getCc());
            }
            if (null != mailContent.getBcc() && mailContent.getBcc().length > 0) {
                messageHelper.setBcc(mailContent.getBcc());
            }

            List<String> imageContentIds = new ArrayList<>();

            if (!emptyAttachments) {
                mailContent.getAttachments().forEach(attachment -> {
                    if (attachment.isImage()) {
                        imageContentIds.add(attachment.getName());
                    }
                });
            }

            if (!CollectionUtils.isEmpty(imageContentIds)) {
                context.setVariable("images", imageContentIds);
            }

            contentHtml = templateEngine.process(mailContentTemplate, context);
            messageHelper.setText(contentHtml, true);

            if (!emptyAttachments) {
                mailContent.getAttachments().forEach(attachment -> {
                    try {
                        if (attachment.isImage()) {
                            messageHelper.addInline(attachment.getName(), attachment.getFile());
                        } else {
                            messageHelper.addAttachment(attachment.getName(), attachment.getFile());
                        }
                    } catch (MessagingException e) {
                        log.warn(e.getMessage());
                        if (customLogger != null) {
                            log.warn(e.getMessage());
                        }
                    }
                });
            }

            javaMailSender.send(message);
            if (customLogger != null) {
                customLogger.info("Email sending content:{}, cost:{}", mailContent.toString(), watch.elapsed(TimeUnit.MILLISECONDS));
            }
        } catch (Exception e) {
            if (customLogger != null) {
                customLogger.error("Send mail error:{}", e.getMessage());
            }
            e.printStackTrace();
            throw new DataOpenPlatformException(e.getMessage());
        } 
    }
}
