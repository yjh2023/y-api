package com.wind.controller;

import com.wind.model.domain.User;
import com.wind.model.request.UserLoginRequest;
import com.wind.model.request.UserRegisterRequest;
import com.wind.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.wind.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author wind
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User user = userService.userLogin (userAccount, userPassword, request);
        if(user == null){
            return null;
        }
        return user;
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        if(id == -1){
            return null;
        }
        return id;
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request){
        if(request == null){
            return null;
        }
        //
        boolean result = userService.userLogout(request);
        return result;
    }

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        if(request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            return null;
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return safetyUser;
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("delete")
    public Boolean userDelete(Long id, HttpServletRequest request){
        boolean isAdmin = userService.isAdmin(request);
        if(!isAdmin){
            return null;
        }
        if (id == null){
            return null;
        }
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        Long userId = currentUser.getId();
        if(userId == id){
           return null;
        }
        boolean result = userService.removeById(id);
        return result;
    }

}
