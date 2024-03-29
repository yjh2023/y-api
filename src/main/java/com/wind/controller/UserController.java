package com.wind.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wind.common.BaseResponse;
import com.wind.common.DeleteRequest;
import com.wind.common.ErrorCode;
import com.wind.common.ResultUtils;
import com.wind.exception.BusinessException;
import com.wind.model.request.user.UserLoginRequest;
import com.wind.model.request.user.UserQueryRequest;
import com.wind.model.request.user.UserRegisterRequest;
import com.wind.model.request.user.UserUpdateRequest;
import com.wind.model.vo.UserVO;
import com.wind.service.UserService;
import com.wind.yapicommon.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.userLogin (userAccount, userPassword, request);
        if(userVO == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userVO);
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        if(id == -1){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(id);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.getLoginUser(request);
        if(userVO == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(userVO);
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("delete")
    public BaseResponse<Boolean> userDelete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        Long id = deleteRequest.getId();
        boolean isAdmin = userService.isAdmin(request);
        if(!isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (id == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        if(userId == id){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }
    /**
     * 修改用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.updateUser(userUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        boolean isAdmin = userService.isAdmin(request);
        if(!isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
        }
        List<UserVO> userVOList = userService.listUser(userQuery);
        return ResultUtils.success(userVOList);
    }

    /**
     * 分页获取用户信息
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> pageUserVO(UserQueryRequest userQueryRequest,HttpServletRequest request){
        boolean isAdmin = userService.isAdmin(request);
        if(!isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if(userQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current  = userQueryRequest.getCurrrent();
        long pageSize = userQueryRequest.getPageSize();
        User userQuery = new User();
        BeanUtils.copyProperties(userQueryRequest,userQuery);
        Page<UserVO> pageUserVO = userService.getPageUserVO(current, pageSize, userQuery);
        return ResultUtils.success(pageUserVO);
    }


}
