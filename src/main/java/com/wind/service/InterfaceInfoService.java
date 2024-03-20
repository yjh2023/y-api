package com.wind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wind.yapicommon.model.domain.InterfaceInfo;

/**
 *  接口服务
 *
 * @author wind
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口信息参数
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
