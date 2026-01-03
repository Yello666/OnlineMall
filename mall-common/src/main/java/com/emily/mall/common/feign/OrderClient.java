package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="order-service",configuration = DefaultFeignConfig.class)
public interface OrderClient {

    @PutMapping("/order/status")
    Result<Boolean> updateOrderStatus(@RequestParam("orderId") Long orderId,
                                      @RequestParam("newStatus") Integer newStatus);
}
