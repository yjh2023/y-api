package com.wind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wind.yapicommon.model.domain.UserInterfaceInfo;

import java.util.List;

/**
 * @Entity com.wind.model.domain.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterface(int limit);

}




