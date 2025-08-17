package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户角色关联实体类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserRole", description = "用户角色关联实体")
@TableName("sys_user_role")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    // === 构造方法 ===

    public UserRole() {
        super();
    }

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", createTime=" + getCreateTime() +
                '}';
    }
}