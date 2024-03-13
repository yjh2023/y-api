package com.wind.exception;

import com.wind.common.BaseResponse;
import com.wind.common.ErrorCode;
import com.wind.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHander(BusinessException e){
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> businessExceptionHander(RuntimeException e){
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
