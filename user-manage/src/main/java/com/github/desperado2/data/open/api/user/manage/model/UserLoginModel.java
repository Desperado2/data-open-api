package com.github.desperado2.data.open.api.user.manage.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户登录请求模型
 * @author tu nan
 * @date 2023/2/9
 **/
@ApiModel(value = "UserLoginModel", description = "用户登录模型")
public class UserLoginModel {

    @ApiModelProperty("邮箱")
    @Size(min=1,max = 50, message = "邮箱最长50位")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "不是正确的邮箱格式")
    private String email;

    @ApiModelProperty("密码")
    @Size(max = 50, message = "密码最长50位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "人机验证码凭证")
    private String codeToken;


    @ApiModelProperty(value = "人机验证码")
    private String codeResult;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeToken() {
        return codeToken;
    }

    public void setCodeToken(String codeToken) {
        this.codeToken = codeToken;
    }

    public String getCodeResult() {
        return codeResult;
    }

    public void setCodeResult(String codeResult) {
        this.codeResult = codeResult;
    }
}
