package com.wind.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wind.common.BaseResponse;
import com.wind.common.ErrorCode;
import com.wind.common.ResultUtils;
import com.wind.exception.BusinessException;
import com.wind.mapper.UserInterfaceInfoMapper;
import com.wind.model.vo.InterfaceInfoVO;
import com.wind.service.InterfaceInfoService;
import com.wind.service.UserService;
import com.wind.yapicommon.model.domain.InterfaceInfo;
import com.wind.yapicommon.model.domain.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口调用数据分析
 *
 * @author wind
 */
@RestController
@RequestMapping("analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo(HttpServletRequest request){
        boolean isAdmin = userService.isAdmin(request);
        if(!isAdmin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterface(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }
}
