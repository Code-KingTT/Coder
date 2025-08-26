package com.coder.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User-Agent解析工具类
 * <p>
 * 用于解析HTTP请求中的User-Agent字符串，提取浏览器、操作系统、设备等信息
 * </p>
 *
 * @author Sunset
 * @since 2025-08-24
 */
@Slf4j
public class UserAgentUtils {

    /**
     * 解析User-Agent字符串
     *
     * @param userAgent User-Agent字符串
     * @return 解析结果对象，包含浏览器、操作系统、设备等信息
     */
    public static UserAgentInfo parseUserAgent(String userAgent) {
        UserAgentInfo info = new UserAgentInfo();

        if (userAgent == null || userAgent.trim().isEmpty()) {
            log.debug("User-Agent字符串为空，返回默认信息");
            return info;
        }

        try {
            // 解析浏览器信息
            parseBrowserInfo(userAgent, info);

            // 解析操作系统信息
            parseOperatingSystemInfo(userAgent, info);

            // 解析设备信息
            parseDeviceInfo(userAgent, info);

            log.debug("User-Agent解析完成：{}", info);
        } catch (Exception e) {
            log.error("解析User-Agent失败：{}", userAgent, e);
        }

        return info;
    }

    /**
     * 解析浏览器信息
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseBrowserInfo(String userAgent, UserAgentInfo info) {
        // Chrome浏览器
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            info.setBrowserName("Chrome");
            info.setBrowserEngine("Blink");
            extractVersion(userAgent, "Chrome/([\\d.]+)", info::setBrowserVersion);
        }
        // Firefox浏览器
        else if (userAgent.contains("Firefox")) {
            info.setBrowserName("Firefox");
            info.setBrowserEngine("Gecko");
            extractVersion(userAgent, "Firefox/([\\d.]+)", info::setBrowserVersion);
        }
        // Safari浏览器（排除Chrome）
        else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            info.setBrowserName("Safari");
            info.setBrowserEngine("WebKit");
            extractVersion(userAgent, "Version/([\\d.]+)", info::setBrowserVersion);
        }
        // Edge浏览器
        else if (userAgent.contains("Edg")) {
            info.setBrowserName("Edge");
            info.setBrowserEngine("Blink");
            extractVersion(userAgent, "Edg/([\\d.]+)", info::setBrowserVersion);
        }
        // Internet Explorer
        else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            info.setBrowserName("Internet Explorer");
            info.setBrowserEngine("Trident");
            extractVersion(userAgent, "(?:MSIE |rv:)([\\d.]+)", info::setBrowserVersion);
        }
        // Opera浏览器
        else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            info.setBrowserName("Opera");
            info.setBrowserEngine("Blink");
            extractVersion(userAgent, "(?:Opera|OPR)/([\\d.]+)", info::setBrowserVersion);
        }
    }

    /**
     * 解析操作系统信息
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseOperatingSystemInfo(String userAgent, UserAgentInfo info) {
        // Windows操作系统
        if (userAgent.contains("Windows")) {
            info.setOsName("Windows");
            parseWindowsVersion(userAgent, info);
            parseWindowsArchitecture(userAgent, info);
        }
        // macOS操作系统
        else if (userAgent.contains("Mac OS X")) {
            info.setOsName("macOS");
            extractVersion(userAgent, "Mac OS X ([\\d_]+)", version -> 
                info.setOsVersion(version.replace("_", ".")));
            info.setOsArch("x64");
        }
        // Linux操作系统
        else if (userAgent.contains("Linux")) {
            info.setOsName("Linux");
            parseLinuxArchitecture(userAgent, info);
        }
        // Android操作系统
        else if (userAgent.contains("Android")) {
            info.setOsName("Android");
            extractVersion(userAgent, "Android ([\\d.]+)", info::setOsVersion);
        }
        // iOS操作系统
        else if (userAgent.contains("iPhone OS") || userAgent.contains("OS ")) {
            info.setOsName("iOS");
            extractVersion(userAgent, "OS ([\\d_]+)", version -> 
                info.setOsVersion(version.replace("_", ".")));
        }
    }

    /**
     * 解析Windows版本
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseWindowsVersion(String userAgent, UserAgentInfo info) {
        if (userAgent.contains("Windows NT 10.0")) {
            info.setOsVersion("10");
        } else if (userAgent.contains("Windows NT 6.3")) {
            info.setOsVersion("8.1");
        } else if (userAgent.contains("Windows NT 6.2")) {
            info.setOsVersion("8");
        } else if (userAgent.contains("Windows NT 6.1")) {
            info.setOsVersion("7");
        } else if (userAgent.contains("Windows NT 6.0")) {
            info.setOsVersion("Vista");
        } else if (userAgent.contains("Windows NT 5.1")) {
            info.setOsVersion("XP");
        }
    }

    /**
     * 解析Windows架构
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseWindowsArchitecture(String userAgent, UserAgentInfo info) {
        if (userAgent.contains("WOW64") || userAgent.contains("Win64") || userAgent.contains("x64")) {
            info.setOsArch("x64");
        } else {
            info.setOsArch("x86");
        }
    }

    /**
     * 解析Linux架构
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseLinuxArchitecture(String userAgent, UserAgentInfo info) {
        if (userAgent.contains("x86_64")) {
            info.setOsArch("x64");
        } else if (userAgent.contains("i686")) {
            info.setOsArch("x86");
        } else if (userAgent.contains("aarch64")) {
            info.setOsArch("arm64");
        }
    }

    /**
     * 解析设备信息
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseDeviceInfo(String userAgent, UserAgentInfo info) {
        // 移动设备
        if (userAgent.contains("Mobile") || userAgent.contains("Android")) {
            info.setDeviceType("MOBILE");
            parseMobileDevice(userAgent, info);
        }
        // 平板设备
        else if (userAgent.contains("iPad") || userAgent.contains("Tablet")) {
            info.setDeviceType("TABLET");
            parseTabletDevice(userAgent, info);
        }
        // PC设备
        else {
            info.setDeviceType("PC");
        }
    }

    /**
     * 解析移动设备信息
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseMobileDevice(String userAgent, UserAgentInfo info) {
        // iPhone设备
        if (userAgent.contains("iPhone")) {
            info.setDeviceBrand("Apple");
            info.setDeviceModel("iPhone");
        }
        // Samsung设备
        else if (userAgent.contains("SM-")) {
            info.setDeviceBrand("Samsung");
            extractVersion(userAgent, "(SM-[A-Z0-9]+)", info::setDeviceModel);
        }
        // Huawei设备
        else if (userAgent.contains("HUAWEI") || userAgent.contains("HW-")) {
            info.setDeviceBrand("Huawei");
            extractVersion(userAgent, "(HUAWEI [A-Z0-9-]+|HW-[A-Z0-9]+)", info::setDeviceModel);
        }
        // Xiaomi设备
        else if (userAgent.contains("MI ") || userAgent.contains("Redmi")) {
            info.setDeviceBrand("Xiaomi");
            extractVersion(userAgent, "(MI [A-Z0-9 ]+|Redmi [A-Z0-9 ]+)", info::setDeviceModel);
        }
        // OPPO设备
        else if (userAgent.contains("OPPO")) {
            info.setDeviceBrand("OPPO");
            extractVersion(userAgent, "(OPPO [A-Z0-9]+)", info::setDeviceModel);
        }
        // vivo设备
        else if (userAgent.contains("vivo")) {
            info.setDeviceBrand("vivo");
            extractVersion(userAgent, "(vivo [A-Z0-9]+)", info::setDeviceModel);
        }
    }

    /**
     * 解析平板设备信息
     *
     * @param userAgent User-Agent字符串
     * @param info 用户代理信息对象
     */
    private static void parseTabletDevice(String userAgent, UserAgentInfo info) {
        if (userAgent.contains("iPad")) {
            info.setDeviceBrand("Apple");
            info.setDeviceModel("iPad");
        }
    }

    /**
     * 提取版本号
     *
     * @param userAgent User-Agent字符串
     * @param pattern 正则表达式模式
     * @param setter 版本设置器
     */
    private static void extractVersion(String userAgent, String pattern, VersionSetter setter) {
        try {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(userAgent);
            if (matcher.find()) {
                setter.setVersion(matcher.group(1));
            }
        } catch (Exception e) {
            log.debug("提取版本号失败，pattern: {}, userAgent: {}", pattern, userAgent);
        }
    }

    /**
     * 版本设置器函数式接口
     */
    @FunctionalInterface
    private interface VersionSetter {
        void setVersion(String version);
    }

    /**
     * User-Agent解析结果信息类
     */
    @Data
    public static class UserAgentInfo {
        /** 浏览器名称 */
        private String browserName;
        /** 浏览器版本 */
        private String browserVersion;
        /** 浏览器引擎 */
        private String browserEngine;
        /** 操作系统名称 */
        private String osName;
        /** 操作系统版本 */
        private String osVersion;
        /** 系统架构 */
        private String osArch;
        /** 设备类型 */
        private String deviceType;
        /** 设备品牌 */
        private String deviceBrand;
        /** 设备型号 */
        private String deviceModel;
    }
}