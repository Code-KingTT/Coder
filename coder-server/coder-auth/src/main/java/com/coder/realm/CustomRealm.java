package com.coder.realm;

import com.coder.client.UserServiceClient;
import com.coder.result.Result;
import com.coder.vo.UserPermissionVO;
import com.coder.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Shiro Realm
 * 负责用户认证和授权
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserServiceClient userServiceClient;

    /**
     * 指定支持的Token类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 授权方法
     * 获取用户的角色和权限信息
     *
     * @param principals 用户身份信息
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("CustomRealm开始授权...");

        // 获取用户名
        String username = (String) principals.getPrimaryPrincipal();
        log.debug("当前用户: {}", username);

        try {
            // 查询用户信息
            Result<UserVO> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || !userResult.isSuccess() || userResult.getData() == null) {
                log.warn("用户不存在: {}", username);
                return null;
            }

            UserVO user = userResult.getData();

            // 查询用户权限信息
            Result<UserPermissionVO> permissionResult = userServiceClient.getUserPermissionInfo(user.getId());
            if (permissionResult == null || !permissionResult.isSuccess() || permissionResult.getData() == null) {
                log.warn("用户权限信息不存在: {}", username);
                return new SimpleAuthorizationInfo();
            }

            UserPermissionVO userPermission = permissionResult.getData();

            // 构建授权信息
            SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();

            // 添加角色
            if (userPermission.getRoleCodes() != null && !userPermission.getRoleCodes().isEmpty()) {
                Set<String> roles = new HashSet<>(userPermission.getRoleCodes());
                authInfo.setRoles(roles);
                log.debug("用户角色: {}", roles);
            }

            // 添加权限
            if (userPermission.getPermissions() != null && !userPermission.getPermissions().isEmpty()) {
                Set<String> permissions = new HashSet<>(userPermission.getPermissions());
                authInfo.setStringPermissions(permissions);
                log.debug("用户权限: {}", permissions);
            }

            return authInfo;

        } catch (Exception e) {
            log.error("获取用户授权信息失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 认证方法
     * 验证用户身份
     *
     * @param token 认证令牌
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("CustomRealm开始认证...");

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = String.valueOf(upToken.getPassword());

        log.debug("认证用户: {}", username);

        try {
            // 查询用户信息
            Result<UserVO> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || !userResult.isSuccess() || userResult.getData() == null) {
                log.warn("用户不存在: {}", username);
                throw new UnknownAccountException("用户不存在");
            }

            UserVO user = userResult.getData();

            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                log.warn("用户账户被禁用: {}", username);
                throw new DisabledAccountException("账户被禁用");
            }

            // 🔑 关键：调用用户服务验证密码
            Result<Boolean> passwordResult = userServiceClient.validatePassword(username, password);
            if (passwordResult == null || !passwordResult.isSuccess() || !Boolean.TRUE.equals(passwordResult.getData())) {
                log.warn("密码验证失败: {}", username);
                throw new IncorrectCredentialsException("密码错误");
            }

            log.debug("用户认证成功: {}", username);

            // 返回认证信息 - 密码已验证，这里可以返回任意值
            return new SimpleAuthenticationInfo(
                    username,                           // 用户名作为principal
                    password,                          // 已验证的密码
                    ByteSource.Util.bytes(username),  // 盐值
                    getName()                          // Realm名称
            );

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户认证失败: {}", e.getMessage(), e);
            throw new AuthenticationException("认证失败", e);
        }
    }

    /**
     * 清除指定用户的授权缓存
     *
     * @param username 用户名
     */
    public void clearAuthorizationCache(String username) {
        if (getAuthorizationCache() != null) {
            getAuthorizationCache().remove(username);
            log.debug("清除用户授权缓存: {}", username);
        }
    }

    /**
     * 清除所有授权缓存
     */
    public void clearAllAuthorizationCache() {
        if (getAuthorizationCache() != null) {
            getAuthorizationCache().clear();
            log.debug("清除所有授权缓存");
        }
    }
}