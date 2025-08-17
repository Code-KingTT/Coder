-- Coder项目数据库初始化脚本
-- 作者: Sunset
-- 创建时间: 2025-8-13

-- ======================================
-- 数据库创建
-- ======================================

-- 创建数据库
CREATE
DATABASE IF NOT EXISTS `coder`
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci 
    COMMENT 'Coder项目数据库';

-- 使用数据库
USE
`coder`;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user`
(
    `id`                   BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 基础信息
    `username`             VARCHAR(50)  NOT NULL COMMENT '用户名，唯一标识',
    `password`             VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `salt`                 VARCHAR(64)           DEFAULT NULL COMMENT '密码盐值',
    `nickname`             VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    `real_name`            VARCHAR(50)           DEFAULT NULL COMMENT '真实姓名',

    -- 联系信息
    `mobile`               VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `email`                VARCHAR(100)          DEFAULT NULL COMMENT '邮箱地址',
    `province`             VARCHAR(50)           DEFAULT NULL COMMENT '省份',
    `city`                 VARCHAR(50)           DEFAULT NULL COMMENT '城市',
    `district`             VARCHAR(50)           DEFAULT NULL COMMENT '区县',
    `address`              VARCHAR(255)          DEFAULT NULL COMMENT '详细地址',

    -- 个人信息
    `avatar`               VARCHAR(500)          DEFAULT NULL COMMENT '头像URL',
    `gender`               TINYINT(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday`             DATE                  DEFAULT NULL COMMENT '生日',
    `profile`              TEXT                  DEFAULT NULL COMMENT '个人简介',

    -- 状态信息
    `status`               TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账户状态：0-禁用，1-正常，2-锁定',
    `last_login_time`      DATETIME              DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`        VARCHAR(50)           DEFAULT NULL COMMENT '最后登录IP',
    `login_count`          INT(11) DEFAULT 0 COMMENT '登录次数',

    -- 安全信息
    `failed_login_count`   INT(11) DEFAULT 0 COMMENT '连续登录失败次数',
    `locked_time`          DATETIME              DEFAULT NULL COMMENT '账户锁定时间',
    `password_update_time` DATETIME              DEFAULT NULL COMMENT '密码最后修改时间',
    `two_factor_enabled`   TINYINT(1) DEFAULT 0 COMMENT '是否启用双因子认证：0-否，1-是',
    `two_factor_secret`    VARCHAR(32)           DEFAULT NULL COMMENT '双因子认证密钥',

    -- 扩展信息
    `tags`                 VARCHAR(500)          DEFAULT NULL COMMENT '用户标签，多个标签用逗号分隔',
    `source`               VARCHAR(20)           DEFAULT 'REGISTER' COMMENT '用户来源：REGISTER-注册，IMPORT-导入，THIRD_PARTY-第三方',
    `third_party_id`       VARCHAR(100)          DEFAULT NULL COMMENT '第三方平台用户ID',
    `third_party_type`     VARCHAR(20)           DEFAULT NULL COMMENT '第三方平台类型：WECHAT-微信，QQ，ALIPAY-支付宝等',

    -- 基础字段（继承自BaseEntity）
    `create_time`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`            BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`            BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`              TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`               VARCHAR(500)          DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 初始化管理员用户
INSERT INTO `sys_user` (`username`, `password`, `salt`, `nickname`, `real_name`, `gender`, `status`,
                        `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES ('admin', '$2a$10$7JB720yubVSOfvVWdF3iVu9S56RYNcP6bPT0UVc1JaDkHY1qnJdV6',
        'admin_salt', '系统管理员', '管理员', 1, 1,
        NOW(), NOW(), 1, 1, 0, '系统初始管理员账户');

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 角色基础信息
    `role_code`   VARCHAR(50)  NOT NULL COMMENT '角色编码，唯一标识',
    `role_name`   VARCHAR(100) NOT NULL COMMENT '角色名称',
    `role_desc`   VARCHAR(500)          DEFAULT NULL COMMENT '角色描述',

    -- 显示控制
    `sort_order`  INT(11) DEFAULT 0 COMMENT '显示顺序',
    `status`      TINYINT(1) NOT NULL DEFAULT 1 COMMENT '角色状态：0-禁用，1-启用',

    -- 基础字段（继承自BaseEntity）
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`   BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`      VARCHAR(500)          DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 菜单基础信息
    `menu_name`   VARCHAR(100) NOT NULL COMMENT '菜单名称',
    `parent_id`   BIGINT(20) DEFAULT 0 COMMENT '父菜单ID，0表示顶级菜单',
    `menu_type`   TINYINT(1) NOT NULL DEFAULT 1 COMMENT '菜单类型：1-目录，2-菜单，3-按钮',

    -- 路由信息
    `path`        VARCHAR(200)          DEFAULT NULL COMMENT '路由路径',
    `component`   VARCHAR(200)          DEFAULT NULL COMMENT '组件路径',
    `permission`  VARCHAR(200)          DEFAULT NULL COMMENT '权限标识',

    -- 显示信息
    `icon`        VARCHAR(100)          DEFAULT NULL COMMENT '菜单图标',
    `sort_order`  INT(11) DEFAULT 0 COMMENT '显示顺序',
    `visible`     TINYINT(1) DEFAULT 1 COMMENT '是否显示：0-隐藏，1-显示',
    `status`      TINYINT(1) NOT NULL DEFAULT 1 COMMENT '菜单状态：0-禁用，1-启用',

    -- 基础字段（继承自BaseEntity）
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`   BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`      VARCHAR(500)          DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 关联信息
    `user_id`     BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT(20) NOT NULL COMMENT '角色ID',

    -- 基础字段（继承自BaseEntity）
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`   BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`      VARCHAR(500)         DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 关联信息
    `role_id`     BIGINT(20) NOT NULL COMMENT '角色ID',
    `menu_id`     BIGINT(20) NOT NULL COMMENT '菜单ID',

    -- 基础字段（继承自BaseEntity）
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`   BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`      VARCHAR(500)         DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';


