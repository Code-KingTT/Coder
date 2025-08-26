package com.coder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单树形VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "MenuTreeVO", description = "菜单树形响应对象")
public class MenuTreeVO {

    @ApiModelProperty(value = "菜单ID")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单类型：1-目录，2-菜单，3-按钮")
    private Integer menuType;

    @ApiModelProperty(value = "菜单类型描述")
    private String menuTypeDesc;

    @ApiModelProperty(value = "路由路径")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "显示顺序")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否显示：0-隐藏，1-显示")
    private Integer visible;

    @ApiModelProperty(value = "显示状态描述")
    private String visibleDesc;

    @ApiModelProperty(value = "菜单状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "子菜单列表")
    private List<MenuTreeVO> children;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}