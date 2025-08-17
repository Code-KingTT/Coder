package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户角色分配DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "UserRoleAssignDTO", description = "用户角色分配请求对象")
public class UserRoleAssignDTO {

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "角色ID列表", required = true)
    @NotEmpty(message = "角色ID列表不能为空")
    private List<Long> roleIds;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}