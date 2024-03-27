package com.wind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wind.common.ErrorCode;
import com.wind.exception.BusinessException;
import com.wind.service.UserInterfaceInfoService;
import com.wind.mapper.UserInterfaceInfoMapper;
import com.wind.yapicommon.model.domain.UserInterfaceInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 查询调用记录
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public UserInterfaceInfo getUserUserInterfaceInfo(long interfaceInfoId, long userId){
        if(interfaceInfoId <=0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        queryWrapper.eq("userId",userId);
        return userInterfaceInfoMapper.selectOne(queryWrapper);
    }

}




