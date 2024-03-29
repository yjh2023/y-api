package com.wind.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wind.common.BaseResponse;
import com.wind.common.DeleteRequest;
import com.wind.common.ErrorCode;
import com.wind.common.ResultUtils;
import com.wind.exception.BusinessException;
import com.wind.model.enums.InterfaceInfoStatusEnum;
import com.wind.model.request.interfaceInfo.InterfaceInfoAddRequest;
import com.wind.model.request.interfaceInfo.InterfaceInfoInvokeRequest;
import com.wind.model.request.interfaceInfo.InterfaceInfoQueryRequest;
import com.wind.model.request.interfaceInfo.InterfaceInfoUpdateRequest;
import com.wind.model.vo.UserVO;
import com.wind.service.InterfaceInfoService;
import com.wind.service.UserService;
import com.wind.yapiclientsdk.client.YapiClient;
import com.wind.yapicommon.model.domain.InterfaceInfo;
import com.wind.yapicommon.model.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
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

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> pageInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest){
        if(interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = interfaceInfoQueryRequest.getCurrrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        String description = interfaceInfoQueryRequest.getDescription();
        // 限制爬虫
        if(pageSize > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest,interfaceInfoQuery);
        // 支持 description 模糊搜索
        interfaceInfoQuery.setDescription(null);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description),"description",description);
        Page<InterfaceInfo> pageInterfaceInfo = interfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(pageInterfaceInfo);
    }

    /**
     * 发布接口
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterface(Long id,HttpServletRequest httpServletRequest){
        if(id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // todo 判断是否可以调用
        boolean isAdmin = userService.isAdmin(httpServletRequest);
        if(!isAdmin && !loginUser.getId().equals(interfaceInfo.getUserId())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterface(Long id,HttpServletRequest httpServletRequest){
        if(id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean isAdmin = userService.isAdmin(httpServletRequest);
        if(!isAdmin && !loginUser.getId().equals(interfaceInfo.getUserId())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("invoke")
    public BaseResponse<Object> invokeInterface(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest
            , HttpServletRequest httpServletRequest){
        if(interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfoInvokeRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if(interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }
        UserVO loginUser = userService.getLoginUser(httpServletRequest);
        User user = userService.getById(loginUser.getId());
        if(user.getBalance() < interfaceInfo.getInvokePrice()){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"余额不足");
        }
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        YapiClient yapiClient = new YapiClient(accessKey,secretKey);
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();
        try {
            Method method = YapiClient.class.getMethod(interfaceInfo.getName(),String.class);
            String invoke =(String) method.invoke(yapiClient,requestParams);
            return ResultUtils.success(invoke);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }
}
