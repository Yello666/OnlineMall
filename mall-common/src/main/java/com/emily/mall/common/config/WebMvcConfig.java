package com.emily.mall.common.config;

import com.emily.mall.common.UserContext.UserContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 标记为配置类，SpringBoot启动时会扫描
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

   // 注入第一步的拦截器Bean
    private final UserContextInterceptor userContextInterceptor;

    // 注册拦截器，并配置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor); // 添加拦截器,默认拦截所有请求

    }
}
