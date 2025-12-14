package com.emily.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.emily.mall.order", "com.emily.mall.common"})
@EnableDiscoveryClient
@MapperScan("com.emily.mall.order.mapper")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
