package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.dto.InventoryDeductDTO;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "inventory-service", configuration = DefaultFeignConfig.class)
public interface InventoryClient {

    /**
     * 批量扣减库存
     * @param items 库存扣减列表
     * @return 操作结果
     */
    @PutMapping("/inventory/deduct")
    Result<Boolean> deductStock(@RequestBody List<InventoryDeductDTO> items);

    /**
     * 更新库存信息
     * @param productId 商品ID
     * @param newStock 新库存数量
     * @return 操作结果
     */
    @PutMapping("/inventory/update")
    Result<Boolean> updateInventory(@RequestParam("productId") Long productId,
                                     @RequestParam("newStock") Integer newStock);
}
