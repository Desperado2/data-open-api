package com.github.desperado2.data.open.api.alert.manage.service.impl.dingding;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.github.desperado2.data.open.api.alert.manage.service.config.DingtalkConfig;
import com.github.desperado2.data.open.api.alert.manage.service.enmus.MessageTypeEnums;
import com.taobao.api.ApiException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 钉钉消息发送
 * @author tu nan
 * @date 2023/4/28
 **/
@Configuration
@ConditionalOnBean(value = {DingtalkConfig.class})
public class DingtalkSendService {

    private static final Logger logger = LoggerFactory.getLogger(DingDingAlertServiceImpl.class);

    private final DingtalkConfig dingtalkConfig;

    public DingtalkSendService(DingtalkConfig dingtalkConfig) {
        this.dingtalkConfig = dingtalkConfig;
    }

    public void send(String content, MessageTypeEnums messageTypeEnums) throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + dingtalkConfig.getSecret();
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(dingtalkConfig.getSecret().getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        String signResult = "&timestamp=" + timestamp + "&sign=" + sign;
        // 得到拼接后的 URL
        String baseUrl = "https://oapi.dingtalk.com/robot/send?access_token=";
        String url = baseUrl + dingtalkConfig.getTokenId() + signResult;
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendResponse response = client.execute(buildRequest(content, messageTypeEnums));
        logger.debug("dingding robot msg send response:" + response.isSuccess());
        if(!response.isSuccess()){
            logger.debug("dingding robot msg send response msg:" + response.getErrmsg());
        }
    }

    public OapiRobotSendRequest buildRequest(String content, MessageTypeEnums messageTypeEnums){
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(messageTypeEnums.getType());
        if (messageTypeEnums == MessageTypeEnums.MARKDOWN) {
            OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
            markdown.setText(content);
            request.setMarkdown(markdown);
        } else {
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(content);
            request.setText(text);
        }
        return request;
    }
}
