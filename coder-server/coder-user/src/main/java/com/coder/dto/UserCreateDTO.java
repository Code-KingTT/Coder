package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * 用户创建DTO
 *
 * @author Sunset
 * @date 2025-08-15
 */
@Data
@ApiModel(value = "UserCreateDTO", description = "用户创建请求对象")
public class UserCreateDTO {

    @ApiModelProperty(value = "用户名", example = "zhangsan", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,50}$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @ApiModelProperty(value = "昵称", example = "张三")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @ApiModelProperty(value = "真实姓名", example = "张三")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    @ApiModelProperty(value = "邮箱地址", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @ApiModelProperty(value = "性别：0-未知，1-男，2-女", example = "1")
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer gender;

    @ApiModelProperty(value = "生日", example = "1990-01-01")
    private LocalDate birthday;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;

}