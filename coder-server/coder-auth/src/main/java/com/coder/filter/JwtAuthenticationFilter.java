package com.coder.filter;

import com.coder.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT认证过滤器
 * 
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends AuthenticatingFilter {

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 创建AuthenticationToken
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader(jwtUtils.getHeaderName());
        String token = jwtUtils.getTokenFromHeader(authHeader);
        
        if (token != null) {
            return new JwtToken(token);
        }
        return null;
    }

    /**
     * 判断是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 检查是否在网关已验证过JWT
        String userIdFromGateway = httpRequest.getHeader("X-User-Id");
        if (userIdFromGateway != null) {
            return true;
        }

        String requestURI = httpRequest.getRequestURI();

        // 检查是否是OPTIONS请求，如果是则直接放行
        if (isOptionsRequest(request)) {
            return true;
        }

        // 检查是否是匿名访问路径，如果是则直接放行
        if (isAnonymousPath(requestURI)) {
            return true;
        }

        // 检查是否有Token
        if (getAuthzHeader(request) != null) {
            try {
                // 尝试执行登录
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                log.debug("JWT认证失败: {}", e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * 检查是否是匿名访问路径
     */
    private boolean isAnonymousPath(String requestURI) {
        String[] anonymousPaths = {
                "/coder/auth/login",
                "/coder/auth/register",
                "/coder/auth/forgot-password",
                "/coder/auth/reset-password",
                "/coder/auth/send-email-code",
                "/static/",
                "/favicon.ico",
                "/swagger-ui/",
                "/swagger-resources/",
                "/swagger-resources",
                "/webjars/",
                "/v2/api-docs",
                "/doc.html",
                "/actuator/",
                "/health"
        };

        for (String path : anonymousPaths) {
            if (requestURI.equals(path) ||
                    (path.endsWith("/") && requestURI.startsWith(path)) ||
                    (!path.endsWith("/") && requestURI.startsWith(path + "/"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 访问拒绝时调用
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 对于OPTIONS请求，直接返回成功
        if (isOptionsRequest(request)) {
            return true;
        }

        // 返回401未授权
        return onLoginFail(response);
    }

    /**
     * 登录失败处理
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, 
                                   ServletRequest request, ServletResponse response) {
        log.debug("JWT登录失败: {}", e.getMessage());
        return onLoginFail(response);
    }

    /**
     * 处理登录失败
     */
    private boolean onLoginFail(ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 401);
            result.put("message", "未授权，请先登录");
            result.put("timestamp", System.currentTimeMillis());
            
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            httpResponse.getWriter().write(json);
        } catch (IOException e) {
            log.error("写入响应失败: {}", e.getMessage());
        }
        
        return false;
    }

    /**
     * 获取认证header
     */
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(jwtUtils.getHeaderName());
    }

    /**
     * 判断是否是OPTIONS请求
     */
    private boolean isOptionsRequest(ServletRequest request) {
        return WebUtils.toHttp(request).getMethod().equals(RequestMethod.OPTIONS.name());
    }
}