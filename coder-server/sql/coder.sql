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

-- 文件表
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`
(
    `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 文件基础信息
    `file_name`         VARCHAR(255) NOT NULL COMMENT '文件原始名称',
    `file_path`         VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_url`          VARCHAR(500)          DEFAULT NULL COMMENT '文件访问URL',
    `file_size`         BIGINT(20) NOT NULL COMMENT '文件大小（字节）',
    `file_type`         VARCHAR(50)  NOT NULL COMMENT '文件类型（扩展名）',
    `mime_type`         VARCHAR(100)          DEFAULT NULL COMMENT 'MIME类型',
    `file_md5`          VARCHAR(32)           DEFAULT NULL COMMENT '文件MD5值',
    `file_sha1`         VARCHAR(40)           DEFAULT NULL COMMENT '文件SHA1值',

    -- 文件分类信息
    `category`          VARCHAR(50)           DEFAULT 'OTHER' COMMENT '文件分类：IMAGE-图片，DOCUMENT-文档，VIDEO-视频，AUDIO-音频，OTHER-其他',
    `business_type`     VARCHAR(50)           DEFAULT NULL COMMENT '业务类型：AVATAR-头像，ATTACHMENT-附件，TEMP-临时文件等',
    `module_name`       VARCHAR(50)           DEFAULT NULL COMMENT '所属模块名称',
    `business_id`       BIGINT(20)            DEFAULT NULL COMMENT '关联业务ID',

    -- 存储信息
    `storage_type`      VARCHAR(20)  NOT NULL DEFAULT 'LOCAL' COMMENT '存储类型：LOCAL-本地存储，OSS-阿里云OSS，COS-腾讯云COS，QINIU-七牛云等',
    `bucket_name`       VARCHAR(100)          DEFAULT NULL COMMENT '存储桶名称（云存储）',
    `storage_path`      VARCHAR(500)          DEFAULT NULL COMMENT '存储路径（云存储）',

    -- 分片上传相关
    `chunk_size`        INT(11) DEFAULT NULL COMMENT '分片大小（字节）',
    `total_chunks`      INT(11) DEFAULT NULL COMMENT '总分片数',
    `upload_id`         VARCHAR(100)          DEFAULT NULL COMMENT '分片上传ID（云存储）',
    `upload_status`     TINYINT(1) DEFAULT 1 COMMENT '上传状态：0-上传中，1-上传完成，2-上传失败',

    -- 文件状态
    `status`            TINYINT(1) NOT NULL DEFAULT 1 COMMENT '文件状态：0-禁用，1-正常，2-待审核，3-审核失败',
    `download_count`    INT(11) DEFAULT 0 COMMENT '下载次数',
    `view_count`        INT(11) DEFAULT 0 COMMENT '查看次数',
    `favorite_count`    INT(11) DEFAULT 0 COMMENT '收藏次数',

    -- 权限控制
    `access_level`      TINYINT(1) DEFAULT 1 COMMENT '访问级别：1-公开，2-登录可见，3-私有',
    `owner_id`          BIGINT(20)            DEFAULT NULL COMMENT '文件所有者ID',

    -- 扩展信息
    `thumbnail_path`    VARCHAR(500)          DEFAULT NULL COMMENT '缩略图路径（图片/视频）',
    `duration`          INT(11) DEFAULT NULL COMMENT '时长（音频/视频，单位：秒）',
    `width`             INT(11) DEFAULT NULL COMMENT '宽度（图片/视频）',
    `height`            INT(11) DEFAULT NULL COMMENT '高度（图片/视频）',
    `tags`              VARCHAR(500)          DEFAULT NULL COMMENT '文件标签，多个标签用逗号分隔',

    -- 基础字段（继承自BaseEntity）
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`         BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`         BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`           TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`            VARCHAR(500)          DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 文件操作记录表
DROP TABLE IF EXISTS `sys_file_record`;
CREATE TABLE `sys_file_record`
(
    `id`                BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    -- 关联信息
    `file_id`           BIGINT(20) NOT NULL COMMENT '文件ID',
    `user_id`           BIGINT(20) NOT NULL COMMENT '用户ID',

    -- 操作信息
    `action_type`       VARCHAR(20) NOT NULL COMMENT '操作类型：UPLOAD-上传，DOWNLOAD-下载，VIEW-查看，FAVORITE-收藏，UNFAVORITE-取消收藏，DELETE-删除',
    `action_desc`       VARCHAR(200)         DEFAULT NULL COMMENT '操作描述',

    -- 基础字段（继承自BaseEntity）
    `create_time`       DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`         BIGINT(20) DEFAULT NULL COMMENT '创建人ID',
    `update_by`         BIGINT(20) DEFAULT NULL COMMENT '更新人ID',
    `deleted`           TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除',
    `remark`            VARCHAR(500)         DEFAULT NULL COMMENT '备注信息',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件操作记录表';

