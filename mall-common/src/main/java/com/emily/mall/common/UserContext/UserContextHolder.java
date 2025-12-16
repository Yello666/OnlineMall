package com.emily.mall.common.UserContext;

import java.util.HashMap;
import java.util.Map;

// 这个类封装了ThreadLocal的工具类
public class UserContextHolder {
    // 核心：ThreadLocal存储当前线程的用户信息（每个线程有独立副本）,用于每个微服务的请求处理，每个请求都是单独的线程，
    private static final ThreadLocal<Map<String, String>> USER_CONTEXT = new ThreadLocal<>();

    // 初始化：把Header里的userid等信息存进去
    public static void setUserInfo(String userId, String username,String role) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);//此处存进去的userId是字符串，后续获取也是获取字符串
        map.put("username", username);
        map.put("role",role);
        USER_CONTEXT.set(map);
    }

    // 获取userid：全链路任意地方直接调
    public static String getUserId() {
        Map<String, String> map = USER_CONTEXT.get();
        return map == null ? null : map.get("userId");
    }

    // 获取username：同理
    public static String getUsername() {
        Map<String, String> map = USER_CONTEXT.get();
        return map == null ? null : map.get("username");
    }
    // 获取role：同理
    public static String getRole() {
        Map<String, String> map = USER_CONTEXT.get();
        return map == null ? null : map.get("role");
    }

    // 用完清理：避免内存泄漏
    public static void clear() {
        USER_CONTEXT.remove();
    }
}