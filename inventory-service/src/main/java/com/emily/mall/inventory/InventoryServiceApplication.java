package com.emily.mall.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.emily.mall.inventory", "com.emily.mall.common"})
@EnableDiscoveryClient
@MapperScan("com.emily.mall.inventory.mapper")
@EnableFeignClients(value="com.emily.mall.common.feign")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
