package com.wind.model.request.interfaceInfo;

import com.wind.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息查询请求
 *
 * @author wind
 */
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

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
