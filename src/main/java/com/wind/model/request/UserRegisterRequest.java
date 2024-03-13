package com.wind.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author wind
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

    private static final long serialVersionUID = 1L;

}
