package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 角色菜单关联实体类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "RoleMenu", description = "角色菜单关联实体")
@TableName("sys_role_menu")
public class RoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID", required = true)
    @NotNull(message = "菜单ID不能为空")
    private Long menuId;

    // === 构造方法 ===

    public RoleMenu() {
        super();
    }

    public RoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
                "id=" + getId() +
                ", roleId=" + roleId +
                ", menuId=" + menuId +
                ", createTime=" + getCreateTime() +
                '}';
    }
}