-- ======================================
-- 初始化数据
-- ======================================

-- 初始化角色数据
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `role_desc`, `sort_order`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (1, 'ADMIN', '超级管理员', '拥有系统所有权限', 1, 1, NOW(), NOW(), 1, 1, 0, '系统管理员角色'),
    (2, 'USER_MANAGER', '用户管理员', '负责用户管理相关功能', 2, 1, NOW(), NOW(), 1, 1, 0, '用户管理员角色'),
    (3, 'ORDINARY_USER', '普通用户', '普通用户权限', 3, 1, NOW(), NOW(), 1, 1, 0, '普通用户角色');

-- 初始化菜单数据（基于当前项目存在的接口）
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `menu_type`, `path`, `component`, `permission`, `icon`, `sort_order`, `visible`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
-- 系统管理目录
(1, '系统管理', 0, 1, '/system', 'Layout', NULL, 'system', 1, 1, 1, NOW(), NOW(), 1, 1, 0, '系统管理目录'),

-- 用户管理菜单（基于UserController）
(10, '用户管理', 1, 2, '/system/user', 'system/user/index', 'system:user:list', 'user', 1, 1, 1, NOW(), NOW(), 1, 1, 0, '用户管理菜单'),
(11, '用户查询', 10, 3, '', '', 'system:user:query', '', 1, 0, 1, NOW(), NOW(), 1, 1, 0, '用户查询按钮'),
(12, '用户新增', 10, 3, '', '', 'system:user:add', '', 2, 0, 1, NOW(), NOW(), 1, 1, 0, '用户新增按钮'),
(13, '用户修改', 10, 3, '', '', 'system:user:update', '', 3, 0, 1, NOW(), NOW(), 1, 1, 0, '用户修改按钮'),
(14, '用户删除', 10, 3, '', '', 'system:user:delete', '', 4, 0, 1, NOW(), NOW(), 1, 1, 0, '用户删除按钮'),
(15, '用户状态', 10, 3, '', '', 'system:user:status', '', 5, 0, 1, NOW(), NOW(), 1, 1, 0, '用户状态修改按钮'),
(16, '重置密码', 10, 3, '', '', 'system:user:resetPwd', '', 6, 0, 1, NOW(), NOW(), 1, 1, 0, '重置密码按钮'),
(17, '用户导出', 10, 3, '', '', 'system:user:export', '', 7, 0, 1, NOW(), NOW(), 1, 1, 0, '用户导出按钮'),
(18, '用户导入', 10, 3, '', '', 'system:user:import', '', 8, 0, 1, NOW(), NOW(), 1, 1, 0, '用户导入按钮'),

