package com.wind.yapicommon.service;

import com.wind.yapicommon.model.domain.User;

/**
 * 内部用户服务
 *
 * @author wind
 */
public interface InnerUserService {
    /**
     * 查询是否已经分配给用户 accessKey
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
