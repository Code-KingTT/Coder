package com.coder.context;

import lombok.Data;

public class UserContext {

    private static final ThreadLocal<UserInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentUser(String userId, String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Long.valueOf(userId));
        userInfo.setUsername(username);
        USER_THREAD_LOCAL.set(userInfo);
    }

    public static UserInfo getCurrentUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static Long getCurrentUserId() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    public static String getCurrentUsername() {
        UserInfo userInfo = USER_THREAD_LOCAL.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }

    @Data
    public static class UserInfo {
        private Long userId;
        private String username;
    }
}