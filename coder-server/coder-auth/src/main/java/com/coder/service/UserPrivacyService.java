package com.coder.service;

import com.coder.entity.UserPrivacy;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户隐私信息服务接口
 *
 * @author Sunset
 * @since 2025-08-24
 */
public interface UserPrivacyService {

    /**
     * 记录用户登录隐私信息
     * <p>
     * 在用户登录时调用，记录用户的IP地址、浏览器信息、操作系统信息等隐私数据
     * </p>
     *
     * @param userId 用户ID，不能为空
     * @param request HTTP请求对象，不能为空
     * @param loginResult 登录结果：0-失败，1-成功
     * @param loginDuration 登录持续时间（毫秒），可以为空
     */
    void recordLoginPrivacy(Long userId, HttpServletRequest request, Integer loginResult, Long loginDuration);

    /**
     * 新增用户隐私信息
     *
     * @param userPrivacy 用户隐私信息实体对象，不能为空
     * @return 是否新增成功
     */
    boolean saveUserPrivacy(UserPrivacy userPrivacy);
}