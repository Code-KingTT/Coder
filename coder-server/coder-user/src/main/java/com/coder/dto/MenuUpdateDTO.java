package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 菜单更新DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "MenuUpdateDTO", description = "菜单更新请求对象")
public class MenuUpdateDTO {

    @ApiModelProperty(value = "菜单ID", required = true)
    @NotNull(message = "菜单ID不能为空")
    private Long id;

    @ApiModelProperty(value = "菜单名称", example = "用户管理")
    @Size(min = 2, max = 100, message = "菜单名称长度必须在2-100个字符之间")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID", example = "0")
    @Min(value = 0, message = "父菜单ID不能小于0")
    private Long parentId;

    @ApiModelProperty(value = "菜单类型：1-目录，2-菜单，3-按钮", example = "2")
    @Min(value = 1, message = "菜单类型值不正确")
    @Max(value = 3, message = "菜单类型值不正确")
    private Integer menuType;

    @ApiModelProperty(value = "路由路径", example = "/system/user")
    @Size(max = 200, message = "路由路径长度不能超过200个字符")
    private String path;

    @ApiModelProperty(value = "组件路径", example = "system/user/index")
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    private String component;

    @ApiModelProperty(value = "权限标识", example = "system:user:list")
    @Size(max = 200, message = "权限标识长度不能超过200个字符")
    private String permission;

    @ApiModelProperty(value = "菜单图标", example = "el-icon-user")
    @Size(max = 100, message = "菜单图标长度不能超过100个字符")
    private String icon;

    @ApiModelProperty(value = "显示顺序", example = "1")
    @Min(value = 0, message = "显示顺序不能小于0")
    @Max(value = 9999, message = "显示顺序不能大于9999")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否显示：0-隐藏，1-显示", example = "1")
    @Min(value = 0, message = "显示状态值不正确")
    @Max(value = 1, message = "显示状态值不正确")
    private Integer visible;

    @ApiModelProperty(value = "菜单状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "菜单状态值不正确")
    @Max(value = 1, message = "菜单状态值不正确")
    private Integer status;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}