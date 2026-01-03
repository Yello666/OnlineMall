package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.dto.PaymentCreateDto;
import com.emily.mall.common.result.Result;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value="payment-service", configuration= DefaultFeignConfig.class)
public interface PaymentClient {
    //创建支付记录（用户下单引发）包括了修改订单状态为待支付
    @PostMapping("/payment/create")
    Result<Boolean> createPayment(@RequestBody PaymentCreateDto dto);

    //修改订单状态
    @PutMapping("/payment/status")
    Result<Boolean> updatePayment(@RequestParam("paymentId") Long paymentId,
                                  @RequestParam("newStatus") Integer newStatus);

}
