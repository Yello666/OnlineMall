package com.emily.mall.common.UserContext;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 第一步：在请求入口（比如拦截器/Controller）一次性从Header取值，存入ThreadLocal
//但只能在同一个微服务中使用ThreadLocal，不同线程之间的ThreadLocal的值的修改是互不影响的
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 从Header取网关传过来的userid、username
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Name");
        String role=request.getHeader("X-User-Role");
        // 存入ThreadLocal
        UserContextHolder.setUserInfo(userId, username,role);
        return true;
    }

    // 请求结束后清理ThreadLocal，避免内存泄漏
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        UserContextHolder.clear();
    }
}
