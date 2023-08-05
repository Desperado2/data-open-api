package com.github.desperado2.data.open.api.api.manage.config;

import com.github.desperado2.data.open.api.api.manage.service.IApiInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 将存储的API注册为request mapping，并且提供对入参及存储的执行脚本进行解析。
 * 输出解析后的最终脚本提供给脚本执行器，然后对结果进行封装返回
 * @author tu nan
 * @date 2023/3/13
 **/
@Component
public class ApiRegisterConfig {

    private final static Logger logger = LoggerFactory.getLogger(ApiRegisterConfig.class);

    @Autowired
    private IApiInfoService apiInfoService;

    @PostConstruct
    public void  buildInit() throws Exception{
        reInit(true);
    }

    public void  reInit(Boolean isStart) throws Exception{
        apiInfoService.reLoadApiInfo(true);
    }

}
