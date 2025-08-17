package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色实体类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Role", description = "角色实体")
@TableName("sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编码，唯一标识
     */
    @ApiModelProperty(value = "角色编码", example = "ADMIN", required = true)
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", example = "系统管理员", required = true)
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述", example = "拥有系统所有权限")
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    private String roleDesc;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序", example = "1")
    private Integer sortOrder = 0;

    /**
     * 角色状态
     * 0-禁用，1-启用
     */
    @ApiModelProperty(value = "角色状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    // === 业务方法 ===

    /**
     * 判断角色是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return Integer.valueOf(1).equals(this.status);
    }

    /**
     * 判断角色是否禁用
     *
     * @return true-禁用，false-启用
     */
    public boolean isDisabled() {
        return Integer.valueOf(0).equals(this.status);
    }

    /**
     * 启用角色
     */
    public void enable() {
        this.status = 1;
    }

    /**
     * 禁用角色
     */
    public void disable() {
        this.status = 0;
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
        return "Role{" +
                "id=" + getId() +
                ", roleCode='" + roleCode + '\'' +
                ", roleName='" + roleName + '\'' +
                ", status=" + status +
                ", createTime=" + getCreateTime() +
                '}';
    }
}