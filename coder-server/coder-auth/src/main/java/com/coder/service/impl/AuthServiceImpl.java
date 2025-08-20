package com.coder.service.impl;

import com.coder.client.UserServiceClient;
import com.coder.constant.Constants;
import com.coder.dto.*;
import com.coder.exception.BusinessException;
import com.coder.result.Result;
import com.coder.result.ResultCode;
import com.coder.service.AuthService;
import com.coder.service.EmailService;
import com.coder.utils.EmailTemplateUtils;
import com.coder.utils.JwtUtils;
import com.coder.utils.RedisUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.LoginVO;
import com.coder.vo.UserPermissionVO;
import com.coder.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserServiceClient userServiceClient;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private EmailService emailService;

    @Resource
    private EmailTemplateUtils emailTemplateUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录，用户名：{}", loginDTO.getUsername());

        // 参数校验
        if (StrUtils.isBlank(loginDTO.getUsername()) || StrUtils.isBlank(loginDTO.getPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户名和密码不能为空");
        }

        try {
            // 检查Redis缓存
            Result<UserVO> userResult = userServiceClient.getUserByUsername(loginDTO.getUsername());
            if (userResult != null && userResult.isSuccess() && userResult.getData() != null) {
                UserVO user = userResult.getData();

                // 检查Redis中是否有有效的登录状态
                String loginKey = Constants.CacheKey.USER_LOGIN + user.getId();
                String existingToken = redisUtils.get(loginKey, String.class);

                if (StrUtils.isNotBlank(existingToken) && !jwtUtils.isTokenExpired(existingToken)) {
                    log.info("用户已登录，返回现有Token，用户名：{}", loginDTO.getUsername());

                    // 延期现有Token的过期时间
                    redisUtils.set(loginKey, existingToken, jwtUtils.getExpiration(), TimeUnit.SECONDS);

                    // 构建登录响应
                    return buildLoginVO(existingToken, user);
                }
            }

            // 未命中则使用Shiro进行认证
            Subject subject = SecurityUtils.getSubject();

            // 创建用户名密码Token
            UsernamePasswordToken token = new UsernamePasswordToken(
                    loginDTO.getUsername(),
                    loginDTO.getPassword()
            );

            // 设置记住我功能
            if (loginDTO.getRememberMe() != null && loginDTO.getRememberMe()) {
                token.setRememberMe(true);
                log.debug("启用记住我功能");
            }

            // Shiro认证
            subject.login(token);

            // 认证成功，获取用户信息
            String username = (String) subject.getPrincipal();
            log.info("Shiro认证成功，用户：{}", username);

            // 查询完整用户信息
            Result<UserVO> authUserResult = userServiceClient.getUserByUsername(username);
            if (authUserResult == null || !authUserResult.isSuccess() || authUserResult.getData() == null) {
                throw new BusinessException(ResultCode.LOGIN_ERROR, "获取用户信息失败");
            }

            UserVO user = authUserResult.getData();

            // 生成JWT Token
            String jwtToken = jwtUtils.generateToken(user.getUsername(), user.getId());

            // 缓存登录状态到Redis
            String loginKey = Constants.CacheKey.USER_LOGIN + user.getId();
            redisUtils.set(loginKey, jwtToken, jwtUtils.getExpiration(), TimeUnit.SECONDS);

            // 登出Shiro会话，会话管理由JWT处理
            subject.logout();

            log.info("用户登录成功，用户名：{}", username);
            return buildLoginVO(jwtToken, user);

        } catch (UnknownAccountException e) {
            log.warn("用户不存在：{}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "用户名或密码错误");
        } catch (IncorrectCredentialsException e) {
            log.warn("密码错误：{}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "用户名或密码错误");
        } catch (LockedAccountException e) {
            log.warn("账户被锁定：{}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "账户被锁定，请联系管理员");
        } catch (DisabledAccountException e) {
            log.warn("账户被禁用：{}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "账户被禁用，请联系管理员");
        } catch (ExcessiveAttemptsException e) {
            log.warn("登录尝试次数过多：{}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "登录失败次数过多，请稍后再试");
        } catch (AuthenticationException e) {
            log.error("Shiro认证失败：{}", e.getMessage());
            throw new BusinessException(ResultCode.LOGIN_ERROR, "用户名或密码错误");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户登录失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.LOGIN_ERROR, "登录失败，请稍后重试");
        }
    }

    /**
     * 构建登录响应对象
     */
    private LoginVO buildLoginVO(String token, UserVO user) {
        // 查询用户权限信息
        Result<UserPermissionVO> permissionResult = userServiceClient.getUserPermissionInfo(user.getId());
        UserPermissionVO permissions = null;
        if (permissionResult != null && permissionResult.isSuccess() && permissionResult.getData() != null) {
            permissions = permissionResult.getData();
        }

        // 构建登录响应
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(token);
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(jwtUtils.getExpiration());
        loginVO.setUserInfo(user);
        loginVO.setPermissions(permissions);

        return loginVO;
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        log.info("用户注册，用户名：{}，邮箱：{}", registerDTO.getUsername(), registerDTO.getEmail());

        // 验证密码一致性
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "两次输入的密码不一致");
        }

        // 验证邮箱验证码
        String cacheKey = Constants.CacheKey.EMAIL_CODE_PREFIX + "register:" + registerDTO.getEmail();
        String cachedCode = redisUtils.get(cacheKey, String.class);
        if (cachedCode == null || !cachedCode.equals(registerDTO.getEmailCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱验证码错误或已过期");
        }

        try {
            // 检查用户名是否存在
            Result<Boolean> usernameExistsResult = userServiceClient.checkUsernameExists(registerDTO.getUsername());
            if (usernameExistsResult != null && usernameExistsResult.isSuccess() && Boolean.TRUE.equals(usernameExistsResult.getData())) {
                throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "用户名已存在");
            }

            // 构建用户创建DTO
            RegisterUserDTO createDTO = new RegisterUserDTO();
            createDTO.setUsername(registerDTO.getUsername());
            createDTO.setPassword(registerDTO.getPassword());
            createDTO.setEmail(registerDTO.getEmail());
            createDTO.setNickname(StrUtils.isNotBlank(registerDTO.getNickname()) ? registerDTO.getNickname() : registerDTO.getUsername());
            createDTO.setRemark("邮箱注册用户");

            // 调用用户服务创建用户
            Result<Long> createResult = userServiceClient.createUser(createDTO);
            if (createResult == null || !createResult.isSuccess()) {
                throw new BusinessException(ResultCode.OPERATION_FAILED, "用户注册失败");
            }

            // 删除验证码缓存
            redisUtils.delete(cacheKey);

            log.info("用户注册成功，用户名：{}", registerDTO.getUsername());
            return "注册成功";

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户注册失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "注册失败");
        }
    }

    @Override
    public String logout(String token) {

        Long userId = jwtUtils.getUserIdFromToken(token);
        String loginKey = Constants.CacheKey.USER_LOGIN + userId;

        // 判断当前Token是否有效
        if (!redisUtils.hasKey(loginKey)) {
            return "已退出登录";
        }

        if (StrUtils.isBlank(token)) {
            return "登出成功";
        }

        try {
            redisUtils.delete(loginKey);

            log.info("用户登出成功，用户ID：{}", userId);
            return "登出成功";

        } catch (Exception e) {
            log.error("用户登出处理失败：{}", e.getMessage(), e);
            return "登出成功";
        }
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        log.info("忘记密码，邮箱：{}", forgotPasswordDTO.getEmail());

        try {
            // 发送重置密码验证码
            return sendEmailCode(forgotPasswordDTO.getEmail(), "reset");

        }  catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("忘记密码处理失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "发送重置邮件失败");
        }
    }

    @Override
    public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
        log.info("重置密码，邮箱：{}", resetPasswordDTO.getEmail());

        // 验证密码一致性
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "两次输入的密码不一致");
        }

        // 验证邮箱验证码
        String cacheKey = Constants.CacheKey.EMAIL_CODE_PREFIX + "reset:" + resetPasswordDTO.getEmail();
        String cachedCode = redisUtils.get(cacheKey, String.class);
        if (cachedCode == null || !cachedCode.equals(resetPasswordDTO.getEmailCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱验证码错误或已过期");
        }

        try {
            // 根据邮箱修改密码
            userServiceClient.updatePasswordByEmail(resetPasswordDTO.getEmail(), resetPasswordDTO.getNewPassword());

            // 删除验证码缓存
            redisUtils.delete(cacheKey);

            return "密码重置成功";

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("重置密码失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "密码重置失败");
        }
    }

    @Override
    public String sendEmailCode(String email, String type) {
        log.info("发送邮箱验证码，邮箱：{}，类型：{}", email, type);

        if (StrUtils.isBlank(email) || StrUtils.isBlank(type)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱和类型不能为空");
        }

        // 检查发送频率限制
        String rateLimitKey = Constants.CacheKey.RATE_LIMIT + "email:" + email;
        if (redisUtils.hasKey(rateLimitKey)) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "发送过于频繁，请一分钟后再试");
        }

        try {
            // 生成6位验证码
            String code = generateEmailCode();

            // 缓存验证码，有效期5分钟
            String cacheKey = Constants.CacheKey.EMAIL_CODE_PREFIX + type + ":" + email;
            redisUtils.set(cacheKey, code, Constants.CacheExpire.SMS_CODE_EXPIRE, TimeUnit.SECONDS);

            // 发送邮件
            String subject = getEmailSubject(type);
            String content = getEmailContent(type, code);
            emailService.sendHtmlEmail(email, subject, content);

            // 设置发送频率限制，1分钟
            redisUtils.set(rateLimitKey, "1", Constants.CacheExpire.ONE_MINUTE, TimeUnit.SECONDS);

            log.info("邮箱验证码发送成功，邮箱：{}", email);
            return "验证码已发送到您的邮箱";

        } catch (Exception e) {
            log.error("发送邮箱验证码失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "验证码发送失败");
        }
    }

    @Override
    public LoginVO refreshToken(String token) {
        log.info("刷新Token");

        if (StrUtils.isBlank(token)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "Token不能为空");
        }

        try {
            // 验证Token
            String username = jwtUtils.getUsernameFromToken(token);
            if (!jwtUtils.validateToken(token, username)) {
                throw new BusinessException(ResultCode.UNAUTHORIZED, "Token无效");
            }

            // 生成新Token
            Long userId = jwtUtils.getUserIdFromToken(token);
            String newToken = jwtUtils.generateToken(username, userId);

            // 查询用户信息
            Result<UserVO> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || !userResult.isSuccess() || userResult.getData() == null) {
                throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户不存在");
            }

            UserVO user = userResult.getData();

            // 查询用户权限信息
            Result<UserPermissionVO> permissionResult = userServiceClient.getUserPermissionInfo(user.getId());
            UserPermissionVO permissions = null;
            if (permissionResult != null && permissionResult.isSuccess() && permissionResult.getData() != null) {
                permissions = permissionResult.getData();
            }

            // 构建响应
            LoginVO loginVO = new LoginVO();
            loginVO.setAccessToken(newToken);
            loginVO.setTokenType("Bearer");
            loginVO.setExpiresIn(jwtUtils.getExpiration());
            loginVO.setUserInfo(user);
            loginVO.setPermissions(permissions);

            // 更新登录状态缓存
            String loginKey = Constants.CacheKey.USER_LOGIN + user.getId();
            redisUtils.set(loginKey, newToken, jwtUtils.getExpiration(), TimeUnit.SECONDS);

            log.info("Token刷新成功，用户：{}", username);
            return loginVO;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Token刷新失败：{}", e.getMessage(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "Token刷新失败");
        }
    }

    /**
     * 生成邮箱验证码
     */
    private String generateEmailCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 获取邮件主题
     */
    private String getEmailSubject(String type) {
        switch (type) {
            case "register":
                return "【Coder】注册验证码";
            case "reset":
                return "【Coder】密码重置验证码";
            default:
                return "【Coder】验证码";
        }
    }

    /**
     * 获取邮件内容
     */
    private String getEmailContent(String type, String code) {
        String purpose = "register".equals(type) ? "注册账户" : "重置密码";
        String actionText = "register".equals(type) ? "完成注册" : "重置密码";
        String welcomeText = "register".equals(type) ? "欢迎加入Coder大家庭！" : "密码重置验证";

        // 准备模板变量
        Map<String, String> variables = new HashMap<>();
        variables.put("purpose", purpose);
        variables.put("actionText", actionText);
        variables.put("welcomeText", welcomeText);
        variables.put("code", code);

        // 渲染模板
        return emailTemplateUtils.renderTemplate("templates/email-verification.html", variables);
    }
}