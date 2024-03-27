package com.wind.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wind.common.ErrorCode;
import com.wind.exception.BusinessException;
import com.wind.service.InterfaceInfoService;
import com.wind.service.UserInterfaceInfoService;
import com.wind.service.UserService;
import com.wind.yapicommon.model.domain.InterfaceInfo;
import com.wind.yapicommon.model.domain.User;
import com.wind.yapicommon.model.domain.UserInterfaceInfo;
import com.wind.yapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if(interfaceInfoId <=0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        Long invokePrice = interfaceInfo.getInvokePrice();
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id",userId);
        userUpdateWrapper.setSql("balance = balance - " + invokePrice);
        userService.update(userUpdateWrapper);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getUserUserInterfaceInfo(interfaceInfoId,userId);
        if(userInterfaceInfo == null){
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setTotalNum(0);
        }
        userInterfaceInfo.setTotalNum(userInterfaceInfo.getTotalNum() + 1);
        return userInterfaceInfoService.saveOrUpdate(userInterfaceInfo);
    }
}
