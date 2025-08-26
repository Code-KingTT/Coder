package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色菜单分配DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RoleMenuAssignDTO", description = "角色菜单分配请求对象")
public class RoleMenuAssignDTO {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID列表", required = true)
    @NotEmpty(message = "菜单ID列表不能为空")
    private List<Long> menuIds;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}