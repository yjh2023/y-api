package com.wind.service;

import com.wind.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wind.model.request.UserUpdateRequest;
import com.wind.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author wind
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 退出登录
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    UserVO getSafetyUser(User originUser);

    /**
     * 用户鉴权
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 批量查询用户
     * @param user
     * @return
     */
    List<UserVO> listUser(User user);

    /**
     * 修改用户
     * @param userUpdateRequest
     * @param request
     * @return
     */
    int updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);
}
