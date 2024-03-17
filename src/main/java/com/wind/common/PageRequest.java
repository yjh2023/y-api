package com.wind.common;

import lombok.Data;

@Data
public class PageRequest {

    /**
     * 当前页面
     */
    private long currrent = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;
}
