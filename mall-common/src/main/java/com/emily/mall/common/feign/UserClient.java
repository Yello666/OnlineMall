package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 用户服务Feign客户端
 * 供其他微服务调用
 */
@FeignClient(name = "user-service", path = "/user",configuration = DefaultFeignConfig.class)
public interface UserClient {

    //支付余额
    @PostMapping("/pay")
    Result<Boolean> payByUserBalance(@RequestParam("amount")BigDecimal amount,
                                     @RequestParam("userId")Long userId);

//    /**
//     * 根据ID查询用户
//     */
//    @GetMapping("/{id}")
//    Result<User> getUser(@PathVariable("id") Long id);
//
//    /**
//     * 批量查询用户
//     */
//    @GetMapping("/batch")
//    Result<List<User>> getUserBatch(@RequestParam("ids") List<Long> ids);
//
//    /**
//     * 根据用户名查询用户
//     */
//    @GetMapping("/username/{username}")
//    Result<User> getUserByUsername(@PathVariable("username") String username);



}

