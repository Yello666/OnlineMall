package com.emily.mall.inventory.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.inventory.entity.Inventory;

import com.emily.mall.common.dto.InventoryDeductDTO;
import java.util.List;

public interface InventoryService extends IService<Inventory> {

    Page<Inventory> getInventoryPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId);

    Inventory getInventoryByProductId(Long productId);

    Inventory getInventoryByProductCode(String productCode);

    void deductStock(List<InventoryDeductDTO> items);
}
