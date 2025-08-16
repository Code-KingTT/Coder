package com.coder.utils;

import com.coder.constant.Constants;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public final class DateUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 默认时区
     */
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    // ================ 日期格式化器 ================

    /**
     * 日期格式化器
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.DATE);
    public static final DateTimeFormatter DATE_SIMPLE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.DATE_SIMPLE);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.TIME);
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.DATETIME);
    public static final DateTimeFormatter DATETIME_SIMPLE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.DATETIME_SIMPLE);
    public static final DateTimeFormatter DATETIME_MILLIS_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.DATETIME_MILLIS);
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(Constants.DateFormat.TIMESTAMP);

    // ================ 获取当前时间方法 ================

    /**
     * 获取当前LocalDateTime
     *
     * @return 当前LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前LocalDate
     *
     * @return 当前LocalDate
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 获取当前LocalTime
     *
     * @return 当前LocalTime
     */
    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     *
     * @return 当前时间戳（秒）
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    // ================ 日期格式化方法 ================

    /**
     * 格式化LocalDateTime为字符串
     *
     * @param dateTime LocalDateTime
     * @param pattern  格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || StrUtils.isBlank(pattern)) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化LocalDateTime为字符串（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTime LocalDateTime
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, Constants.DateFormat.DATETIME);
    }

    /**
     * 格式化LocalDate为字符串
     *
     * @param date    LocalDate
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null || StrUtils.isBlank(pattern)) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化LocalDate为字符串（默认格式：yyyy-MM-dd）
     *
     * @param date LocalDate
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date) {
        return format(date, Constants.DateFormat.DATE);
    }

    /**
     * 格式化LocalTime为字符串
     *
     * @param time    LocalTime
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null || StrUtils.isBlank(pattern)) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化LocalTime为字符串（默认格式：HH:mm:ss）
     *
     * @param time LocalTime
     * @return 格式化后的字符串
     */
    public static String format(LocalTime time) {
        return format(time, Constants.DateFormat.TIME);
    }

    // ================ 日期解析方法 ================

    /**
     * 解析字符串为LocalDateTime
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     格式模式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StrUtils.isBlank(dateTimeStr) || StrUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDateTime（默认格式：yyyy-MM-dd HH:mm:ss）
     *
     * @param dateTimeStr 日期时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, Constants.DateFormat.DATETIME);
    }

    /**
     * 解析字符串为LocalDate
     *
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (StrUtils.isBlank(dateStr) || StrUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalDate（默认格式：yyyy-MM-dd）
     *
     * @param dateStr 日期字符串
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, Constants.DateFormat.DATE);
    }

    /**
     * 解析字符串为LocalTime
     *
     * @param timeStr 时间字符串
     * @param pattern 格式模式
     * @return LocalTime
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (StrUtils.isBlank(timeStr) || StrUtils.isBlank(pattern)) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串为LocalTime（默认格式：HH:mm:ss）
     *
     * @param timeStr 时间字符串
     * @return LocalTime
     */
    public static LocalTime parseTime(String timeStr) {
        return parseTime(timeStr, Constants.DateFormat.TIME);
    }

    // ================ 日期转换方法 ================

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * LocalDate转Date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    /**
     * Date转LocalDate
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp 时间戳（毫秒）
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID);
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param localDateTime LocalDateTime
     * @return 时间戳（毫秒）
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        }
        return localDateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
    }

    // ================ 日期计算方法 ================

    /**
     * 增加年数
     *
     * @param dateTime LocalDateTime
     * @param years    年数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        return dateTime == null ? null : dateTime.plusYears(years);
    }

    /**
     * 增加月数
     *
     * @param dateTime LocalDateTime
     * @param months   月数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime == null ? null : dateTime.plusMonths(months);
    }

    /**
     * 增加天数
     *
     * @param dateTime LocalDateTime
     * @param days     天数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.plusDays(days);
    }

    /**
     * 增加小时数
     *
     * @param dateTime LocalDateTime
     * @param hours    小时数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.plusHours(hours);
    }

    /**
     * 增加分钟数
     *
     * @param dateTime LocalDateTime
     * @param minutes  分钟数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime == null ? null : dateTime.plusMinutes(minutes);
    }

    /**
     * 增加秒数
     *
     * @param dateTime LocalDateTime
     * @param seconds  秒数
     * @return 计算后的LocalDateTime
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime == null ? null : dateTime.plusSeconds(seconds);
    }

    // ================ 日期比较方法 ================

    /**
     * 判断是否为同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return true-同一天，false-不是同一天
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    /**
     * 判断是否为同一天
     *
     * @param dateTime1 日期时间1
     * @param dateTime2 日期时间2
     * @return true-同一天，false-不是同一天
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().equals(dateTime2.toLocalDate());
    }

    /**
     * 判断是否为今天
     *
     * @param date 日期
     * @return true-今天，false-不是今天
     */
    public static boolean isToday(LocalDate date) {
        return isSameDay(date, today());
    }

    /**
     * 判断是否为今天
     *
     * @param dateTime 日期时间
     * @return true-今天，false-不是今天
     */
    public static boolean isToday(LocalDateTime dateTime) {
        return isSameDay(dateTime, now());
    }

    /**
     * 判断是否为昨天
     *
     * @param date 日期
     * @return true-昨天，false-不是昨天
     */
    public static boolean isYesterday(LocalDate date) {
        return isSameDay(date, today().minusDays(1));
    }

    /**
     * 判断是否为明天
     *
     * @param date 日期
     * @return true-明天，false-不是明天
     */
    public static boolean isTomorrow(LocalDate date) {
        return isSameDay(date, today().plusDays(1));
    }

    // ================ 日期范围方法 ================

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的小时差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的分钟差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 分钟差
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的秒数差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 秒数差
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    // ================ 日期边界方法 ================

    /**
     * 获取一天的开始时间（00:00:00）
     *
     * @param date 日期
     * @return 开始时间
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    /**
     * 获取一天的结束时间（23:59:59.999999999）
     *
     * @param date 日期
     * @return 结束时间
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date == null ? null : date.atTime(LocalTime.MAX);
    }

    /**
     * 获取本月第一天
     *
     * @return 本月第一天
     */
    public static LocalDate firstDayOfMonth() {
        return today().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取本月最后一天
     *
     * @return 本月最后一天
     */
    public static LocalDate lastDayOfMonth() {
        return today().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取指定日期所在月的第一天
     *
     * @param date 日期
     * @return 月第一天
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期所在月的最后一天
     *
     * @param date 日期
     * @return 月最后一天
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取本年第一天
     *
     * @return 本年第一天
     */
    public static LocalDate firstDayOfYear() {
        return today().with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取本年最后一天
     *
     * @return 本年最后一天
     */
    public static LocalDate lastDayOfYear() {
        return today().with(TemporalAdjusters.lastDayOfYear());
    }

    // ================ 年龄计算方法 ================

    /**
     * 根据生日计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int calculateAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(birthday, today());
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int calculateAge(LocalDateTime birthday) {
        if (birthday == null) {
            return 0;
        }
        return calculateAge(birthday.toLocalDate());
    }

    // ================ 工作日判断方法 ================

    /**
     * 判断是否为工作日（周一到周五）
     *
     * @param date 日期
     * @return true-工作日，false-非工作日
     */
    public static boolean isWorkday(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
               dayOfWeek.getValue() <= DayOfWeek.FRIDAY.getValue();
    }

    /**
     * 判断是否为周末（周六周日）
     *
     * @param date 日期
     * @return true-周末，false-非周末
     */
    public static boolean isWeekend(LocalDate date) {
        return !isWorkday(date);
    }

    // ================ 人性化时间方法 ================

    /**
     * 获取人性化的时间描述
     * 例如：刚刚、1分钟前、1小时前、1天前等
     *
     * @param dateTime 日期时间
     * @return 人性化描述
     */
    public static String getHumanizedTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        LocalDateTime now = now();
        long seconds = secondsBetween(dateTime, now);
        
        if (seconds < 60) {
            return "刚刚";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else if (seconds < 2592000) {
            return (seconds / 86400) + "天前";
        } else if (seconds < 31536000) {
            return (seconds / 2592000) + "个月前";
        } else {
            return (seconds / 31536000) + "年前";
        }
    }
}
