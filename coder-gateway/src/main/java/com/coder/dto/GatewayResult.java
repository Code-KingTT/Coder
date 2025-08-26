package com.coder.dto;

import lombok.Data;

/**
 * 网关统一响应格式
 */
@Data
public class GatewayResult<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> GatewayResult<T> success(T data) {
        GatewayResult<T> result = new GatewayResult<>();
        result.setCode(200);
        result.setMessage("请求成功");
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> GatewayResult<T> failed(Integer code, String message) {
        GatewayResult<T> result = new GatewayResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> GatewayResult<T> serviceUnavailable() {
        return failed(503, "服务暂时不可用，请稍后重试");
    }
}