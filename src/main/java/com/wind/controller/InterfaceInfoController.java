package com.wind.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wind.common.BaseResponse;
import com.wind.common.DeleteRequest;
import com.wind.common.ErrorCode;
import com.wind.common.ResultUtils;
import com.wind.exception.BusinessException;
import com.wind.model.domain.InterfaceInfo;
import com.wind.model.request.interfaceInfo.InterfaceInfoAddRequest;
import com.wind.model.request.interfaceInfo.InterfaceInfoQueryRequest;
import com.wind.model.request.interfaceInfo.InterfaceInfoUpdateRequest;
import com.wind.model.vo.UserVO;
import com.wind.service.InterfaceInfoService;
import com.wind.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;

/**
 * 接口信息管理
 *
 * @author wind
 */
@RestController
@RequestMapping("/interface")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 添加接口信息
     *
     * @param interfaceInfoAddRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest,
                                                  HttpServletRequest httpServletRequest){
        if(interfaceInfoAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest,interfaceInfo);
        // 校验参数
        interfaceInfoService.validInterfaceInfo(interfaceInfo,true);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if(!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long id = interfaceInfo.getId();
        return ResultUtils.success(id);
    }

    /**
     * 删除接口信息
     *
     * @param deleteRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(DeleteRequest deleteRequest,HttpServletRequest httpServletRequest){
        if(deleteRequest == null || deleteRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        boolean isAdmin = userService.isAdmin(httpServletRequest);
        if(!Objects.equals(interfaceInfo.getUserId(), loginUser.getId()) && !isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 修改接口信息
     *
     * @param interfaceInfoUpdateRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest httpServletRequest){
        if(interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest,interfaceInfo);
        // 校验参数
        interfaceInfoService.validInterfaceInfo(interfaceInfo,false);
        Long id = interfaceInfoUpdateRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if(oldInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        boolean isAdmin = userService.isAdmin(httpServletRequest);
        if(!Objects.equals(oldInterfaceInfo.getUserId(), loginUser.getId()) && !isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);

    }

    /**
     * 根据 id 查询接口信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfobyId(Long id){
        if(id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 批量查询接口信息（管理员）
     *
     * @param interfaceInfoQueryRequest
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                               HttpServletRequest httpServletRequest){
        boolean isAdmin = userService.isAdmin(httpServletRequest);
        if(!isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if(interfaceInfoQueryRequest != null){
            BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(list);
    }
}
