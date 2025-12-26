package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cart-service", configuration = DefaultFeignConfig.class)
public interface CartClient {

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PostMapping("/cart/add")
    Result<Boolean> addCartItem(@RequestParam("userId") Long userId,
                                @RequestParam("productId") Long productId,
                                @RequestParam("quantity") Integer quantity);

    @DeleteMapping("/cart/clear")
    Result<Boolean> clearCartItems(@RequestParam("productIds") List<Long> productIds);
}
