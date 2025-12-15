package com.emily.mall.user.feign;

import com.emily.mall.common.result.Result;
import com.emily.mall.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户服务Feign客户端
 * 供其他微服务调用
 */
@FeignClient(name = "user-service", path = "/user")
public interface UserFeignClient {

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    Result<User> getUser(@PathVariable("id") Long id);

    /**
     * 批量查询用户
     */
    @GetMapping("/batch")
    Result<List<User>> getUserBatch(@RequestParam("ids") List<Long> ids);

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    Result<User> getUserByUsername(@PathVariable("username") String username);
}
