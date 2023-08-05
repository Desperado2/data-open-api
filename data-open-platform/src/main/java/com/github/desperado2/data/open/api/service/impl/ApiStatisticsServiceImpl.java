package com.github.desperado2.data.open.api.service.impl;

import com.github.desperado2.data.open.api.api.manage.service.IOpenApiService;
import com.github.desperado2.data.open.api.cache.manage.config.UserInfoProvider;
import com.github.desperado2.data.open.api.common.manage.enums.RoleCodeEnum;
import com.github.desperado2.data.open.api.common.manage.model.BaseUserModel;
import com.github.desperado2.data.open.api.log.manage.model.ApiLogStatisticsModel;
import com.github.desperado2.data.open.api.log.manage.service.IApiLogsService;
import com.github.desperado2.data.open.api.model.statistic.BaseInfoStatisticsModel;
import com.github.desperado2.data.open.api.model.statistic.RequestInfoStatisticsModel;
import com.github.desperado2.data.open.api.service.IApiStatisticsService;
import com.github.desperado2.data.open.api.service.IApiSubscribeService;
import com.github.desperado2.data.open.api.user.manage.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * 实现
 * @author tu nan
 * @date 2023/3/22
 **/
@Service
public class ApiStatisticsServiceImpl implements IApiStatisticsService {

    private final IOpenApiService openApiService;
    private final IApiSubscribeService apiSubscribeService;
    private final IApiLogsService apiLogsService;
    private final IUserService userService;
    private final UserInfoProvider userInfoProvider;

    public ApiStatisticsServiceImpl(IOpenApiService openApiService, IApiSubscribeService apiSubscribeService,
                                    IApiLogsService apiLogsService, IUserService userService, UserInfoProvider userInfoProvider) {
        this.openApiService = openApiService;
        this.apiSubscribeService = apiSubscribeService;
        this.apiLogsService = apiLogsService;
        this.userService = userService;
        this.userInfoProvider = userInfoProvider;
    }

    @Override
    public BaseInfoStatisticsModel baseStatistics() throws Exception {
        // api
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        BaseInfoStatisticsModel baseInfoStatisticsModel = new BaseInfoStatisticsModel();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        if(RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){

            FutureTask<Integer> apiTask = new FutureTask<>(apiSubscribeService::normalCount);
            executorService.submit(apiTask);

            FutureTask<Integer> subscribeTask = new FutureTask<>(apiSubscribeService::normalCount);
            executorService.submit(subscribeTask);

            FutureTask<Integer> userTask = new FutureTask<>(userService::normalCount);
            executorService.submit(userTask);

            FutureTask<Integer> callTask = new FutureTask<>(apiLogsService::normalCount);
            executorService.submit(callTask);

            FutureTask<Integer> successTask = new FutureTask<>(apiLogsService::successCount);
            executorService.submit(successTask);

            FutureTask<Integer> failTask = new FutureTask<>(apiLogsService::failCount);
            executorService.submit(failTask);

            baseInfoStatisticsModel.setApiCount(apiTask.get());
            baseInfoStatisticsModel.setSubscribeCount(subscribeTask.get());
            baseInfoStatisticsModel.setUserCount(userTask.get());
            baseInfoStatisticsModel.setCallCount(callTask.get());
            baseInfoStatisticsModel.setSuccessCount(successTask.get());
            baseInfoStatisticsModel.setFailCount(failTask.get());
        }else{
            Long userId = loginUserInfo.getId();
            FutureTask<Integer> apiTask = new FutureTask<>(() ->apiSubscribeService.subscribeCountByUserId(userId));
            executorService.submit(apiTask);

            FutureTask<Integer> subscribeTask = new FutureTask<>(() ->apiSubscribeService.normalCountByUserId(userId));
            executorService.submit(subscribeTask);

            FutureTask<Integer> userTask = new FutureTask<>(() ->apiSubscribeService.approvalingCountByUserId(userId));
            executorService.submit(userTask);

            FutureTask<Integer> callTask = new FutureTask<>(() -> apiLogsService.normalCountByUserId(userId));
            executorService.submit(callTask);

            FutureTask<Integer> successTask = new FutureTask<>(() -> apiLogsService.successCountByUserId(userId));
            executorService.submit(successTask);

            FutureTask<Integer> failTask = new FutureTask<>(() -> apiLogsService.failCountByUserId(userId));
            executorService.submit(failTask);

            baseInfoStatisticsModel.setApiCount(apiTask.get());
            baseInfoStatisticsModel.setSubscribeCount(subscribeTask.get());
            baseInfoStatisticsModel.setUserCount(userTask.get());
            baseInfoStatisticsModel.setCallCount(callTask.get());
            baseInfoStatisticsModel.setSuccessCount(successTask.get());
            baseInfoStatisticsModel.setFailCount(failTask.get());
        }
        executorService.shutdown();
        return baseInfoStatisticsModel;
    }

    @Override
    public List<RequestInfoStatisticsModel> requestStatistics() {
        BaseUserModel loginUserInfo = userInfoProvider.getLoginUserInfo();
        List<ApiLogStatisticsModel> apiLogStatisticsModels;
        if(RoleCodeEnum.ADMIN.getCode().equals(loginUserInfo.getRoleCode())){
            apiLogStatisticsModels = apiLogsService.logStatistics();
        }else{
            apiLogStatisticsModels = apiLogsService.logStatisticsByUserId(loginUserInfo.getId());
        }
        return apiLogStatisticsModels.stream().map(it -> {
            RequestInfoStatisticsModel requestInfoStatisticsModel = new RequestInfoStatisticsModel();
            requestInfoStatisticsModel.setDate(it.getDate());
            requestInfoStatisticsModel.setCallCount(it.getRequireCount());
            requestInfoStatisticsModel.setSuccessCount(it.getSuccessCount());
            requestInfoStatisticsModel.setFailCount(it.getFailCount());
            return requestInfoStatisticsModel;
        }).collect(Collectors.toList());
    }
}
