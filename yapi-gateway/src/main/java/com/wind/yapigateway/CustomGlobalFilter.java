package com.wind.yapigateway;

import com.wind.yapiclientsdk.utils.SignUtils;
import com.wind.yapicommon.model.domain.InterfaceInfo;
import com.wind.yapicommon.model.domain.User;
import com.wind.yapicommon.service.InnerInterfaceInfoService;
import com.wind.yapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤
 *
 * @author wind
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    private static final String INTERFACE_HOST = "http://localhost:8082";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        // 请求日志
        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径: " + path);
        log.info("请求方法: " + method);
        log.info("请求参数: " + request.getQueryParams());
        log.info("请求来源地址: " + request.getLocalAddress());
        log.info("请求远程地址: " + request.getRemoteAddress());
        // 用户鉴权
        HttpHeaders headers = request.getHeaders();
        ServerHttpResponse response = exchange.getResponse();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 到数据库中查询用户的 sk，并校验 sign
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error",e);
        }
        if(invokeUser == null){
            return handleNoAuth(response);
        }
        String genSign = SignUtils.genSign(body, invokeUser.getSecretKey());
        if(!sign.equals(genSign)){
            return handleNoAuth(response);
        }
        if(Long.parseLong(nonce) > 10000L){
            return handleNoAuth(response);
        }
        // 请求时间与当前时间不能相差超过 5 分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES){
            return handleNoAuth(response);
        }
        // 根据请求地址和方法查询接口
        InterfaceInfo invokeInterfaceInfo = null;
        try {
            invokeInterfaceInfo = innerInterfaceInfoService.getInvokeInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInvokeInterfaceInfo error",e);
        }
        if(invokeInterfaceInfo == null){
            return handleNoAuth(response);
        }
        // todo 接口调用统计
        log.info("custom global filter");
        return chain.filter(exchange);
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}