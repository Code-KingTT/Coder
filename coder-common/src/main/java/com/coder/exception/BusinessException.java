package com.coder.exception;

import com.coder.result.ResultCode;

/**
 * 业务异常类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 构造方法 - 使用默认业务异常码
     */
    public BusinessException() {
        super(ResultCode.BUSINESS_ERROR.getMessage());
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.message = ResultCode.BUSINESS_ERROR.getMessage();
    }

    /**
     * 构造方法 - 自定义错误消息
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造方法 - 使用结果码枚举
     *
     * @param resultCode 结果码枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 构造方法 - 自定义错误码和消息
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造方法 - 使用结果码枚举和自定义消息
     *
     * @param resultCode 结果码枚举
     * @param message    自定义错误消息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }

    /**
     * 构造方法 - 包含原始异常
     *
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.message = message;
    }

    /**
     * 构造方法 - 使用结果码枚举和原始异常
     *
     * @param resultCode 结果码枚举
     * @param cause      原始异常
     */
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 构造方法 - 完整参数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置错误消息
     *
     * @param message 错误消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 静态工厂方法 - 创建业务异常
     *
     * @param message 错误消息
     * @return 业务异常实例
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 静态工厂方法 - 使用结果码枚举创建异常
     *
     * @param resultCode 结果码枚举
     * @return 业务异常实例
     */
    public static BusinessException of(ResultCode resultCode) {
        return new BusinessException(resultCode);
    }

    /**
     * 静态工厂方法 - 使用结果码和自定义消息创建异常
     *
     * @param resultCode 结果码枚举
     * @param message    自定义消息
     * @return 业务异常实例
     */
    public static BusinessException of(ResultCode resultCode, String message) {
        return new BusinessException(resultCode, message);
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
