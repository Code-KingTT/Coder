package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 重置密码请求DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "ResetPasswordDTO", description = "重置密码请求对象")
public class ResetPasswordDTO {

    @ApiModelProperty(value = "邮箱地址", example = "user@example.com", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @ApiModelProperty(value = "邮箱验证码", required = true)
    @NotBlank(message = "邮箱验证码不能为空")
    @Size(min = 4, max = 8, message = "验证码长度不正确")
    private String emailCode;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String newPassword;

    @ApiModelProperty(value = "确认新密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}