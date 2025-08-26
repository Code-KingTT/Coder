package com.coder.aspect;

import com.coder.annotation.RequiresPermission;
import com.coder.annotation.RequiresPermissions;
import com.coder.annotation.RequiresRoles;
import com.coder.context.UserContext;
import com.coder.exception.BusinessException;
import com.coder.result.ResultCode;
import com.coder.service.UserService;
import com.coder.vo.UserPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Resource
    private UserService userService;

    /**
     * 权限检查切面（单数形式）
     */
    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        log.debug("执行权限检查：{}", Arrays.toString(requiresPermission.value()));

        Long currentUserId = getCurrentUserId();
        UserPermissionVO permissionInfo = getUserPermissionInfo(currentUserId);

        String[] requiredPermissions = requiresPermission.value();
        if (requiredPermissions.length == 0) {
            return joinPoint.proceed();
        }

        boolean hasPermission = checkUserPermissions(
                permissionInfo.getPermissions(),
                requiredPermissions,
                requiresPermission.logical() == RequiresPermission.Logical.AND
        );

        if (!hasPermission) {
            throw new BusinessException(ResultCode.FORBIDDEN, "权限不足，需要权限：" + Arrays.toString(requiredPermissions));
        }

        return joinPoint.proceed();
    }

    /**
     * 权限检查切面（复数形式）
     */
    @Around("@annotation(requiresPermissions)")
    public Object checkPermissions(ProceedingJoinPoint joinPoint, RequiresPermissions requiresPermissions) throws Throwable {
        log.debug("执行权限检查：{}", Arrays.toString(requiresPermissions.value()));

        Long currentUserId = getCurrentUserId();
        UserPermissionVO permissionInfo = getUserPermissionInfo(currentUserId);

        String[] requiredPermissions = requiresPermissions.value();
        if (requiredPermissions.length == 0) {
            return joinPoint.proceed();
        }

        boolean hasPermission = checkUserPermissions(
                permissionInfo.getPermissions(),
                requiredPermissions,
                requiresPermissions.logical() == RequiresPermissions.Logical.AND
        );

        if (!hasPermission) {
            throw new BusinessException(ResultCode.FORBIDDEN, "权限不足，需要权限：" + Arrays.toString(requiredPermissions));
        }

        return joinPoint.proceed();
    }

    /**
     * 角色检查切面
     */
    @Around("@annotation(requiresRoles)")
    public Object checkRoles(ProceedingJoinPoint joinPoint, RequiresRoles requiresRoles) throws Throwable {
        log.debug("执行角色检查：{}", Arrays.toString(requiresRoles.value()));

        Long currentUserId = getCurrentUserId();
        UserPermissionVO permissionInfo = getUserPermissionInfo(currentUserId);

        String[] requiredRoles = requiresRoles.value();
        if (requiredRoles.length == 0) {
            return joinPoint.proceed();
        }

        boolean hasRole = checkUserRoles(
                permissionInfo.getRoleCodes(),
                requiredRoles,
                requiresRoles.logical() == RequiresRoles.Logical.AND
        );

        if (!hasRole) {
            throw new BusinessException(ResultCode.FORBIDDEN, "角色权限不足，需要角色：" + Arrays.toString(requiredRoles));
        }

        return joinPoint.proceed();
    }

    /**
     * 类级别注解检查
     */
    @Around("@within(com.coder.annotation.RequiresRoles) || @within(com.coder.annotation.RequiresPermissions)")
    public Object checkClassLevelAnnotations(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // 检查类级别的角色注解
        RequiresRoles classRoles = targetClass.getAnnotation(RequiresRoles.class);
        if (classRoles != null && method.getAnnotation(RequiresRoles.class) == null) {
            checkRoles(joinPoint, classRoles);
        }

        // 检查类级别的权限注解
        RequiresPermissions classPermissions = targetClass.getAnnotation(RequiresPermissions.class);
        if (classPermissions != null && method.getAnnotation(RequiresPermissions.class) == null
                && method.getAnnotation(RequiresPermission.class) == null) {
            checkPermissions(joinPoint, classPermissions);
        }

        return joinPoint.proceed();
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null || currentUserId.equals(0L)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户未登录");
        }
        return currentUserId;
    }

    /**
     * 获取用户权限信息
     */
    private UserPermissionVO getUserPermissionInfo(Long currentUserId) {
        return userService.getUserPermissionInfo(currentUserId);
    }

    /**
     * 检查用户权限
     */
    private boolean checkUserPermissions(List<String> userPermissions, String[] requiredPermissions, boolean isAnd) {
        if (isAnd) {
            return Arrays.stream(requiredPermissions).allMatch(userPermissions::contains);
        } else {
            return Arrays.stream(requiredPermissions).anyMatch(userPermissions::contains);
        }
    }

    /**
     * 检查用户角色
     */
    private boolean checkUserRoles(List<String> userRoles, String[] requiredRoles, boolean isAnd) {
        if (isAnd) {
            return Arrays.stream(requiredRoles).allMatch(userRoles::contains);
        } else {
            return Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
        }
    }
}