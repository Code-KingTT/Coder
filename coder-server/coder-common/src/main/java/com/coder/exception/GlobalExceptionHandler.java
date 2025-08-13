package com.coder.exception;

import com.coder.result.Result;
import com.coder.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author Sunset
 * @date 2025/8/13
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: URI=[{}], 错误码=[{}], 错误信息=[{}]", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 - @RequestBody 参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数校验异常: URI=[{}]", request.getRequestURI());
        
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        
        String message = errorMsg.length() > 0 ? errorMsg.substring(0, errorMsg.length() - 2) : "参数校验失败";
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理参数绑定异常 - @ModelAttribute 参数校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定异常: URI=[{}]", request.getRequestURI());
        
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        
        String message = errorMsg.length() > 0 ? errorMsg.substring(0, errorMsg.length() - 2) : "参数绑定失败";
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理路径变量校验异常 - @PathVariable 和 @RequestParam 参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("约束校验异常: URI=[{}]", request.getRequestURI());
        
        StringBuilder errorMsg = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errorMsg.append(fieldName)
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        }
        
        String message = errorMsg.length() > 0 ? errorMsg.substring(0, errorMsg.length() - 2) : "参数校验失败";
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("缺少请求参数异常: URI=[{}], 参数名=[{}]", request.getRequestURI(), e.getParameterName());
        String message = String.format("缺少必需的请求参数: %s", e.getParameterName());
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("参数类型不匹配异常: URI=[{}], 参数名=[{}], 期望类型=[{}], 实际值=[{}]", 
                request.getRequestURI(), e.getName(), e.getRequiredType(), e.getValue());
        String message = String.format("参数 %s 类型不正确，期望类型: %s", e.getName(), e.getRequiredType().getSimpleName());
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理HTTP消息不可读异常 - JSON格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("HTTP消息不可读异常: URI=[{}], 错误信息=[{}]", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR, "请求参数格式错误，请检查JSON格式");
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("请求方法不支持异常: URI=[{}], 方法=[{}], 支持的方法=[{}]", 
                request.getRequestURI(), e.getMethod(), e.getSupportedHttpMethods());
        String message = String.format("请求方法 %s 不支持，支持的方法: %s", e.getMethod(), e.getSupportedHttpMethods());
        return Result.failed(ResultCode.METHOD_NOT_ALLOWED, message);
    }

    /**
     * 处理媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.warn("媒体类型不支持异常: URI=[{}], Content-Type=[{}], 支持的类型=[{}]", 
                request.getRequestURI(), e.getContentType(), e.getSupportedMediaTypes());
        String message = String.format("不支持的媒体类型: %s", e.getContentType());
        return Result.failed(ResultCode.PARAM_ERROR, message);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("404异常: URI=[{}], 请求方法=[{}]", request.getRequestURI(), e.getHttpMethod());
        String message = String.format("请求的资源不存在: %s %s", e.getHttpMethod(), e.getRequestURL());
        return Result.failed(ResultCode.NOT_FOUND, message);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常: URI=[{}], 错误信息=[{}]", request.getRequestURI(), e.getMessage());
        return Result.failed(ResultCode.PARAM_ERROR, e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: URI=[{}]", request.getRequestURI(), e);
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统内部错误，请联系管理员");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI=[{}]", request.getRequestURI(), e);
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统内部错误，请联系管理员");
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("未知异常: URI=[{}], 异常类型=[{}]", request.getRequestURI(), e.getClass().getSimpleName(), e);
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统内部错误，请联系管理员");
    }
}
