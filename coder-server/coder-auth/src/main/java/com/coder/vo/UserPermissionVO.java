package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户权限信息VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "UserPermissionVO", description = "用户权限信息响应对象")
public class UserPermissionVO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "拥有的角色列表")
    private List<RoleVO> roles;

    @ApiModelProperty(value = "拥有的菜单树")
    private List<MenuTreeVO> menuTree;

    @ApiModelProperty(value = "拥有的权限标识列表")
    private List<String> permissions;

    @ApiModelProperty(value = "角色编码列表")
    private List<String> roleCodes;
}