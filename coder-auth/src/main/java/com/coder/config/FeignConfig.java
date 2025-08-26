package com.coder.config;

import com.coder.result.ResultCode;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.coder.exception.BusinessException;

/**
 * Feign全局配置
 */
@Slf4j
@Configuration
public class FeignConfig {

    /**
     * Feign日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 请求拦截器 - 添加通用请求头
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 添加内部服务调用标识
                template.header("X-Internal-Call", "true");
                template.header("X-Service-Name", "coder-auth");

                // 添加请求ID用于链路追踪
                String requestId = generateRequestId();
                template.header("X-Request-Id", requestId);

                log.debug("Feign请求拦截器 - 请求ID: {}, 目标: {}", requestId, template.url());
            }
        };
    }

    /**
     * 超时配置
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                5000,  // 连接超时时间
                10000  // 读取超时时间
        );
    }

    /**
     * 重试配置
     */
    @Bean
    public Retryer retryer() {
        // 最大重试次数3次，初始间隔100ms，最大间隔1000ms
        return new Retryer.Default(100, 1000, 3);
    }

    /**
     * 错误解码器
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, feign.Response response) {
                log.error("Feign调用失败 - 方法: {}, 状态码: {}", methodKey, response.status());

                switch (response.status()) {
                    case 404:
                        return new BusinessException(ResultCode.DATA_NOT_EXISTS, "请求的资源不存在");
                    case 503:
                        return new BusinessException(ResultCode.SERVICE_UNAVAILABLE, "服务暂时不可用");
                    case 500:
                        return new BusinessException(ResultCode.SYSTEM_ERROR, "服务内部错误");
                    default:
                        return new BusinessException(ResultCode.OPERATION_FAILED, "服务调用失败");
                }
            }
        };
    }

    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }
}