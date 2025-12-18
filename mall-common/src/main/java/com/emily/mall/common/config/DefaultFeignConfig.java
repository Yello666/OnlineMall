package com.emily.mall.common.config;


import com.emily.mall.common.UserContext.UserContextHolder;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel(){
        //设置请求的日志类型
        return Logger.Level.BASIC;
    }

    //每一次调用openfeign发请求，都会在请求头处添加userinfo的Header
    @Bean
    public RequestInterceptor userContextInterceptor(){
        return template -> {
            // 获取当前线程的用户信息
            String userId = UserContextHolder.getUserId();
            String username = UserContextHolder.getUsername();
            String role = UserContextHolder.getRole();

            // 如果用户信息存在，则添加到请求头中
            if(userId != null){
                template.header("X-User-Id", userId);
            }
            if(username != null){
                template.header("X-User-Name", username);
            }
            if(role != null){
                template.header("X-User-Role", role);
            }
        };
    }
}
