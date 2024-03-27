package com.wind.model.request.interfaceInfo;

import lombok.Data;
import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author wind
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;
}
