package com.coder.exception;

import com.coder.result.Result;
import com.coder.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 处理与Shiro相关的权限异常
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理Shiro权限不足异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Result<String> handleUnauthorizedException(UnauthorizedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return Result.failed(ResultCode.FORBIDDEN, "权限不足，无法访问该资源");
    }

    /**
     * 处理Shiro认证异常
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public Result<String> handleUnauthenticatedException(UnauthenticatedException e) {
        log.warn("用户未认证: {}", e.getMessage());
        return Result.failed(ResultCode.UNAUTHORIZED, "用户未认证，请先登录");
    }

    /**
     * 处理Shiro授权异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public Result<String> handleAuthorizationException(AuthorizationException e) {
        log.warn("授权失败: {}", e.getMessage());
        return Result.failed(ResultCode.FORBIDDEN, "权限验证失败");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

}