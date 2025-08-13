package com.coder.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * 加密工具类
 * 提供常用的加密、解密、散列等功能
 * 符合阿里巴巴开发规范
 *
 * @author Sunset
 * @date 2025/8/13
 */
@Slf4j
public final class EncryptUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private EncryptUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 默认字符编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * AES加密算法
     */
    private static final String AES_ALGORITHM = "AES";

    /**
     * AES加密模式
     */
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 安全随机数生成器
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // ================ MD5散列方法 ================

    /**
     * MD5散列
     *
     * @param input 输入字符串
     * @return MD5散列值（32位小写）
     */
    public static String md5(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return md5(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5散列
     *
     * @param input 输入字节数组
     * @return MD5散列值（32位小写）
     */
    public static String md5(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input);
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5算法不存在", e);
            return null;
        }
    }

    /**
     * MD5散列（加盐）
     *
     * @param input 输入字符串
     * @param salt  盐值
     * @return MD5散列值（32位小写）
     */
    public static String md5WithSalt(String input, String salt) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        String saltedInput = input + (salt != null ? salt : "");
        return md5(saltedInput);
    }

    // ================ SHA散列方法 ================

    /**
     * SHA-1散列
     *
     * @param input 输入字符串
     * @return SHA-1散列值（40位小写）
     */
    public static String sha1(String input) {
        return hash(input, "SHA-1");
    }

    /**
     * SHA-256散列
     *
     * @param input 输入字符串
     * @return SHA-256散列值（64位小写）
     */
    public static String sha256(String input) {
        return hash(input, "SHA-256");
    }

    /**
     * SHA-512散列
     *
     * @param input 输入字符串
     * @return SHA-512散列值（128位小写）
     */
    public static String sha512(String input) {
        return hash(input, "SHA-512");
    }

    /**
     * 通用散列方法
     *
     * @param input     输入字符串
     * @param algorithm 散列算法
     * @return 散列值
     */
    private static String hash(String input, String algorithm) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("{}算法不存在", algorithm, e);
            return null;
        }
    }

    // ================ AES对称加密方法 ================

    /**
     * 生成AES密钥
     *
     * @return Base64编码的AES密钥
     */
    public static String generateAesKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(128); // 128位密钥
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            log.error("生成AES密钥失败", e);
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param plainText 明文
     * @param key       Base64编码的密钥
     * @return Base64编码的密文
     */
    public static String aesEncrypt(String plainText, String key) {
        if (StringUtils.isBlank(plainText) || StringUtils.isBlank(key)) {
            return null;
        }
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("AES加密失败: plainText={}", plainText, e);
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param cipherText Base64编码的密文
     * @param key        Base64编码的密钥
     * @return 明文
     */
    public static String aesDecrypt(String cipherText, String key) {
        if (StringUtils.isBlank(cipherText) || StringUtils.isBlank(key)) {
            return null;
        }
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密失败: cipherText={}", cipherText, e);
            return null;
        }
    }

    // ================ Base64编码方法 ================

    /**
     * Base64编码
     *
     * @param input 输入字符串
     * @return Base64编码字符串
     */
    public static String base64Encode(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64编码
     *
     * @param input 输入字节数组
     * @return Base64编码字符串
     */
    public static String base64Encode(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input);
    }

    /**
     * Base64解码
     *
     * @param input Base64编码字符串
     * @return 解码后的字符串
     */
    public static String base64Decode(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(input);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Base64解码失败: input={}", input, e);
            return null;
        }
    }

    /**
     * Base64解码为字节数组
     *
     * @param input Base64编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64DecodeToBytes(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            return Base64.getDecoder().decode(input);
        } catch (Exception e) {
            log.error("Base64解码为字节数组失败: input={}", input, e);
            return null;
        }
    }

    // ================ 随机数生成方法 ================

    /**
     * 生成指定长度的随机数字字符串
     *
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        return StringUtils.randomNumeric(length);
    }

    /**
     * 生成指定长度的随机字母字符串
     *
     * @param length 长度
     * @return 随机字母字符串
     */
    public static String randomAlphabetic(int length) {
        return StringUtils.randomAlphabetic(length);
    }

    /**
     * 生成指定长度的随机字母数字字符串
     *
     * @param length 长度
     * @return 随机字母数字字符串
     */
    public static String randomAlphanumeric(int length) {
        return StringUtils.randomAlphanumeric(length);
    }

    /**
     * 生成安全的随机字节数组
     *
     * @param length 长度
     * @return 随机字节数组
     */
    public static byte[] randomBytes(int length) {
        if (length <= 0) {
            return new byte[0];
        }
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * 生成安全的随机整数
     *
     * @param bound 上界（不包含）
     * @return 随机整数
     */
    public static int randomInt(int bound) {
        return SECURE_RANDOM.nextInt(bound);
    }

    /**
     * 生成安全的随机长整数
     *
     * @return 随机长整数
     */
    public static long randomLong() {
        return SECURE_RANDOM.nextLong();
    }

    // ================ UUID生成方法 ================

    /**
     * 生成标准UUID
     *
     * @return UUID字符串（包含连字符）
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成简化UUID（去除连字符）
     *
     * @return 简化UUID字符串
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成大写UUID
     *
     * @return 大写UUID字符串
     */
    public static String upperUuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 生成简化大写UUID
     *
     * @return 简化大写UUID字符串
     */
    public static String simpleUpperUuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    // ================ 盐值生成方法 ================

    /**
     * 生成随机盐值
     *
     * @param length 盐值长度
     * @return 盐值字符串
     */
    public static String generateSalt(int length) {
        return randomAlphanumeric(length);
    }

    /**
     * 生成随机盐值（默认16位）
     *
     * @return 盐值字符串
     */
    public static String generateSalt() {
        return generateSalt(16);
    }

    /**
     * 生成Base64编码的盐值
     *
     * @param byteLength 字节长度
     * @return Base64编码的盐值
     */
    public static String generateBase64Salt(int byteLength) {
        byte[] salt = randomBytes(byteLength);
        return Base64.getEncoder().encodeToString(salt);
    }

    // ================ 密码相关方法 ================

    /**
     * 生成安全密码
     *
     * @param length 密码长度
     * @return 安全密码
     */
    public static String generateSecurePassword(int length) {
        if (length < 8) {
            length = 8; // 最小8位
        }
        
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String allChars = uppercase + lowercase + numbers + symbols;
        
        StringBuilder password = new StringBuilder();
        
        // 确保至少包含每种类型的字符
        password.append(uppercase.charAt(randomInt(uppercase.length())));
        password.append(lowercase.charAt(randomInt(lowercase.length())));
        password.append(numbers.charAt(randomInt(numbers.length())));
        password.append(symbols.charAt(randomInt(symbols.length())));
        
        // 填充剩余长度
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(randomInt(allChars.length())));
        }
        
        // 打乱字符顺序
        return shuffleString(password.toString());
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 强度等级：0-弱，1-中等，2-强
     */
    public static int checkPasswordStrength(String password) {
        if (StringUtils.isBlank(password)) {
            return 0;
        }
        
        int score = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            score++;
        }
        if (password.length() >= 12) {
            score++;
        }
        
        // 字符类型检查
        if (password.matches(".*[a-z].*")) {
            score++; // 包含小写字母
        }
        if (password.matches(".*[A-Z].*")) {
            score++; // 包含大写字母
        }
        if (password.matches(".*[0-9].*")) {
            score++; // 包含数字
        }
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*")) {
            score++; // 包含特殊字符
        }
        
        // 返回强度等级
        if (score <= 2) {
            return 0; // 弱
        } else if (score <= 4) {
            return 1; // 中等
        } else {
            return 2; // 强
        }
    }

    // ================ 工具方法 ================

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串（小写）
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * 打乱字符串中字符的顺序
     *
     * @param input 输入字符串
     * @return 打乱后的字符串
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = randomInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    /**
     * 验证两个散列值是否相等（防时序攻击）
     *
     * @param hash1 散列值1
     * @param hash2 散列值2
     * @return true-相等，false-不相等
     */
    public static boolean constantTimeEquals(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return hash1 == hash2;
        }
        
        if (hash1.length() != hash2.length()) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < hash1.length(); i++) {
            result |= hash1.charAt(i) ^ hash2.charAt(i);
        }
        return result == 0;
    }

    // ================ 令牌生成方法 ================

    /**
     * 生成访问令牌
     *
     * @return 访问令牌
     */
    public static String generateAccessToken() {
        return base64Encode(simpleUuid() + System.currentTimeMillis());
    }

    /**
     * 生成刷新令牌
     *
     * @return 刷新令牌
     */
    public static String generateRefreshToken() {
        return sha256(simpleUuid() + System.currentTimeMillis() + randomAlphanumeric(16));
    }

    /**
     * 生成验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public static String generateVerificationCode(int length) {
        return randomNumeric(length);
    }

    /**
     * 生成验证码（默认6位）
     *
     * @return 6位验证码
     */
    public static String generateVerificationCode() {
        return generateVerificationCode(6);
    }
}
