package com.coder.filter;

import com.coder.constant.Constants;
import com.coder.utils.JwtUtils;
import com.coder.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GatewayJwtFilter implements GlobalFilter, Ordered {

    @Resource
    private JwtUtils jwtUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisUtils redisUtils;

    // 不需要认证的路径
    private static final List<String> ANONYMOUS_PATHS = Arrays.asList(
            "/coder/auth/login",
            "/coder/auth/register",
            "/coder/auth/forgot-password",
            "/coder/auth/reset-password",
            "/coder/auth/send-email-code",
            "/actuator/health"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否是内部服务调用
        String internalCall = request.getHeaders().getFirst("X-Internal-Call");
        if ("true".equals(internalCall)) {
            log.debug("内部服务调用，跳过认证: {}", path);
            return chain.filter(exchange);
        }

        // 检查是否是匿名路径
        if (isAnonymousPath(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String authHeader = request.getHeaders().getFirst("Authorization");
        String token = jwtUtils.getTokenFromHeader(authHeader);

        if (token == null) {
            return handleUnauthorized(exchange, "缺少认证Token");
        }

        try {
            // 验证Token
            String username = jwtUtils.getUsernameFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);

            if (!jwtUtils.validateToken(token, username)) {
                return handleUnauthorized(exchange, "Token无效或已过期");
            }

            // 检查Redis中的登录状态
            String loginKey = Constants.CacheKey.USER_LOGIN + userId;
            String cachedToken = redisUtils.get(loginKey, String.class);
            if (cachedToken == null || !cachedToken.equals(token)) {
                return handleUnauthorized(exchange, "登录状态已失效");
            }

            // 在请求头中添加用户信息，传递给下游服务
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-Username", username)
                    .header("X-Token", token)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage());
            return handleUnauthorized(exchange, "认证失败");
        }
    }

    private boolean isAnonymousPath(String path) {
        return ANONYMOUS_PATHS.stream().anyMatch(anonymousPath -> 
            path.equals(anonymousPath) || path.startsWith(anonymousPath + "/"));
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());

        try {
            String json = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("序列化响应失败: {}", e.getMessage());
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100; // 优先级高，早执行
    }
}