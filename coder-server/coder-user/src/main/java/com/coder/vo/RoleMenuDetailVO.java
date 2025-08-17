package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单详情VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RoleMenuDetailVO", description = "角色菜单详情响应对象")
public class RoleMenuDetailVO {

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "拥有的菜单列表")
    private List<MenuVO> menus;

    @ApiModelProperty(value = "拥有的菜单树")
    private List<MenuTreeVO> menuTree;

    @ApiModelProperty(value = "菜单ID列表")
    private List<Long> menuIds;

    @ApiModelProperty(value = "权限标识列表")
    private List<String> permissions;
}