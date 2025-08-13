package com.coder.utils;

import com.coder.exception.BusinessException;
import com.coder.result.ResultCode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * 异常工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public class ExceptionUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private ExceptionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 断言对象不为空，为空则抛出业务异常
     *
     * @param object  待检查对象
     * @param message 异常消息
     * @throws BusinessException 对象为空时抛出
     */
    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言对象不为空，为空则抛出业务异常
     *
     * @param object     待检查对象
     * @param resultCode 结果码枚举
     * @throws BusinessException 对象为空时抛出
     */
    public static void assertNotNull(Object object, ResultCode resultCode) {
        if (object == null) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 断言对象不为空，为空则抛出业务异常
     *
     * @param object     待检查对象
     * @param resultCode 结果码枚举
     * @param message    自定义消息
     * @throws BusinessException 对象为空时抛出
     */
    public static void assertNotNull(Object object, ResultCode resultCode, String message) {
        if (object == null) {
            throw new BusinessException(resultCode, message);
        }
    }

    /**
     * 断言字符串不为空，为空则抛出业务异常
     *
     * @param str     待检查字符串
     * @param message 异常消息
     * @throws BusinessException 字符串为空时抛出
     */
    public static void assertNotEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言字符串不为空，为空则抛出业务异常
     *
     * @param str        待检查字符串
     * @param resultCode 结果码枚举
     * @throws BusinessException 字符串为空时抛出
     */
    public static void assertNotEmpty(String str, ResultCode resultCode) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 断言条件为真，为假则抛出业务异常
     *
     * @param condition 待检查条件
     * @param message   异常消息
     * @throws BusinessException 条件为假时抛出
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言条件为真，为假则抛出业务异常
     *
     * @param condition  待检查条件
     * @param resultCode 结果码枚举
     * @throws BusinessException 条件为假时抛出
     */
    public static void assertTrue(boolean condition, ResultCode resultCode) {
        if (!condition) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 断言条件为真，为假则抛出业务异常
     *
     * @param condition  待检查条件
     * @param resultCode 结果码枚举
     * @param message    自定义消息
     * @throws BusinessException 条件为假时抛出
     */
    public static void assertTrue(boolean condition, ResultCode resultCode, String message) {
        if (!condition) {
            throw new BusinessException(resultCode, message);
        }
    }

    /**
     * 断言条件为假，为真则抛出业务异常
     *
     * @param condition 待检查条件
     * @param message   异常消息
     * @throws BusinessException 条件为真时抛出
     */
    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言条件为假，为真则抛出业务异常
     *
     * @param condition  待检查条件
     * @param resultCode 结果码枚举
     * @throws BusinessException 条件为真时抛出
     */
    public static void assertFalse(boolean condition, ResultCode resultCode) {
        if (condition) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 断言两个对象相等，不等则抛出业务异常
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param message  异常消息
     * @throws BusinessException 对象不相等时抛出
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言两个对象相等，不等则抛出业务异常
     *
     * @param expected   期望值
     * @param actual     实际值
     * @param resultCode 结果码枚举
     * @throws BusinessException 对象不相等时抛出
     */
    public static void assertEquals(Object expected, Object actual, ResultCode resultCode) {
        if (!Objects.equals(expected, actual)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 抛出业务异常的便捷方法
     *
     * @param message 异常消息
     * @throws BusinessException 业务异常
     */
    public static void throwBusinessException(String message) {
        throw new BusinessException(message);
    }

    /**
     * 抛出业务异常的便捷方法
     *
     * @param resultCode 结果码枚举
     * @throws BusinessException 业务异常
     */
    public static void throwBusinessException(ResultCode resultCode) {
        throw new BusinessException(resultCode);
    }

    /**
     * 抛出业务异常的便捷方法
     *
     * @param resultCode 结果码枚举
     * @param message    自定义消息
     * @throws BusinessException 业务异常
     */
    public static void throwBusinessException(ResultCode resultCode, String message) {
        throw new BusinessException(resultCode, message);
    }

    /**
     * 获取异常的完整堆栈信息
     *
     * @param throwable 异常对象
     * @return 堆栈信息字符串
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * 获取异常的根本原因
     *
     * @param throwable 异常对象
     * @return 根本异常
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    /**
     * 获取异常链中第一个指定类型的异常
     *
     * @param throwable     异常对象
     * @param exceptionType 异常类型
     * @param <T>          异常类型泛型
     * @return 指定类型的异常，如果没有找到则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getException(Throwable throwable, Class<T> exceptionType) {
        if (throwable == null || exceptionType == null) {
            return null;
        }
        
        Throwable current = throwable;
        while (current != null) {
            if (exceptionType.isInstance(current)) {
                return (T) current;
            }
            current = current.getCause();
            // 避免循环引用
            if (current == throwable) {
                break;
            }
        }
        return null;
    }

    /**
     * 判断异常链中是否包含指定类型的异常
     *
     * @param throwable     异常对象
     * @param exceptionType 异常类型
     * @return true=包含，false=不包含
     */
    public static boolean containsException(Throwable throwable, Class<? extends Throwable> exceptionType) {
        return getException(throwable, exceptionType) != null;
    }
}
