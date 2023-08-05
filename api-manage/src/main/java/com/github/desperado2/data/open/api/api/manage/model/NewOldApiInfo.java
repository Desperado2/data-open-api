package com.github.desperado2.data.open.api.api.manage.model;


import com.github.desperado2.data.open.api.cache.manage.model.ApiInfo;

public class NewOldApiInfo {
    private Long apiInfoId;
    private ApiInfo newApiInfo;
    private ApiInfo oldApiInfo;

    public NewOldApiInfo() {
    }

    public NewOldApiInfo(Long apiInfoId, ApiInfo newApiInfo, ApiInfo oldApiInfo) {
        this.apiInfoId = apiInfoId;
        this.newApiInfo = newApiInfo;
        this.oldApiInfo = oldApiInfo;
    }

    public Long getApiInfoId() {
        return apiInfoId;
    }

    public void setApiInfoId(Long apiInfoId) {
        this.apiInfoId = apiInfoId;
    }

    public ApiInfo getNewApiInfo() {
        return newApiInfo;
    }

    public void setNewApiInfo(ApiInfo newApiInfo) {
        this.newApiInfo = newApiInfo;
    }

    public ApiInfo getOldApiInfo() {
        return oldApiInfo;
    }

    public void setOldApiInfo(ApiInfo oldApiInfo) {
        this.oldApiInfo = oldApiInfo;
    }
}
