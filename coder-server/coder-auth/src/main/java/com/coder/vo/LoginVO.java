package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录响应VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "LoginVO", description = "登录响应对象")
public class LoginVO {

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "令牌类型")
    private String tokenType = "Bearer";

    @ApiModelProperty(value = "过期时间（秒）")
    private Long expiresIn;

    @ApiModelProperty(value = "用户信息")
    private UserVO userInfo;

    @ApiModelProperty(value = "用户权限信息")
    private UserPermissionVO permissions;
}