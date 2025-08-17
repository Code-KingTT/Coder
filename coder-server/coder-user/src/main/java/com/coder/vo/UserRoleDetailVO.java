package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户角色详情VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "UserRoleDetailVO", description = "用户角色详情响应对象")
public class UserRoleDetailVO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "拥有的角色列表")
    private List<RoleVO> roles;

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIds;

    @ApiModelProperty(value = "角色名称列表（逗号分隔）")
    private String roleNames;
}