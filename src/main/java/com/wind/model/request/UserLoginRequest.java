package com.wind.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author wind
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    private static final long serialVersionUID = 1L;
}
