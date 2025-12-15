package com.emily.mall.common.config;


import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel(){
        //设置请求的日志类型
        return Logger.Level.BASIC;
    }
}
