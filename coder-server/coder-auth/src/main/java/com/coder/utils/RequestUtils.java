package com.coder.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类
 * <p>
 * 提供HTTP请求相关的工具方法，包括获取客户端IP、请求头信息等
 * </p>
 *
 * @author Sunset
 * @since 2025-08-24
 */
@Slf4j
public class RequestUtils {

    /** JSON对象映射器 */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** 未知IP标识 */
    private static final String UNKNOWN = "unknown";

    /** 本地回环地址 */
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端真实IP地址
     * <p>
     * 优先级：X-Forwarded-For > X-Real-IP > Proxy-Client-IP > WL-Proxy-Client-IP > 
     * HTTP_CLIENT_IP > HTTP_X_FORWARDED_FOR > RemoteAddr
     * </p>
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            log.warn("获取客户端IP失败：请求对象为空");
            return UNKNOWN;
        }

        String ip = getIpFromHeader(request, "X-Forwarded-For");
        if (isValidIp(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            return getFirstIp(ip);
        }

        ip = getIpFromHeader(request, "X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = getIpFromHeader(request, "Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = getIpFromHeader(request, "WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = getIpFromHeader(request, "HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = getIpFromHeader(request, "HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        log.debug("获取到客户端IP：{}", ip);
        return ip;
    }

    /**
     * 获取代理IP地址
     *
     * @param request HTTP请求对象
     * @return 代理IP地址
     */
    public static String getProxyIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty() && !UNKNOWN.equalsIgnoreCase(forwardedFor)) {
            // 如果有多个IP，取第二个作为代理IP
            String[] ips = forwardedFor.split(",");
            if (ips.length > 1) {
                return ips[1].trim();
            }
        }
        return null;
    }

    /**
     * 获取所有请求头信息并转换为JSON字符串
     *
     * @param request HTTP请求对象
     * @return JSON格式的请求头信息
     */
    public static String getHeadersAsJson(HttpServletRequest request) {
        if (request == null) {
            log.warn("获取请求头失败：请求对象为空");
            return "{}";
        }

        try {
            Map<String, String> headers = new HashMap<>(16);
            Enumeration<String> headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
            }

            return OBJECT_MAPPER.writeValueAsString(headers);
        } catch (Exception e) {
            log.error("转换请求头为JSON失败", e);
            return "{}";
        }
    }

    /**
     * 从请求头中获取IP地址
     *
     * @param request HTTP请求对象
     * @param headerName 请求头名称
     * @return IP地址
     */
    private static String getIpFromHeader(HttpServletRequest request, String headerName) {
        String ip = request.getHeader(headerName);
        log.debug("从请求头 {} 获取IP：{}", headerName, ip);
        return ip;
    }

    /**
     * 验证IP地址是否有效
     *
     * @param ip IP地址
     * @return 是否有效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 从IP列表中获取第一个IP
     *
     * @param ipList IP列表，多个IP用逗号分隔
     * @return 第一个IP地址
     */
    private static String getFirstIp(String ipList) {
        if (ipList == null || ipList.isEmpty()) {
            return ipList;
        }

        int index = ipList.indexOf(',');
        if (index != -1) {
            return ipList.substring(0, index).trim();
        }
        return ipList.trim();
    }
}