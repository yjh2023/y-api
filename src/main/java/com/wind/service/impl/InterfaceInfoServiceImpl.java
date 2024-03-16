package com.wind.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wind.common.ErrorCode;
import com.wind.exception.BusinessException;
import com.wind.mapper.InterfaceInfoMapper;
import com.wind.model.domain.InterfaceInfo;
import com.wind.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * 接口服务实现类
 *
 * @author wind
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {


    /**
     * 校验接口信息参数
     *
     * @param interfaceInfo
     * @param add
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo,boolean add) {
        if(interfaceInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        // 创建时，名称，接口地址，请求类型必须非空
        if(add){
            if(StringUtils.isAnyBlank(name,url,method)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if(StringUtils.isNotBlank(name) && name.length() > 50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"名称过长");
        }

    }
}




