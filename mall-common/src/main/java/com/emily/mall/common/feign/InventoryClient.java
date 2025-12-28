package com.emily.mall.common.feign;

import com.emily.mall.common.config.DefaultFeignConfig;
import com.emily.mall.common.dto.InventoryDeductDTO;
import com.emily.mall.common.dto.InventoryLockDto;
import com.emily.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "inventory-service", url="http://192.168.64.1:8085",configuration = DefaultFeignConfig.class)
public interface InventoryClient {

    /**
     * 批量扣减库存
     * @param items 库存扣减列表
     * @return 操作结果
     */
    @PutMapping("/inventory/deduct")
    Result<Boolean> deductStock(@RequestBody List<InventoryDeductDTO> items);

    //批量锁定库存
    @PutMapping("/inventory/lock")
    Result<Boolean> lockStock(@RequestBody List<InventoryLockDto> items);

    /**
     * 更新库存信息
     * @param productId 商品ID
     * @param newStock 新库存数量
     * @return 操作结果
     */
    @PutMapping("/inventory/update")
    Result<Boolean> updateInventory(@RequestParam("productId") Long productId,
                                     @RequestParam("newStock") Integer newStock);

//    设置库存（创建商品时）
    @PostMapping("/inventory/create")
    Result<Boolean> createInventory(@RequestParam("productId") Long productId,
                                    @RequestParam("stock") Integer stock);


    //删除库存（删除商品时）
    @DeleteMapping("/inventory")
    Result<Boolean> deleteInventoryByProductId(@RequestParam("productId") Long productId);


}
