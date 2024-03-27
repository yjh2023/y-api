package com.wind.model.request.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息添加请求
 *
 * @author wind
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 接口调用所需接口币
     */
    private Long invokePrice;

    private static final long serialVersionUID = 1L;
}
