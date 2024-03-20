package com.wind.yapicommon.service;

import com.wind.yapicommon.model.domain.InterfaceInfo;

/**
 * 内部接口信息服务
 *
 * @author wind
 */
public interface InnerInterfaceInfoService {
    /**
     * 查询调用接口是否存在
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInvokeInterfaceInfo(String path,String method);
}
