package com.github.desperado2.data.open.api.common.manage.constants;


/**
 *
 * @author tu nan
 * @date 2023/2/10
 **/
public class Constants {

    /**
     * 用户激活 / 重发激活邮件模板
     */
    public static final String USER_ACTIVATE_EMAIL_TEMPLATE = "mail/userActivateEmailTemplate";

    /**
     * 用户激开通邮件模板
     */
    public static final String USER_OPEN_EMAIL_TEMPLATE = "mail/userOpenEmailTemplate";

    /**
     * 用户重置密码邮件模板
     */
    public static final String USER_REST_PASSWORD_EMAIL_TEMPLATE = "mail/userRestPasswordEmailTemplate";


    public static final String EMAIL_DEFAULT_TEMPLATE = "mail/emaiDefaultTemplate";

    /**
     * 用户激活 / 重发激活邮件主题
     */
    public static final String USER_ACTIVATE_EMAIL_SUBJECT = "[数据开放平台] 用户激活";

    /**
     * 用户开通邮件主题
     */
    public static final String USER_OPEN_EMAIL_SUBJECT = "[数据开放平台] 用户开通";

    /**
     * 默认不开放申请的原因
     */
    public static final String DEFAULT_NOT_OPEN_APPLY_REASON = "API维护中,暂不支持申请......";

    /**
     * 用户重置密码邮件主题
     */
    public static final String USER_REST_PASSWORD_EMAIL_SUBJECT = "[数据开放平台] 重置密码";


    public static final String AT_SYMBOL = "@";

    public static final String HTTP_PROTOCOL = "http";

    public static final String HTTPS_PROTOCOL = "https";

    public static final String PROTOCOL_SEPARATOR = "://";

    public static final String SLASH = "/";

    public static final String BASE_PATH = "/open-data-platform";

    // 日志唯一key
    public static final String LOG_KEY = "logKey";

    // 请求环境请求头key
    public static final String API_REQUEST_ENVIRONMENT = "execute-env";
}
