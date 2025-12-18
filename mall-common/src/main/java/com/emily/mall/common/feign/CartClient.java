package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cart-service", configuration = DefaultFeignConfig.class)
public interface CartClient {

    @DeleteMapping("/cart/clear")
    Result<Boolean> clearCartItems(@RequestParam("productIds") List<Long> productIds);
}
