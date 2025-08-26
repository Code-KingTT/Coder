package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 菜单查询DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "MenuQueryDTO", description = "菜单查询请求对象")
public class MenuQueryDTO {

    @ApiModelProperty(value = "菜单名称", example = "用户")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID", example = "0")
    @Min(value = 0, message = "父菜单ID不能小于0")
    private Long parentId;

    @ApiModelProperty(value = "菜单类型：1-目录，2-菜单，3-按钮", example = "2")
    @Min(value = 1, message = "菜单类型值不正确")
    @Max(value = 3, message = "菜单类型值不正确")
    private Integer menuType;

    @ApiModelProperty(value = "权限标识", example = "system:user")
    private String permission;

    @ApiModelProperty(value = "是否显示：0-隐藏，1-显示", example = "1")
    @Min(value = 0, message = "显示状态值不正确")
    @Max(value = 1, message = "显示状态值不正确")
    private Integer visible;

    @ApiModelProperty(value = "菜单状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "菜单状态值不正确")
    @Max(value = 1, message = "菜单状态值不正确")
    private Integer status;

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能大于100")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段", example = "sortOrder")
    private String orderBy = "sortOrder";

    @ApiModelProperty(value = "排序方向：asc-升序，desc-降序", example = "asc")
    private String orderDirection = "asc";
}