-- 初始化角色数据
INSERT INTO `sys_role` (`role_code`, `role_name`, `role_desc`, `sort_order`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    ('ADMIN', '系统管理员', '拥有系统所有权限', 1, 1, NOW(), NOW(), 1, 1, 0, '系统管理员角色'),
    ('USER', '普通用户', '普通用户权限', 2, 1, NOW(), NOW(), 1, 1, 0, '普通用户角色');

-- 初始化菜单数据
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `menu_type`, `path`, `component`, `permission`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
-- 系统管理目录
(1, '系统管理', 0, 1, '/system', 'Layout', 'system', 'el-icon-setting', 1, 1, 1, NOW(), NOW(), 1, 1, 0, '系统管理目录'),

-- 用户管理菜单
(2, '用户管理', 1, 2, '/system/user', 'system/user/index', 'system:user:list', 'el-icon-user', 1, 1, 1, NOW(), NOW(), 1, 1, 0, '用户管理菜单'),
(3, '用户新增', 2, 3, '', '', 'system:user:add', '', 1, 0, 1, NOW(), NOW(), 1, 1, 0, '用户新增按钮'),
(4, '用户修改', 2, 3, '', '', 'system:user:edit', '', 2, 0, 1, NOW(), NOW(), 1, 1, 0, '用户修改按钮'),
(5, '用户删除', 2, 3, '', '', 'system:user:delete', '', 3, 0, 1, NOW(), NOW(), 1, 1, 0, '用户删除按钮'),

-- 角色管理菜单
(6, '角色管理', 1, 2, '/system/role', 'system/role/index', 'system:role:list', 'el-icon-s-custom', 2, 1, 1, NOW(), NOW(), 1, 1, 0, '角色管理菜单'),
(7, '角色新增', 6, 3, '', '', 'system:role:add', '', 1, 0, 1, NOW(), NOW(), 1, 1, 0, '角色新增按钮'),
(8, '角色修改', 6, 3, '', '', 'system:role:edit', '', 2, 0, 1, NOW(), NOW(), 1, 1, 0, '角色修改按钮'),
(9, '角色删除', 6, 3, '', '', 'system:role:delete', '', 3, 0, 1, NOW(), NOW(), 1, 1, 0, '角色删除按钮'),

-- 菜单管理菜单
(10, '菜单管理', 1, 2, '/system/menu', 'system/menu/index', 'system:menu:list', 'el-icon-menu', 3, 1, 1, NOW(), NOW(), 1, 1, 0, '菜单管理菜单'),
(11, '菜单新增', 10, 3, '', '', 'system:menu:add', '', 1, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单新增按钮'),
(12, '菜单修改', 10, 3, '', '', 'system:menu:edit', '', 2, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单修改按钮'),
(13, '菜单删除', 10, 3, '', '', 'system:menu:delete', '', 3, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单删除按钮');

-- 给管理员分配角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES (1, 1, NOW(), NOW(), 1, 1, 0, '管理员角色分配');

-- 给管理员角色分配所有菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (1, 1, NOW(), NOW(), 1, 1, 0, '系统管理目录'),
    (1, 2, NOW(), NOW(), 1, 1, 0, '用户管理菜单'),
    (1, 3, NOW(), NOW(), 1, 1, 0, '用户新增按钮'),
    (1, 4, NOW(), NOW(), 1, 1, 0, '用户修改按钮'),
    (1, 5, NOW(), NOW(), 1, 1, 0, '用户删除按钮'),
    (1, 6, NOW(), NOW(), 1, 1, 0, '角色管理菜单'),
    (1, 7, NOW(), NOW(), 1, 1, 0, '角色新增按钮'),
    (1, 8, NOW(), NOW(), 1, 1, 0, '角色修改按钮'),
    (1, 9, NOW(), NOW(), 1, 1, 0, '角色删除按钮'),
    (1, 10, NOW(), NOW(), 1, 1, 0, '菜单管理菜单'),
    (1, 11, NOW(), NOW(), 1, 1, 0, '菜单新增按钮'),
    (1, 12, NOW(), NOW(), 1, 1, 0, '菜单修改按钮'),
    (1, 13, NOW(), NOW(), 1, 1, 0, '菜单删除按钮');

