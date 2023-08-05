package com.github.desperado2.data.open.api.common.manage.utils;

import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class ServerUtils {
    @Value("${server.protocol:http}")
    private String protocol;

    @Value("${server.address}")
    private String address;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;


    public String getHost() {

        String pro = protocol.trim().toLowerCase();
        if (pro.equals(Constants.HTTP_PROTOCOL) && "80".equals(port)) {
            port = null;
        }

        if (pro.equals(Constants.HTTPS_PROTOCOL) && "443".equals(port)) {
            port = null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(pro).append(Constants.PROTOCOL_SEPARATOR).append(address);
        if (!StringUtils.isEmpty(port)) {
            sb.append(":" + port);
        }

        if (!StringUtils.isEmpty(contextPath)) {
            contextPath = contextPath.replaceAll(Constants.SLASH, "");
            sb.append(Constants.SLASH);
            sb.append(contextPath);
        }

        return sb.toString();
    }

    public String getLocalHost() {
        String hostName="localhost";
        try {
            InetAddress ia = InetAddress.getLocalHost();
            hostName=ia.getHostName();
        }catch (UnknownHostException ex){
            hostName="localhost";
        }
        return protocol + Constants.PROTOCOL_SEPARATOR + hostName+":" + port;
    }

}
