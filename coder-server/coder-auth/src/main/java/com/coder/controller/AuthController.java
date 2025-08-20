package com.coder.controller;

import com.coder.dto.ForgotPasswordDTO;
import com.coder.dto.LoginDTO;
import com.coder.dto.RegisterDTO;
import com.coder.dto.ResetPasswordDTO;
import com.coder.result.Result;
import com.coder.service.AuthService;
import com.coder.utils.JwtUtils;
import com.coder.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 认证控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/auth")
@Api(tags = "用户认证")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     * @param loginDTO 登录请求DTO
     * @return loginVO 登录响应VO
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 用户注册
     * @param registerDTO 注册请求DTO
     * @return message 注册响应信息
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        String message = authService.register(registerDTO);
        return Result.success(message);
    }

    /**
     * 用户登出
     * @param request HttpServletRequest
     * @return message 登出响应信息
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtUtils.getHeaderName());
        String token = jwtUtils.getTokenFromHeader(authHeader);
        
        String message = authService.logout(token);
        
        // 清除Shiro Subject
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
        }
        
        return Result.success(message);
    }

    /**
     * 忘记密码
     * @param forgotPasswordDTO 忘记密码请求DTO
     * @return message 忘记密码响应信息
     */
    @PostMapping("/forgot-password")
    @ApiOperation("忘记密码")
    public Result<String> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        String message = authService.forgotPassword(forgotPasswordDTO);
        return Result.success(message);
    }

    /**
     * 重置密码
     * @param resetPasswordDTO 重置密码请求DTO
     * @return message 重置密码响应信息
     */
    @PostMapping("/reset-password")
    @ApiOperation("重置密码")
    public Result<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        String message = authService.resetPassword(resetPasswordDTO);
        return Result.success(message);
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param type 验证码类型
     * @return message 发送邮箱验证码响应信息
     */
    @PostMapping("/send-email-code")
    @ApiOperation("发送邮箱验证码")
    public Result<String> sendEmailCode(
            @ApiParam(value = "邮箱地址", required = true)
            @RequestParam @NotBlank(message = "邮箱不能为空") @Email(message = "邮箱格式不正确") String email,
            @ApiParam(value = "验证码类型", required = true)
            @RequestParam @NotBlank(message = "类型不能为空") String type) {
        String message = authService.sendEmailCode(email, type);
        return Result.success(message);
    }

    /**
     * 刷新Token
     * @param request HttpServletRequest
     * @return loginVO 登录响应VO
     */
    @PostMapping("/refresh-token")
    @ApiOperation("刷新Token")
    public Result<LoginVO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtUtils.getHeaderName());
        String token = jwtUtils.getTokenFromHeader(authHeader);
        
        LoginVO loginVO = authService.refreshToken(token);
        return Result.success("Token刷新成功", loginVO);
    }

    /**
     * 获取当前用户信息
     * @return 响应信息
     */
    @GetMapping("/user-info")
    @ApiOperation("获取当前用户信息")
    public Result<Object> getCurrentUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Object principal = subject.getPrincipal();
            return Result.success("获取成功", principal);
        }
        return Result.failed("用户未登录");
    }
}