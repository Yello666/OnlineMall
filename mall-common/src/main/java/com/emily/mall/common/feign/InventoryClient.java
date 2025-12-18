package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.dto.InventoryDeductDTO;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "inventory-service", configuration = DefaultFeignConfig.class)
public interface InventoryClient {

    @PutMapping("/inventory/deduct")
    Result<Boolean> deductStock(@RequestBody List<InventoryDeductDTO> items);
}
