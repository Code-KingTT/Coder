package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 角色更新DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RoleUpdateDTO", description = "角色更新请求对象")
public class RoleUpdateDTO {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long id;

    @ApiModelProperty(value = "角色编码", example = "ADMIN")
    @Size(min = 2, max = 50, message = "角色编码长度必须在2-50个字符之间")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "角色编码只能包含大写字母、数字和下划线")
    private String roleCode;

    @ApiModelProperty(value = "角色名称", example = "系统管理员")
    @Size(min = 2, max = 100, message = "角色名称长度必须在2-100个字符之间")
    private String roleName;

    @ApiModelProperty(value = "角色描述", example = "拥有系统所有权限")
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    private String roleDesc;

    @ApiModelProperty(value = "显示顺序", example = "1")
    @Min(value = 0, message = "显示顺序不能小于0")
    @Max(value = 9999, message = "显示顺序不能大于9999")
    private Integer sortOrder;

    @ApiModelProperty(value = "角色状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "角色状态值不正确")
    @Max(value = 1, message = "角色状态值不正确")
    private Integer status;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}