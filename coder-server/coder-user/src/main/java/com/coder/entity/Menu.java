package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 菜单实体类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Menu", description = "菜单实体")
@TableName("sys_menu")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", example = "用户管理", required = true)
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 100, message = "菜单名称长度不能超过100个字符")
    private String menuName;

    /**
     * 父菜单ID，0表示顶级菜单
     */
    @ApiModelProperty(value = "父菜单ID", example = "0")
    private Long parentId = 0L;

    /**
     * 菜单类型
     * 1-目录，2-菜单，3-按钮
     */
    @ApiModelProperty(value = "菜单类型：1-目录，2-菜单，3-按钮", example = "2", required = true)
    @NotNull(message = "菜单类型不能为空")
    private Integer menuType = 2;

    /**
     * 路由路径
     */
    @ApiModelProperty(value = "路由路径", example = "/system/user")
    @Size(max = 200, message = "路由路径长度不能超过200个字符")
    private String path;

    /**
     * 组件路径
     */
    @ApiModelProperty(value = "组件路径", example = "system/user/index")
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    private String component;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识", example = "system:user:list")
    @Size(max = 200, message = "权限标识长度不能超过200个字符")
    private String permission;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标", example = "el-icon-user")
    @Size(max = 100, message = "菜单图标长度不能超过100个字符")
    private String icon;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer sortOrder = 0;

    /**
     * 是否显示
     * 0-隐藏，1-显示
     */
    @ApiModelProperty(value = "是否显示：0-隐藏，1-显示", example = "1")
    private Integer visible = 1;

    /**
     * 菜单状态
     * 0-禁用，1-启用
     */
    @ApiModelProperty(value = "菜单状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    // === 业务方法 ===

    /**
     * 判断是否为顶级菜单
     *
     * @return true-顶级菜单，false-子菜单
     */
    public boolean isTopLevel() {
        return Long.valueOf(0L).equals(this.parentId);
    }

    /**
     * 判断是否为目录类型
     *
     * @return true-目录，false-非目录
     */
    public boolean isDirectory() {
        return Integer.valueOf(1).equals(this.menuType);
    }

    /**
     * 判断是否为菜单类型
     *
     * @return true-菜单，false-非菜单
     */
    public boolean isMenu() {
        return Integer.valueOf(2).equals(this.menuType);
    }

    /**
     * 判断是否为按钮类型
     *
     * @return true-按钮，false-非按钮
     */
    public boolean isButton() {
        return Integer.valueOf(3).equals(this.menuType);
    }

    /**
     * 判断菜单是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return Integer.valueOf(1).equals(this.status);
    }

    /**
     * 判断菜单是否显示
     *
     * @return true-显示，false-隐藏
     */
    public boolean isVisible() {
        return Integer.valueOf(1).equals(this.visible);
    }

    /**
     * 获取菜单类型描述
     *
     * @return 菜单类型描述
     */
    public String getMenuTypeDesc() {
        if (this.menuType == null) {
            return "未知";
        }
        switch (this.menuType) {
            case 1:
                return "目录";
            case 2:
                return "菜单";
            case 3:
                return "按钮";
            default:
                return "未知";
        }
    }

    /**
     * 获取状态描述
     *
     * @return 状态描述
     */
    public String getStatusDesc() {
        if (this.status == null) {
            return "未知";
        }
        switch (this.status) {
            case 0:
                return "禁用";
            case 1:
                return "启用";
            default:
                return "未知";
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + getId() +
                ", menuName='" + menuName + '\'' +
                ", parentId=" + parentId +
                ", menuType=" + menuType +
                ", permission='" + permission + '\'' +
                ", status=" + status +
                ", createTime=" + getCreateTime() +
                '}';
    }
}