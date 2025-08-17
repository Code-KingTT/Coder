package com.coder.filter;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWT Token
 * 
 * @author Sunset
 * @date 2025-8-17
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }
}