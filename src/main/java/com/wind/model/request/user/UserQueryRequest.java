package com.wind.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author wind
 */
@Data
public class UserQueryRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}