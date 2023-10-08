package com.github.desperado2.data.open.api.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.desperado2.data.open.api.cache.manage.chche.IApiApplyInfoCache;
import com.github.desperado2.data.open.api.cache.manage.model.ApiApplyInfo;
import com.github.desperado2.data.open.api.entity.ApiSubscribe;
import com.github.desperado2.data.open.api.service.IApiSubscribeService;
import com.github.desperado2.data.open.api.user.manage.entity.User;
import com.github.desperado2.data.open.api.user.manage.enums.UserStatusEnum;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 申请初始化
 * @author tu nan
 * @date 2023/3/18
 **/
@Component
public class SubscribeInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeInitializationConfig.class);
    private final IApiApplyInfoCache apiApplyInfoCache;

    private final IApiSubscribeService apiSubscribeService;

    private final IUserService userService;

    public SubscribeInitializationConfig(IApiApplyInfoCache apiApplyInfoCache, @Lazy IApiSubscribeService apiSubscribeService,
                                         @Lazy IUserService userService) {
        this.apiApplyInfoCache = apiApplyInfoCache;
        this.apiSubscribeService = apiSubscribeService;
        this.userService = userService;
    }

    @PostConstruct
    public void init(){
        List<ApiSubscribe> allEnableList = apiSubscribeService.getAllEnableList();
        Set<Long> userIdList = allEnableList.stream().map(ApiSubscribe::getUserId).collect(Collectors.toSet());
        logger.info("用户id：{}", userIdList.size());
        List<User> userList = userService.getBaseMapper().selectList(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, UserStatusEnum.NORMAL.getCode())
                .in(User::getId, userIdList));
        allEnableList.forEach(it -> {
            userList.stream().filter(it1 -> it1.getId().equals(it.getUserId())).findFirst()
                    .ifPresent(user -> apiApplyInfoCache.put(ApiApplyInfo.Builder.builder()
                    .userId(it.getUserId())
                    .username(user.getName())
                    .apiId(it.getApiId())
                    .build()));
        });
    }
}
