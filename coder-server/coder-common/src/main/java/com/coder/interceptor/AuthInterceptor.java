package com.coder.interceptor;

import com.coder.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();

        // 排除不需要认证的接口
        if (isExcludedPath(requestURI)) {
            log.debug("跳过认证的接口: {}", requestURI);
            return true;
        }

        // 检查是否是内部服务调用
        String internalCall = request.getHeader("X-Internal-Call");
        if ("true".equals(internalCall)) {
            log.debug("内部服务调用，跳过用户认证: {}", requestURI);
            UserContext.setCurrentUser("0", "SYSTEM");
            return true;
        }

        // 从网关传递的Header中获取用户信息
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");

        if (userId == null || username == null) {
            log.warn("请求缺少用户信息: {}", request.getRequestURI());
            response.setStatus(401);
            return false;
        }

        UserContext.setCurrentUser(userId, username);
        return true;
    }

    /**
     * 判断是否是排除认证的路径
     */
    private boolean isExcludedPath(String requestURI) {
        String[] excludedPaths = {
                "/auth/login",
                "/auth/register",
                "/auth/logout",
                "/coder/auth/login",
                "/coder/auth/register",
                "/coder/auth/logout",
                "/coder/auth/forgot-password",
                "/coder/auth/reset-password",
                "/coder/auth/send-email-code",
                "/health",
                "/actuator/**",
                // Swagger 相关路径
                "/doc.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/favicon.ico"
        };

        for (String path : excludedPaths) {
            if (path.endsWith("/**")) {
                String prefix = path.substring(0, path.length() - 3);
                if (requestURI.startsWith(prefix)) {
                    return true;
                }
            } else if (requestURI.equals(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理ThreadLocal，避免内存泄漏
        UserContext.clear();
    }
}