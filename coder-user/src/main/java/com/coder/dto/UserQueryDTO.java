package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询DTO
 *
 * @author Sunset
 * @date 2025-08-15
 */
@Data
@ApiModel(value = "UserQueryDTO", description = "用户查询请求对象")
public class UserQueryDTO {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "性别：0-未知，1-男，2-女")
    private Integer gender;

    @ApiModelProperty(value = "账户状态：0-禁用，1-正常，2-锁定")
    private Integer status;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}