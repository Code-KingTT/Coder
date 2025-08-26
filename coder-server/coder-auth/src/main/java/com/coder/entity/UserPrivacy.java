package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户隐私信息实体类
 *
 * @author Sunset
 * @since 2025-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserPrivacy对象", description = "用户隐私信息表")
@TableName("sys_user_privacy")
public class UserPrivacy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "客户端IP地址", required = true)
    private String ipAddress;

    @ApiModelProperty(value = "真实IP地址（X-Real-IP）")
    private String realIp;

    @ApiModelProperty(value = "转发IP链（X-Forwarded-For）")
    private String forwardedFor;

    @ApiModelProperty(value = "代理IP地址")
    private String proxyIp;

    @ApiModelProperty(value = "完整User-Agent字符串")
    private String userAgent;

    @ApiModelProperty(value = "浏览器名称")
    private String browserName;

    @ApiModelProperty(value = "浏览器版本")
    private String browserVersion;

    @ApiModelProperty(value = "浏览器引擎")
    private String browserEngine;

    @ApiModelProperty(value = "Accept请求头")
    private String accept;

    @ApiModelProperty(value = "Accept-Language请求头")
    private String acceptLanguage;

    @ApiModelProperty(value = "Accept-Encoding请求头")
    private String acceptEncoding;

    @ApiModelProperty(value = "操作系统名称")
    private String osName;

    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;

    @ApiModelProperty(value = "系统架构")
    private String osArch;

    @ApiModelProperty(value = "设备类型：PC-电脑，MOBILE-手机，TABLET-平板，UNKNOWN-未知")
    private String deviceType;

    @ApiModelProperty(value = "设备品牌")
    private String deviceBrand;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "来源页面")
    private String referer;

    @ApiModelProperty(value = "请求方法")
    private String requestMethod;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "Host请求头")
    private String host;

    @ApiModelProperty(value = "Origin请求头")
    private String origin;

    @ApiModelProperty(value = "登录类型：WEB-网页，APP-应用，API-接口", required = true)
    private String loginType;

    @ApiModelProperty(value = "登录结果：0-失败，1-成功", required = true)
    private Integer loginResult;

    @ApiModelProperty(value = "登录持续时间（毫秒）")
    private Long loginDuration;

    @ApiModelProperty(value = "完整请求头信息（JSON格式）")
    private String requestHeaders;
}