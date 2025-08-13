package com.coder.constant;

/**
 * 全局业务常量类
 *
 * @author Sunset
 * @date 2025/8/13
 */
public final class Constants {

    /**
     * 私有构造方法，防止实例化
     */
    private Constants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ================ 通用常量 ================

    /**
     * 是否标识
     */
    public static final Integer YES = 1;
    public static final Integer NO = 0;

    /**
     * 启用禁用状态
     */
    public static final Integer ENABLED = 1;
    public static final Integer DISABLED = 0;

    /**
     * 删除状态
     */
    public static final Integer NOT_DELETED = 0;
    public static final Integer DELETED = 1;

    /**
     * 默认字符编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 默认分页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "666666";

    // ================ 用户相关常量 ================

    /**
     * 用户相关常量
     */
    public static final class User {
        /**
         * 用户状态
         */
        public static final Integer STATUS_NORMAL = 1;    // 正常
        public static final Integer STATUS_DISABLED = 2;  // 禁用
        public static final Integer STATUS_LOCKED = 3;    // 锁定

        /**
         * 用户类型
         */
        public static final Integer TYPE_NORMAL = 1;   // 普通用户
        public static final Integer TYPE_VIP = 2;      // VIP用户
        public static final Integer TYPE_ADMIN = 3;    // 管理员

        /**
         * 性别
         */
        public static final Integer GENDER_MALE = 1;      // 男
        public static final Integer GENDER_FEMALE = 2;    // 女
        public static final Integer GENDER_UNKNOWN = 3;   // 未知

        /**
         * 用户来源
         */
        public static final Integer SOURCE_REGISTER = 1;  // 注册
        public static final Integer SOURCE_ADMIN = 2;     // 管理员创建
        public static final Integer SOURCE_IMPORT = 3;    // 第三方导入

        /**
         * 用户名长度限制
         */
        public static final Integer USERNAME_MIN_LENGTH = 3;
        public static final Integer USERNAME_MAX_LENGTH = 20;

        /**
         * 密码长度限制
         */
        public static final Integer PASSWORD_MIN_LENGTH = 6;
        public static final Integer PASSWORD_MAX_LENGTH = 20;
    }

    // ================ 缓存相关常量 ================

    /**
     * 缓存键前缀
     */
    public static final class CacheKey {
        /**
         * 用户缓存键前缀
         */
        public static final String USER_PREFIX = "user:";
        public static final String USER_INFO = "user:info:";      // 用户信息
        public static final String USER_TOKEN = "user:token:";    // 用户令牌
        public static final String USER_LOGIN = "user:login:";    // 用户登录信息

        /**
         * 验证码缓存键
         */
        public static final String CAPTCHA_PREFIX = "captcha:";
        public static final String SMS_CODE_PREFIX = "sms:code:";
        public static final String EMAIL_CODE_PREFIX = "email:code:";

        /**
         * 系统配置缓存键
         */
        public static final String SYSTEM_CONFIG = "system:config:";
        public static final String DICT_DATA = "dict:data:";

        /**
         * 接口限流缓存键
         */
        public static final String RATE_LIMIT = "rate:limit:";
    }

    // ================ 缓存过期时间常量 ================

    /**
     * 缓存过期时间（秒）
     */
    public static final class CacheExpire {
        /**
         * 1分钟
         */
        public static final long ONE_MINUTE = 60L;

        /**
         * 5分钟
         */
        public static final long FIVE_MINUTES = 5 * 60L;

        /**
         * 15分钟
         */
        public static final long FIFTEEN_MINUTES = 15 * 60L;

        /**
         * 30分钟
         */
        public static final long THIRTY_MINUTES = 30 * 60L;

        /**
         * 1小时
         */
        public static final long ONE_HOUR = 60 * 60L;

        /**
         * 1天
         */
        public static final long ONE_DAY = 24 * 60 * 60L;

        /**
         * 1周
         */
        public static final long ONE_WEEK = 7 * 24 * 60 * 60L;

        /**
         * 1个月
         */
        public static final long ONE_MONTH = 30 * 24 * 60 * 60L;

        /**
         * 默认过期时间（1小时）
         */
        public static final long DEFAULT_EXPIRE = ONE_HOUR;

        /**
         * 用户令牌过期时间（7天）
         */
        public static final long USER_TOKEN_EXPIRE = ONE_WEEK;

