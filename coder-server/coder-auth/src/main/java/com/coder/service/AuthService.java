package com.coder.service;

import com.coder.dto.ForgotPasswordDTO;
import com.coder.dto.LoginDTO;
import com.coder.dto.RegisterDTO;
import com.coder.dto.ResetPasswordDTO;
import com.coder.vo.LoginVO;

/**
 * 认证服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    String register(RegisterDTO registerDTO);

    /**
     * 用户登出
     *
     * @param token JWT Token
     * @return 登出结果
     */
    String logout(String token);

    /**
     * 忘记密码（发送重置邮件）
     *
     * @param forgotPasswordDTO 忘记密码信息
     * @return 处理结果
     */
    String forgotPassword(ForgotPasswordDTO forgotPasswordDTO);

    /**
     * 重置密码
     *
     * @param resetPasswordDTO 重置密码信息
     * @return 重置结果
     */
    String resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱地址
     * @param type  验证码类型（register-注册，reset-重置密码）
     * @return 发送结果
     */
    String sendEmailCode(String email, String type);

    /**
     * 刷新Token
     *
     * @param token 当前Token
     * @return 新的Token信息
     */
    LoginVO refreshToken(String token);
}