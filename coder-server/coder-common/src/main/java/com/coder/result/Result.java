package com.coder.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一返回结果封装
 *
 * @param <T> 数据类型
 * @author Sunset
 * @date 2025/8/13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 请求处理时间戳
     */
    private Long timestamp;

    /**
     * 私有构造方法，防止外部实例化
     */
    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 私有构造方法
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    返回数据
     */
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 操作成功，无返回数据
     *
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 操作成功，有返回数据
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 操作成功，自定义消息
     *
     * @param message 自定义消息
     * @param <T>     数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null);
    }

    /**
     * 操作成功，自定义消息和数据
     *
     * @param message 自定义消息
     * @param data    返回数据
     * @param <T>     数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 操作失败，使用默认错误码和消息
     *
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage(), null);
    }

    /**
     * 操作失败，自定义消息
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.SYSTEM_ERROR.getCode(), message, null);
    }

    /**
     * 操作失败，使用状态码枚举
     *
     * @param resultCode 状态码枚举
     * @param <T>        数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 操作失败，自定义状态码和消息
     *
     * @param code    状态码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 操作失败，使用状态码枚举和自定义消息
     *
     * @param resultCode 状态码枚举
     * @param message    自定义消息
     * @param <T>        数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    /**
     * 判断是否成功
     *
     * @return true=成功，false=失败
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, ResultCode.SUCCESS.getCode());
    }

    /**
     * 判断是否失败
     *
     * @return true=失败，false=成功
     */
    public boolean isFailed() {
        return !isSuccess();
    }

    // ================ Getter and Setter ================

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    // ================ Override Methods ================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Result<?> result = (Result<?>) obj;
        return Objects.equals(code, result.code) &&
                Objects.equals(message, result.message) &&
                Objects.equals(data, result.data) &&
                Objects.equals(timestamp, result.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, data, timestamp);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
