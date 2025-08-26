package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色权限统计VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RolePermissionStatsVO", description = "角色权限统计响应对象")
public class RolePermissionStatsVO {

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "拥有用户数量")
    private Integer userCount;

    @ApiModelProperty(value = "拥有菜单数量")
    private Integer menuCount;

    @ApiModelProperty(value = "拥有权限数量")
    private Integer permissionCount;

    @ApiModelProperty(value = "角色状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;
}