-- 菜单管理菜单（基于MenuController）
(20, '菜单管理', 1, 2, '/system/menu', 'system/menu/index', 'system:menu:list', 'tree-table', 2, 1, 1, NOW(), NOW(), 1, 1, 0, '菜单管理菜单'),
(21, '菜单查询', 20, 3, '', '', 'system:menu:query', '', 1, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单查询按钮'),
(22, '菜单新增', 20, 3, '', '', 'system:menu:add', '', 2, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单新增按钮'),
(23, '菜单修改', 20, 3, '', '', 'system:menu:update', '', 3, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单修改按钮'),
(24, '菜单删除', 20, 3, '', '', 'system:menu:delete', '', 4, 0, 1, NOW(), NOW(), 1, 1, 0, '菜单删除按钮'),

-- 个人中心菜单
(30, '个人中心', 0, 2, '/profile', 'system/user/profile/index', 'system:user:profile', 'user', 3, 1, 1, NOW(), NOW(), 1, 1, 0, '个人中心菜单');

-- 给超级管理员角色分配所有权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
-- 系统管理
(1, 1, NOW(), NOW(), 1, 1, 0, '系统管理目录'),
-- 用户管理
(1, 10, NOW(), NOW(), 1, 1, 0, '用户管理菜单'),
(1, 11, NOW(), NOW(), 1, 1, 0, '用户查询'),
(1, 12, NOW(), NOW(), 1, 1, 0, '用户新增'),
(1, 13, NOW(), NOW(), 1, 1, 0, '用户修改'),
(1, 14, NOW(), NOW(), 1, 1, 0, '用户删除'),
(1, 15, NOW(), NOW(), 1, 1, 0, '用户状态'),
(1, 16, NOW(), NOW(), 1, 1, 0, '重置密码'),
(1, 17, NOW(), NOW(), 1, 1, 0, '用户导出'),
(1, 18, NOW(), NOW(), 1, 1, 0, '用户导入'),
-- 菜单管理
(1, 20, NOW(), NOW(), 1, 1, 0, '菜单管理菜单'),
(1, 21, NOW(), NOW(), 1, 1, 0, '菜单查询'),
(1, 22, NOW(), NOW(), 1, 1, 0, '菜单新增'),
(1, 23, NOW(), NOW(), 1, 1, 0, '菜单修改'),
(1, 24, NOW(), NOW(), 1, 1, 0, '菜单删除'),
-- 个人中心
(1, 30, NOW(), NOW(), 1, 1, 0, '个人中心');

-- 给用户管理员角色分配用户管理权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (2, 1, NOW(), NOW(), 1, 1, 0, '系统管理目录'),
    (2, 10, NOW(), NOW(), 1, 1, 0, '用户管理菜单'),
    (2, 11, NOW(), NOW(), 1, 1, 0, '用户查询'),
    (2, 12, NOW(), NOW(), 1, 1, 0, '用户新增'),
    (2, 13, NOW(), NOW(), 1, 1, 0, '用户修改'),
    (2, 15, NOW(), NOW(), 1, 1, 0, '用户状态'),
    (2, 16, NOW(), NOW(), 1, 1, 0, '重置密码'),
    (2, 30, NOW(), NOW(), 1, 1, 0, '个人中心');

-- 给普通用户角色分配基础权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (3, 30, NOW(), NOW(), 1, 1, 0, '个人中心');

-- 初始化测试用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `salt`, `nickname`, `real_name`, `email`, `mobile`, `gender`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (2, 'user_manager', '$2a$10$7JB720yubVSOfvVWdF3iVu9S56RYNcP6bPT0UVc1JaDkHY1qnJdV6', 'user_manager_salt', '用户管理员', '张三', 'usermanager@example.com', '13800138001', 1, 1, NOW(), NOW(), 1, 1, 0, '用户管理员账户'),
    (3, 'ordinary_user', '$2a$10$7JB720yubVSOfvVWdF3iVu9S56RYNcP6bPT0UVc1JaDkHY1qnJdV6', 'ordinary_user_salt', '普通用户', '李四', 'user@example.com', '13800138002', 2, 1, NOW(), NOW(), 1, 1, 0, '普通用户账户');

-- 分配用户角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`, `remark`)
VALUES
    (1, 1, NOW(), NOW(), 1, 1, 0, 'admin用户分配超级管理员角色'),
    (2, 2, NOW(), NOW(), 1, 1, 0, 'user_manager用户分配用户管理员角色'),
    (3, 3, NOW(), NOW(), 1, 1, 0, 'ordinary_user用户分配普通用户角色');

