package com.emily.mall.common.feign;


import com.emily.mall.common.config.DefaultFeignConfig;

import com.emily.mall.common.dto.ProductForCartVo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "product-service",url = "http://192.168.64.1:8084", configuration = DefaultFeignConfig.class)
public interface ProductClient {

    @GetMapping("/product/forCart")
    ProductForCartVo getProductForCart(@RequestParam(value = "id") Long productId);

}
