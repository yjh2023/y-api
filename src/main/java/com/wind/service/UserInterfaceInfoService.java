package com.wind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wind.yapicommon.model.domain.UserInterfaceInfo;

/**
 *
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 查询调用记录
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    UserInterfaceInfo getUserUserInterfaceInfo(long interfaceInfoId, long userId);
}
