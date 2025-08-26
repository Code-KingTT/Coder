package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册DTO（用于微服务间调用）
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "RegisterUserDTO", description = "用户注册请求对象")
public class RegisterUserDTO {

    @ApiModelProperty(value = "用户名", example = "john_doe", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @ApiModelProperty(value = "邮箱", example = "john@example.com", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "昵称", example = "John")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @ApiModelProperty(value = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}