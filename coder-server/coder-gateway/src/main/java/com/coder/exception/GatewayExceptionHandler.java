package com.coder.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关全局异常处理器
 */
@Slf4j
@Order(-1)
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // 根据异常类型处理
        if (ex instanceof NotFoundException) {
            return handleNotFoundException(response, ex);
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            return handleResponseStatusException(response, responseStatusException);
        } else {
            return handleGenericException(response, ex);
        }
    }

    /**
     * 处理服务不可用异常
     */
    private Mono<Void> handleNotFoundException(ServerHttpResponse response, Throwable ex) {
        response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", "服务暂时不可用，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        
        log.warn("服务不可用: {}", ex.getMessage());
        return writeResponse(response, result);
    }

    /**
     * 处理响应状态异常
     */
    private Mono<Void> handleResponseStatusException(ServerHttpResponse response, ResponseStatusException ex) {
        response.setStatusCode(ex.getStatus());
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", ex.getStatus().value());
        
        // 自定义不同状态码的消息
        String message = getCustomMessage(ex.getStatus(), ex.getReason());
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        
        log.warn("响应状态异常: {} - {}", ex.getStatus(), ex.getReason());
        return writeResponse(response, result);
    }

    /**
     * 处理通用异常
     */
    private Mono<Void> handleGenericException(ServerHttpResponse response, Throwable ex) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "网关内部错误");
        result.put("timestamp", System.currentTimeMillis());
        
        log.error("网关异常: ", ex);
        return writeResponse(response, result);
    }

    /**
     * 获取自定义错误消息
     */
    private String getCustomMessage(HttpStatus status, String reason) {
        switch (status) {
            case SERVICE_UNAVAILABLE:
                return "服务暂时不可用，请稍后重试";
            case NOT_FOUND:
                return "请求的服务不存在";
            case GATEWAY_TIMEOUT:
                return "服务响应超时";
            case BAD_GATEWAY:
                return "网关错误";
            default:
                return reason != null ? reason : "请求处理失败";
        }
    }

    /**
     * 写入响应
     */
    private Mono<Void> writeResponse(ServerHttpResponse response, Map<String, Object> result) {
        try {
            String json = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("序列化响应失败: {}", e.getMessage());
            return response.setComplete();
        }
    }
}