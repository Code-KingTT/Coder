package com.coder.utils;

import com.coder.constant.Constants;

import java.util.Collection;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public final class StrUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private StrUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 空格字符串
     */
    public static final String SPACE = " ";

    /**
     * 逗号分隔符
     */
    public static final String COMMA = ",";

    /**
     * 点分隔符
     */
    public static final String DOT = ".";

    /**
     * 下划线分隔符
     */
    public static final String UNDERSCORE = "_";

    /**
     * 中横线分隔符
     */
    public static final String DASH = "-";

    // ================ 基础判断方法 ================

    /**
     * 判断字符串是否为空（null或空字符串）
     *
     * @param str 字符串
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白（null、空字符串或只包含空白字符）
     *
     * @param str 字符串
     * @return true-为空白，false-不为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 字符串
     * @return true-不为空白，false-为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断多个字符串是否都不为空
     *
     * @param strs 字符串数组
     * @return true-都不为空，false-存在空字符串
     */
    public static boolean isAllNotEmpty(String... strs) {
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断多个字符串是否都不为空白
     *
     * @param strs 字符串数组
     * @return true-都不为空白，false-存在空白字符串
     */
    public static boolean isAllNotBlank(String... strs) {
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (isBlank(str)) {
                return false;
            }
        }
        return true;
    }

    // ================ 默认值处理方法 ================

    /**
     * 如果字符串为空，返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 非空字符串或默认值
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 如果字符串为空白，返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 非空白字符串或默认值
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    // ================ 字符串操作方法 ================

    /**
     * 去除字符串首尾空白字符
     *
     * @param str 字符串
     * @return 去除空白后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 安全的trim，null返回空字符串
     *
     * @param str 字符串
     * @return 去除空白后的字符串，null返回空字符串
     */
    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param str 字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    // ================ 字符串比较方法 ================

    /**
     * 安全的字符串相等比较
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等，false-不相等
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * 忽略大小写的字符串相等比较
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等，false-不相等
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    // ================ 字符串包含方法 ================

    /**
     * 判断字符串是否包含子字符串
     *
     * @param str    字符串
     * @param subStr 子字符串
     * @return true-包含，false-不包含
     */
    public static boolean contains(String str, String subStr) {
        if (str == null || subStr == null) {
            return false;
        }
        return str.contains(subStr);
    }

    /**
     * 忽略大小写判断字符串是否包含子字符串
     *
     * @param str    字符串
     * @param subStr 子字符串
     * @return true-包含，false-不包含
     */
    public static boolean containsIgnoreCase(String str, String subStr) {
        if (str == null || subStr == null) {
            return false;
        }
        return str.toLowerCase().contains(subStr.toLowerCase());
    }

    // ================ 字符串替换方法 ================

    /**
     * 替换字符串中的所有目标字符串
     *
     * @param str         原字符串
     * @param target      目标字符串
     * @param replacement 替换字符串
     * @return 替换后的字符串
     */
    public static String replace(String str, String target, String replacement) {
        if (isEmpty(str) || isEmpty(target)) {
            return str;
        }
        return str.replace(target, replacement == null ? EMPTY : replacement);
    }

    // ================ 字符串拼接方法 ================

    /**
     * 使用指定分隔符拼接字符串数组
     *
     * @param separator 分隔符
     * @param strs      字符串数组
     * @return 拼接后的字符串
     */
    public static String join(String separator, String... strs) {
        if (strs == null || strs.length == 0) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(strs[i] == null ? EMPTY : strs[i]);
        }
        return sb.toString();
    }

    /**
     * 使用指定分隔符拼接集合
     *
     * @param separator 分隔符
     * @param collection 集合
     * @return 拼接后的字符串
     */
    public static String join(String separator, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object obj : collection) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(obj == null ? EMPTY : obj.toString());
            first = false;
        }
        return sb.toString();
    }

    // ================ 字符串分割方法 ================

    /**
     * 使用指定分隔符分割字符串
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 分割后的字符串数组
     */
    public static String[] split(String str, String separator) {
        if (isEmpty(str)) {
            return new String[0];
        }
        if (isEmpty(separator)) {
            return new String[]{str};
        }
        return str.split(Pattern.quote(separator));
    }

    // ================ 正则表达式验证方法 ================

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return true-有效，false-无效
     */
    public static boolean isValidPhone(String phone) {
        return isNotBlank(phone) && Pattern.matches(Constants.Regex.PHONE, phone);
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return true-有效，false-无效
     */
    public static boolean isValidEmail(String email) {
        return isNotBlank(email) && Pattern.matches(Constants.Regex.EMAIL, email);
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return true-有效，false-无效
     */
    public static boolean isValidUsername(String username) {
        if (isBlank(username)) {
            return false;
        }
        return username.length() >= Constants.User.USERNAME_MIN_LENGTH &&
               username.length() <= Constants.User.USERNAME_MAX_LENGTH &&
               Pattern.matches(Constants.Regex.USERNAME, username);
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return true-有效，false-无效
     */
    public static boolean isValidPassword(String password) {
        if (isBlank(password)) {
            return false;
        }
        return password.length() >= Constants.User.PASSWORD_MIN_LENGTH &&
               password.length() <= Constants.User.PASSWORD_MAX_LENGTH &&
               Pattern.matches(Constants.Regex.PASSWORD, password);
    }

    /**
     * 验证身份证号
     *
     * @param idCard 身份证号
     * @return true-有效，false-无效
     */
    public static boolean isValidIdCard(String idCard) {
        return isNotBlank(idCard) && Pattern.matches(Constants.Regex.ID_CARD, idCard);
    }

    /**
     * 验证是否为纯中文
     *
     * @param str 字符串
     * @return true-纯中文，false-不是纯中文
     */
    public static boolean isChinese(String str) {
        return isNotBlank(str) && Pattern.matches(Constants.Regex.CHINESE, str);
    }

    /**
     * 验证IP地址
     *
     * @param ip IP地址
     * @return true-有效，false-无效
     */
    public static boolean isValidIp(String ip) {
        return isNotBlank(ip) && Pattern.matches(Constants.Regex.IP_ADDRESS, ip);
    }

    // ================ 随机字符串生成方法 ================

    /**
     * 数字字符
     */
    private static final String NUMBERS = "0123456789";

    /**
     * 小写字母
     */
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 大写字母
     */
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 字母
     */
    private static final String LETTERS = LOWERCASE + UPPERCASE;

    /**
     * 字母和数字
     */
    private static final String ALPHANUMERIC = LETTERS + NUMBERS;

    /**
     * 随机数生成器
     */
    private static final Random RANDOM = new Random();

    /**
     * 生成随机数字字符串
     *
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        return randomString(NUMBERS, length);
    }

    /**
     * 生成随机字母字符串
     *
     * @param length 长度
     * @return 随机字母字符串
     */
    public static String randomAlphabetic(int length) {
        return randomString(LETTERS, length);
    }

    /**
     * 生成随机字母数字字符串
     *
     * @param length 长度
     * @return 随机字母数字字符串
     */
    public static String randomAlphanumeric(int length) {
        return randomString(ALPHANUMERIC, length);
    }

    /**
     * 从指定字符集生成随机字符串
     *
     * @param chars  字符集
     * @param length 长度
     * @return 随机字符串
     */
    public static String randomString(String chars, int length) {
        if (isEmpty(chars) || length <= 0) {
            return EMPTY;
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // ================ 字符串格式化方法 ================

    /**
     * 格式化字符串，使用 {} 作为占位符
     *
     * @param template 模板字符串
     * @param args     参数
     * @return 格式化后的字符串
     */
    public static String format(String template, Object... args) {
        if (isEmpty(template) || args == null || args.length == 0) {
            return template;
        }
        
        String result = template;
        for (Object arg : args) {
            result = result.replaceFirst("\\{\\}", arg == null ? "null" : arg.toString());
        }
        return result;
    }

    // ================ 字符串脱敏方法 ================

    /**
     * 手机号脱敏
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (isBlank(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 邮箱脱敏
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        
        if (localPart.length() <= 2) {
            return email;
        }
        
        String maskedLocal = localPart.substring(0, 1) + "***" + localPart.substring(localPart.length() - 1);
        return maskedLocal + "@" + domain;
    }

    /**
     * 身份证号脱敏
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (isBlank(idCard) || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 银行卡号脱敏
     *
     * @param bankCard 银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String maskBankCard(String bankCard) {
        if (isBlank(bankCard) || bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }
}
