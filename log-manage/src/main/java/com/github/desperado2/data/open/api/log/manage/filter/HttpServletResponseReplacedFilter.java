package com.github.desperado2.data.open.api.log.manage.filter;


import com.github.desperado2.data.open.api.common.manage.config.HttpServletResponseCopier;
import com.github.desperado2.data.open.api.common.manage.config.RequestWrapper;
import com.github.desperado2.data.open.api.common.manage.constants.Constants;
import com.github.desperado2.data.open.api.log.manage.enums.LogEventEnums;
import com.github.desperado2.data.open.api.log.manage.event.LogEvent;
import com.github.desperado2.data.open.api.log.manage.model.LogEventModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 过滤器
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
public class HttpServletResponseReplacedFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(HttpServletResponseReplacedFilter.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public HttpServletResponseReplacedFilter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponseCopier responseWrapper = null;
        if(servletResponse instanceof HttpServletResponse){
            responseWrapper = new HttpServletResponseCopier((HttpServletResponse)servletResponse);
        }

        ServletRequest requestWrapper = null;
        if(servletRequest instanceof HttpServletRequest){
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }

        filterChain.doFilter(requestWrapper == null ? servletRequest : requestWrapper,
                responseWrapper == null ?servletResponse : responseWrapper);

        if(responseWrapper != null && servletRequest.getAttribute(Constants.LOG_KEY) != null){
            String logKey = servletRequest.getAttribute(Constants.LOG_KEY).toString();
            String responseBody = new String(responseWrapper.getBytes());
            writeLog(logKey, responseBody);
        }
        //写入结果
        if(responseWrapper != null){
            writeResponse(responseWrapper, (HttpServletResponse)servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private void writeResponse(HttpServletResponseCopier copier, HttpServletResponse response) throws IOException {
        if (copier.isUseWriter()) {
            PrintWriter out = response.getWriter();
            out.write(copier.getWriterCopy());
            out.flush();
            out.close();
        } else {
            OutputStream out = response.getOutputStream();
            out.write(copier.getStreamCopy());
            out.flush();
            out.close();
        }
    }
    private void writeLog(String logKey, String responseBody){
        // 更新日志
        try {
            log.info("logKey=" + logKey);
            if(StringUtils.isNotBlank(logKey)){
                LogEventModel logEventModel = new LogEventModel();
                logEventModel.setLogKey(logKey);
                logEventModel.setLogEventEnums(LogEventEnums.UPDATE);
                logEventModel.setResponseBody(responseBody);
                // 发布事件
                applicationEventPublisher.publishEvent(new LogEvent(logEventModel));
            }
        }catch (Exception e){
            log.error("返回日志解析失败",e);
        }
    }
}
