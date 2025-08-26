package com.coder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色菜单VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RoleMenuVO", description = "角色菜单响应对象")
public class RoleMenuVO {

    @ApiModelProperty(value = "关联ID")
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单类型：1-目录，2-菜单，3-按钮")
    private Integer menuType;

    @ApiModelProperty(value = "菜单类型描述")
    private String menuTypeDesc;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}