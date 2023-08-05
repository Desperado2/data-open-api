package com.github.desperado2.data.open.api.user.manage.service;

import com.github.desperado2.data.open.api.user.manage.model.CaptchaModel;

/**
 * 验证码生成
 *
 * @author tu nan
 * @date 2023/2/10
 **/
public interface ICaptchaService {

    /**
     * 生成基础简单图像
     * @return 图片
     */
    CaptchaModel generateSimple();
}
