-- 用户表建表脚本（无索引版本）
-- 作者: Sunset
-- 创建时间: 2025-01-27

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    
    -- 基础信息
    `username` VARCHAR(50) NOT NULL COMMENT '用户名，唯一标识',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `salt` VARCHAR(64) DEFAULT NULL COMMENT '密码盐值',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    
    -- 联系信息
    `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    
    -- 个人信息
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `profile` TEXT DEFAULT NULL COMMENT '个人简介',
    
    -- 状态信息
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账户状态：0-禁用，1-正常，2-锁定',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `login_count` INT(11) DEFAULT 0 COMMENT '登录次数',
    
    -- 安全信息
    `failed_login_count` INT(11) DEFAULT 0 COMMENT '连续登录失败次数',
    `locked_time` DATETIME DEFAULT NULL COMMENT '账户锁定时间',
    `password_update_time` DATETIME DEFAULT NULL COMMENT '密码最后修改时间',
    `two_factor_enabled` TINYINT(1) DEFAULT 0 COMMENT '是否启用双因子认证：0-否，1-是',
    `two_factor_secret` VARCHAR(32) DEFAULT NULL COMMENT '双因子认证密钥',
    
    -- 扩展信息
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '用户标签，多个标签用逗号分隔',
    `source` VARCHAR(20) DEFAULT 'REGISTER' COMMENT '用户来源：REGISTER-注册，IMPORT-导入，THIRD_PARTY-第三方',
    `third_party_id` VARCHAR(100) DEFAULT NULL COMMENT '第三方平台用户ID',
    `third_party_type` VARCHAR(20) DEFAULT NULL COMMENT '第三方平台类型：WECHAT-微信，QQ，ALIPAY-支付宝等',
    
    -- 基础字段（继承自BaseEntity）
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注信息',
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 初始化管理员用户（密码为123456，实际使用时应修改）
INSERT INTO `sys_user` (
    `username`, `password`, `salt`, `nickname`, `real_name`, `gender`, `status`, 
    `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`
) VALUES (
    'admin', '$2a$10$7JB720yubVSOfvVWdF3iVu9S56RYNcP6bPT0UVc1JaDkHY1qnJdV6', 
    'admin_salt', '系统管理员', '管理员', 1, 1, 
    NOW(), NOW(), 1, 1, 0, '系统初始管理员账户'
);