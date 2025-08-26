package com.coder.service.impl;

import com.coder.entity.UserPrivacy;
import com.coder.mapper.UserPrivacyMapper;
import com.coder.service.UserPrivacyService;
import com.coder.utils.RequestUtils;
import com.coder.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 用户隐私信息服务实现类
 *
 * @author Sunset
 * @since 2025-08-24
 */
@Slf4j
@Service
public class UserPrivacyServiceImpl implements UserPrivacyService {

    @Resource
    private UserPrivacyMapper userPrivacyMapper;

    /**
     * 记录用户登录隐私信息
     *
     * @param userId 用户ID
     * @param request HTTP请求对象
     * @param loginResult 登录结果
     * @param loginDuration 登录持续时间
     */
    @Override
    public void recordLoginPrivacy(Long userId, HttpServletRequest request, Integer loginResult, Long loginDuration) {
        if (userId == null || request == null) {
            log.warn("记录用户隐私信息失败：用户ID或请求对象为空");
            return;
        }

        try {
            UserPrivacy userPrivacy = buildUserPrivacy(userId, request, loginResult, loginDuration);
            saveUserPrivacy(userPrivacy);
            log.info("用户隐私信息记录成功，用户ID：{}", userId);
        } catch (Exception e) {
            log.error("记录用户隐私信息失败，用户ID：{}", userId, e);
        }
    }

    /**
     * 新增用户隐私信息
     *
     * @param userPrivacy 用户隐私信息实体对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveUserPrivacy(UserPrivacy userPrivacy) {
        if (userPrivacy == null) {
            log.warn("新增用户隐私信息失败：实体对象为空");
            return false;
        }

        try {
            int result = userPrivacyMapper.insertUserPrivacy(userPrivacy);
            return result > 0;
        } catch (Exception e) {
            log.error("新增用户隐私信息失败", e);
            return false;
        }
    }

    /**
     * 构建用户隐私信息对象
     *
     * @param userId 用户ID
     * @param request HTTP请求对象
     * @param loginResult 登录结果
     * @param loginDuration 登录持续时间
     * @return 用户隐私信息对象
     */
    private UserPrivacy buildUserPrivacy(Long userId, HttpServletRequest request, Integer loginResult, Long loginDuration) {
        UserPrivacy userPrivacy = new UserPrivacy();

        // 设置用户ID和登录信息
        userPrivacy.setUserId(userId);
        userPrivacy.setLoginResult(loginResult);
        userPrivacy.setLoginDuration(loginDuration);
        userPrivacy.setLoginType("WEB");

        // 获取IP信息
        userPrivacy.setIpAddress(RequestUtils.getClientIp(request));
        userPrivacy.setRealIp(request.getHeader("X-Real-IP"));
        userPrivacy.setForwardedFor(request.getHeader("X-Forwarded-For"));
        userPrivacy.setProxyIp(RequestUtils.getProxyIp(request));

        // 获取User-Agent信息并解析
        String userAgent = request.getHeader("User-Agent");
        userPrivacy.setUserAgent(userAgent);
        parseUserAgentInfo(userAgent, userPrivacy);

        // 获取其他请求头信息
        setRequestHeaders(request, userPrivacy);

        // 获取请求信息
        userPrivacy.setRequestMethod(request.getMethod());
        userPrivacy.setRequestUri(request.getRequestURI());

        // 获取完整请求头信息（JSON格式）
        userPrivacy.setRequestHeaders(RequestUtils.getHeadersAsJson(request));

        // 设置基础信息
        LocalDateTime now = LocalDateTime.now();
        userPrivacy.setCreateTime(now);
        userPrivacy.setUpdateTime(now);
        userPrivacy.setCreateBy(userId);
        userPrivacy.setUpdateBy(userId);
        userPrivacy.setDeleted(0);

        return userPrivacy;
    }

    /**
     * 解析User-Agent信息
     *
     * @param userAgent User-Agent字符串
     * @param userPrivacy 用户隐私信息对象
     */
    private void parseUserAgentInfo(String userAgent, UserPrivacy userPrivacy) {
        if (userAgent != null && !userAgent.trim().isEmpty()) {
            UserAgentUtils.UserAgentInfo agentInfo = UserAgentUtils.parseUserAgent(userAgent);
            userPrivacy.setBrowserName(agentInfo.getBrowserName());
            userPrivacy.setBrowserVersion(agentInfo.getBrowserVersion());
            userPrivacy.setBrowserEngine(agentInfo.getBrowserEngine());
            userPrivacy.setOsName(agentInfo.getOsName());
            userPrivacy.setOsVersion(agentInfo.getOsVersion());
            userPrivacy.setOsArch(agentInfo.getOsArch());
            userPrivacy.setDeviceType(agentInfo.getDeviceType());
            userPrivacy.setDeviceBrand(agentInfo.getDeviceBrand());
            userPrivacy.setDeviceModel(agentInfo.getDeviceModel());
        }
    }

    /**
     * 设置请求头信息
     *
     * @param request HTTP请求对象
     * @param userPrivacy 用户隐私信息对象
     */
    private void setRequestHeaders(HttpServletRequest request, UserPrivacy userPrivacy) {
        userPrivacy.setAccept(request.getHeader("Accept"));
        userPrivacy.setAcceptLanguage(request.getHeader("Accept-Language"));
        userPrivacy.setAcceptEncoding(request.getHeader("Accept-Encoding"));
        userPrivacy.setReferer(request.getHeader("Referer"));
        userPrivacy.setHost(request.getHeader("Host"));
        userPrivacy.setOrigin(request.getHeader("Origin"));
    }
}