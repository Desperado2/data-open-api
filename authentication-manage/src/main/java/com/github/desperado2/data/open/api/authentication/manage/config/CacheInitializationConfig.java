package com.github.desperado2.data.open.api.authentication.manage.config;


import com.github.desperado2.data.open.api.authentication.manage.model.KeySecretModel;
import com.github.desperado2.data.open.api.authentication.manage.service.IKeySecretService;
import com.github.desperado2.data.open.api.cache.manage.chche.IKeySecretCache;
import com.github.desperado2.data.open.api.cache.manage.model.KeySecretInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 缓存初始化
 * @author tu nan
 * @date 2023/3/18
 **/
@Component
public class CacheInitializationConfig {

    @Autowired
    private IKeySecretCache keySecretCache;

    @Autowired
    private IKeySecretService keySecretService;

    @PostConstruct
    public void init(){
        List<KeySecretModel> keySecretAllEnable = keySecretService.getKeySecretAllEnable();
        keySecretAllEnable.forEach(it -> {
            keySecretCache.put(KeySecretInfo.Builder.builder()
                    .id(it.getId())
                    .appKey(it.getAppKey())
                    .appSecret(it.getAppSecret())
                    .userId(it.getUserId()).build());
        });
    }
}
