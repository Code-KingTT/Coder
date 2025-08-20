package com.coder.config;

import com.coder.filter.JwtAuthenticationFilter;
import com.coder.realm.CustomRealm;
import com.coder.realm.JwtRealm;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * Shiro配置类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Configuration
public class ShiroConfig {

    /**
     * 创建ShiroFilterFactoryBean
     * 配置Shiro的Web过滤器
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean (SecurityManager securityManager, JwtAuthenticationFilter jwtAuthenticationFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自定义过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", jwtAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        // 配置访问控制，从上向下顺序判断
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 允许匿名访问
        // 登录相关接口
        filterChainDefinitionMap.put("/coder/auth/login", "anon");
        filterChainDefinitionMap.put("/coder/auth/register", "anon");
        filterChainDefinitionMap.put("/coder/auth/forgot-password", "anon");
        filterChainDefinitionMap.put("/coder/auth/reset-password", "anon");
        filterChainDefinitionMap.put("/coder/auth/send-email-code", "anon");

        // 静态资源
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        
        // Swagger相关
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/v3/api-docs/**", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        
        // 健康检查和监控
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/health", "anon");
        
        // 需要JWT认证
        filterChainDefinitionMap.put("/**", "jwt");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     * Shiro的安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager(CustomRealm customRealm, JwtRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 设置多Realm，需注意顺序
        List<Realm> realms = Arrays.asList(customRealm, jwtRealm);
        securityManager.setRealms(realms);

        // 设置认证策略
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        authenticator.setRealms(realms);
        securityManager.setAuthenticator(authenticator);

        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

}