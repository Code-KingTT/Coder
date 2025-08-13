package com.coder.result;

/**
 * 统一返回状态码枚举
 *
 * @author Sunset
 * @date 2025/8/13
 */
public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 访问被拒绝
     */
    FORBIDDEN(403, "访问被拒绝"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(600, "业务异常"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(601, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXISTS(602, "数据不存在"),

    /**
     * 操作失败
     */
    OPERATION_FAILED(603, "操作失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 返回消息
     */
    private final String message;

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 返回消息
     */
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取返回消息
     *
     * @return 返回消息
     */
    public String getMessage() {
        return message;
    }
}
