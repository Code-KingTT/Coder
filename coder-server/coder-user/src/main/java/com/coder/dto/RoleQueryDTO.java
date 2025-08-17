package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 角色查询DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RoleQueryDTO", description = "角色查询请求对象")
public class RoleQueryDTO {

    @ApiModelProperty(value = "角色编码", example = "ADMIN")
    private String roleCode;

    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "角色状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "角色状态值不正确")
    @Max(value = 1, message = "角色状态值不正确")
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