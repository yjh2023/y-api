package com.wind.model.vo;

import com.wind.yapicommon.model.domain.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    /**
     * 总调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