        /**
         * 验证码过期时间（5分钟）
         */
        public static final long CAPTCHA_EXPIRE = FIVE_MINUTES;

        /**
         * 短信验证码过期时间（5分钟）
         */
        public static final long SMS_CODE_EXPIRE = FIVE_MINUTES;
    }

    // ================ 正则表达式常量 ================

    /**
     * 正则表达式
     */
    public static final class Regex {
        /**
         * 手机号正则
         */
        public static final String PHONE = "^1[3-9]\\d{9}$";

        /**
         * 邮箱正则
         */
        public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        /**
         * 用户名正则（字母、数字、下划线）
         */
        public static final String USERNAME = "^[a-zA-Z0-9_]+$";

        /**
         * 密码正则（字母、数字、特殊字符）
         */
        public static final String PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_+-=\\[\\]{};':\"\\\\|,.<>\\/?]+$";

        /**
         * 身份证号正则
         */
        public static final String ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

        /**
         * 中文正则
         */
        public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

        /**
         * IP地址正则
         */
        public static final String IP_ADDRESS = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    }

    // ================ HTTP相关常量 ================

    /**
     * HTTP相关常量
     */
    public static final class Http {
        /**
         * 请求头
         */
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String USER_AGENT = "User-Agent";
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String X_REAL_IP = "X-Real-IP";

        /**
         * 媒体类型
         */
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_XML = "application/xml";
        public static final String TEXT_PLAIN = "text/plain";
        public static final String TEXT_HTML = "text/html";

        /**
         * 请求方法
         */
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String PATCH = "PATCH";
    }

    // ================ 日期时间常量 ================

    /**
     * 日期时间格式常量
     */
    public static final class DateFormat {
        /**
         * 日期格式
         */
        public static final String DATE = "yyyy-MM-dd";
        public static final String DATE_SIMPLE = "yyyyMMdd";
        public static final String DATE_SLASH = "yyyy/MM/dd";
        public static final String DATE_CHINESE = "yyyy年MM月dd日";

        /**
         * 时间格式
         */
        public static final String TIME = "HH:mm:ss";
        public static final String TIME_SIMPLE = "HHmmss";

        /**
         * 日期时间格式
         */
        public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
        public static final String DATETIME_SIMPLE = "yyyyMMddHHmmss";
        public static final String DATETIME_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";

        /**
         * 时间戳格式
         */
        public static final String TIMESTAMP = "yyyyMMddHHmmssSSS";
    }

    // ================ 系统配置常量 ================

    /**
     * 系统配置相关常量
     */
    public static final class System {
        /**
         * 系统名称
         */
        public static final String SYSTEM_NAME = "Coder Server";

        /**
         * 系统版本
         */
        public static final String SYSTEM_VERSION = "1.0.0";

        /**
         * 系统作者
         */
        public static final String SYSTEM_AUTHOR = "Sunset";

        /**
         * 默认头像
         */
        public static final String DEFAULT_AVATAR = "/static/images/default-avatar.png";

        /**
         * 文件上传路径
         */
        public static final String UPLOAD_PATH = "/uploads/";

        /**
         * 允许上传的文件类型
         */
        public static final String[] ALLOWED_FILE_TYPES = {
            "jpg", "jpeg", "png", "gif", "bmp", "webp",  // 图片
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",  // 文档
            "txt", "md", "log"  // 文本
        };

        /**
         * 文件大小限制（字节）
         */
        public static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;  // 10MB
    }

    // ================ 业务操作常量 ================

    /**
     * 业务操作类型
     */
    public static final class Operation {
        /**
         * 基础操作
         */
        public static final String CREATE = "CREATE";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String QUERY = "QUERY";

        /**
         * 用户操作
         */
        public static final String USER_LOGIN = "USER_LOGIN";
        public static final String USER_LOGOUT = "USER_LOGOUT";
        public static final String USER_REGISTER = "USER_REGISTER";
        public static final String PASSWORD_RESET = "PASSWORD_RESET";

        /**
         * 系统操作
         */
        public static final String SYSTEM_CONFIG = "SYSTEM_CONFIG";
        public static final String CACHE_CLEAR = "CACHE_CLEAR";
        public static final String DATA_EXPORT = "DATA_EXPORT";
        public static final String DATA_IMPORT = "DATA_IMPORT";
    }
}
