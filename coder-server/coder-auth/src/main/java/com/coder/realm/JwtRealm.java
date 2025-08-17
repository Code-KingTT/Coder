package com.coder.realm;

import com.coder.client.UserServiceClient;
import com.coder.filter.JwtToken;
import com.coder.result.Result;
import com.coder.utils.JwtUtils;
import com.coder.vo.UserPermissionVO;
import com.coder.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * JWT Realm
 * 专门处理JWT Token的认证和授权
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Component
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserServiceClient userServiceClient;

    /**
     * 指定支持的Token类型 - 只支持JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权方法
     * 获取用户的角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("JwtRealm开始授权...");

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
     * 验证JWT Token
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        log.debug("JwtRealm开始认证...");

        JwtToken jwtToken = (JwtToken) auth;
        String token = jwtToken.getToken();

        if (token == null) {
            throw new AuthenticationException("JWT Token不能为空");
        }

        try {
            // 验证Token格式和签名
            String username = jwtUtils.getUsernameFromToken(token);

            if (username == null) {
                throw new AuthenticationException("无法从Token中获取用户名");
            }

            // 验证Token是否过期
            if (jwtUtils.isTokenExpired(token)) {
                throw new AuthenticationException("Token已过期");
            }

            // 查询用户信息验证用户是否存在且有效
            Result<UserVO> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || !userResult.isSuccess() || userResult.getData() == null) {
                throw new AuthenticationException("用户不存在");
            }

            UserVO user = userResult.getData();

            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                throw new AuthenticationException("账户被禁用");
            }

            // 验证Token
            if (!jwtUtils.validateToken(token, username)) {
                throw new AuthenticationException("Token验证失败");
            }

            log.debug("JWT认证成功: {}", username);

            // 返回认证信息
            return new SimpleAuthenticationInfo(
                    username,     // 用户名作为principal
                    token,        // token作为credentials
                    getName()     // realm名称
            );

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage(), e);
            throw new AuthenticationException("JWT认证失败", e);
        }
    }
}