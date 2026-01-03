package com.emily.mall.inventory.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.dto.InventoryLockDto;
import com.emily.mall.common.dto.InventoryUpdateDto;
import com.emily.mall.inventory.dto.ChangeInventoryVo;
import com.emily.mall.inventory.entity.Inventory;

import com.emily.mall.common.dto.InventoryDeductDTO;

import java.util.List;

public interface InventoryService extends IService<Inventory> {
    //修改库存（商家操作）
    Boolean updateInventory(InventoryUpdateDto dto);

    //锁定库存（用户下单触发）
    Boolean lockStock(List<InventoryLockDto> items);

    //库存扣减（用户支付触发）
    Boolean deductStock(List<InventoryDeductDTO> items);

    //查询商品的库存信息
    Inventory getInventoryByProductId(Long productId);

    //根据商品ID删除库存
    Boolean deleteInventoryByProductId(Long productId);

    ChangeInventoryVo createInventory(Inventory inventory);


//    Page<Inventory> getInventoryPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId);
//

//
//    Inventory getInventoryByProductCode(String productCode);
//
//
}
