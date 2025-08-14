package com.coder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author Sunset
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User", description = "用户实体")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名，唯一标识
     */
    @ApiModelProperty(value = "用户名", example = "zhangsan", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,50}$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    /**
     * 密码（加密后）
     */
    @ApiModelProperty(value = "密码", hidden = true)
    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Size(max = 255, message = "密码长度不能超过255个字符")
    private String password;

    /**
     * 密码盐值
     */
    @ApiModelProperty(value = "密码盐值", hidden = true)
    @JsonIgnore
    @Size(max = 64, message = "盐值长度不能超过64个字符")
    private String salt;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", example = "张三")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", example = "张三")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱地址", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份", example = "广东省")
    @Size(max = 50, message = "省份长度不能超过50个字符")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市", example = "深圳市")
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty(value = "区县", example = "南山区")
    @Size(max = 50, message = "区县长度不能超过50个字符")
    private String district;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址", example = "科技园南区")
    @Size(max = 255, message = "详细地址长度不能超过255个字符")
    private String address;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatar;

    /**
     * 性别
     * 0-未知，1-男，2-女
     */
    @ApiModelProperty(value = "性别：0-未知，1-男，2-女", example = "1")
    private Integer gender;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日", example = "1990-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简介", example = "这是一个测试用户")
    @Size(max = 1000, message = "个人简介长度不能超过1000个字符")
    private String profile;

    /**
     * 账户状态
     * 0-禁用，1-正常，2-锁定
     */
    @ApiModelProperty(value = "账户状态：0-禁用，1-正常，2-锁定", example = "1")
    private Integer status = 1;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间", example = "2025-01-27 10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @ApiModelProperty(value = "最后登录IP", example = "192.168.1.100")
    @Size(max = 50, message = "登录IP长度不能超过50个字符")
    private String lastLoginIp;

    /**
     * 登录次数
     */
    @ApiModelProperty(value = "登录次数", example = "10")
    private Integer loginCount = 0;

    /**
     * 连续登录失败次数
     */
    @ApiModelProperty(value = "连续登录失败次数", hidden = true)
    @JsonIgnore
    private Integer failedLoginCount = 0;

    /**
     * 账户锁定时间
     */
    @ApiModelProperty(value = "账户锁定时间", hidden = true)
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lockedTime;

    /**
     * 密码最后修改时间
     */
    @ApiModelProperty(value = "密码最后修改时间", hidden = true)
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime passwordUpdateTime;

    /**
     * 是否启用双因子认证
     * 0-否，1-是
     */
    @ApiModelProperty(value = "是否启用双因子认证", hidden = true)
    @JsonIgnore
    private Integer twoFactorEnabled = 0;

    /**
     * 双因子认证密钥
     */
    @ApiModelProperty(value = "双因子认证密钥", hidden = true)
    @JsonIgnore
    @Size(max = 32, message = "双因子认证密钥长度不能超过32个字符")
    private String twoFactorSecret;

    /**
     * 用户标签，多个标签用逗号分隔
     */
    @ApiModelProperty(value = "用户标签，多个标签用逗号分隔", example = "程序员,技术爱好者")
    @Size(max = 500, message = "用户标签长度不能超过500个字符")
    private String tags;

    /**
     * 用户来源
     * REGISTER-注册，IMPORT-导入，THIRD_PARTY-第三方
     */
    @ApiModelProperty(value = "用户来源：REGISTER-注册，IMPORT-导入，THIRD_PARTY-第三方", example = "REGISTER")
    @Size(max = 20, message = "用户来源长度不能超过20个字符")
    private String source = "REGISTER";

    /**
     * 第三方平台用户ID
     */
    @ApiModelProperty(value = "第三方平台用户ID", example = "wx_123456")
    @Size(max = 100, message = "第三方平台用户ID长度不能超过100个字符")
    private String thirdPartyId;

    /**
     * 第三方平台类型
     * WECHAT-微信，QQ，ALIPAY-支付宝等
     */
    @ApiModelProperty(value = "第三方平台类型：WECHAT-微信，QQ，ALIPAY-支付宝等", example = "WECHAT")
    @Size(max = 20, message = "第三方平台类型长度不能超过20个字符")
    private String thirdPartyType;


    // === 业务方法 ===

    /**
     * 判断账户是否正常
     *
     * @return true-正常，false-异常
     */
    public boolean isAccountNormal() {
        return Integer.valueOf(1).equals(this.status);
    }

    /**
     * 判断账户是否被禁用
     *
     * @return true-禁用，false-未禁用
     */
    public boolean isAccountDisabled() {
        return Integer.valueOf(0).equals(this.status);
    }

    /**
     * 判断账户是否被锁定
     *
     * @return true-锁定，false-未锁定
     */
    public boolean isAccountLocked() {
        return Integer.valueOf(2).equals(this.status);
    }

    /**
     * 判断是否启用了双因子认证
     *
     * @return true-启用，false-未启用
     */
    public boolean isTwoFactorEnabled() {
        return Integer.valueOf(1).equals(this.twoFactorEnabled);
    }

    /**
     * 获取性别描述
     *
     * @return 性别描述
     */
    public String getGenderDesc() {
        if (this.gender == null) {
            return "未知";
        }
        switch (this.gender) {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "未知";
        }
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
                return "正常";
            case 2:
                return "锁定";
            default:
                return "未知";
        }
    }

    /**
     * 获取完整地址
     *
     * @return 完整地址字符串
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null && !province.trim().isEmpty()) {
            sb.append(province);
        }
        if (city != null && !city.trim().isEmpty()) {
            sb.append(city);
        }
        if (district != null && !district.trim().isEmpty()) {
            sb.append(district);
        }
        if (address != null && !address.trim().isEmpty()) {
            sb.append(address);
        }
        return sb.toString();
    }

    /**
     * 设置密码（同时设置密码修改时间）
     *
     * @param password 密码
     */
    public void setPasswordWithTime(String password) {
        this.password = password;
        this.passwordUpdateTime = LocalDateTime.now();
    }

    /**
     * 登录成功处理
     *
     * @param loginIp 登录IP
     */
    public void handleLoginSuccess(String loginIp) {
        this.lastLoginTime = LocalDateTime.now();
        this.lastLoginIp = loginIp;
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
        this.failedLoginCount = 0; // 重置失败次数
        if (Integer.valueOf(2).equals(this.status)) { // 如果是锁定状态，解锁
            this.status = 1;
            this.lockedTime = null;
        }
    }

    /**
     * 登录失败处理
     *
     * @param maxFailedCount 最大失败次数
     * @return 是否需要锁定账户
     */
    public boolean handleLoginFailed(int maxFailedCount) {
        this.failedLoginCount = (this.failedLoginCount == null ? 0 : this.failedLoginCount) + 1;
        if (this.failedLoginCount >= maxFailedCount) {
            this.status = 2; // 锁定账户
            this.lockedTime = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * 重置登录失败次数
     */
    public void resetFailedLoginCount() {
        this.failedLoginCount = 0;
    }

    /**
     * 解锁账户
     */
    public void unlockAccount() {
        this.status = 1;
        this.lockedTime = null;
        this.failedLoginCount = 0;
    }

    /**
     * 锁定账户
     */
    public void lockAccount() {
        this.status = 2;
        this.lockedTime = LocalDateTime.now();
    }

    /**
     * 禁用账户
     */
    public void disableAccount() {
        this.status = 0;
    }

    /**
     * 启用账户
     */
    public void enableAccount() {
        this.status = 1;
        this.lockedTime = null;
        this.failedLoginCount = 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createTime=" + getCreateTime() +
                '}';
    }
}
