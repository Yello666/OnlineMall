package com.emily.mall.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.inventory.entity.Inventory;
import com.emily.mall.inventory.mapper.InventoryMapper;
import com.emily.mall.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Override
    public Page<Inventory> getInventoryPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId) {
        Page<Inventory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        
        if (productId != null) {
            wrapper.eq(Inventory::getProductId, productId);
        }
        if (warehouseId != null) {
            wrapper.eq(Inventory::getWarehouseId, warehouseId);
        }
        
        wrapper.orderByDesc(Inventory::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Inventory getInventoryByProductId(Long productId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, productId);
        return this.getOne(wrapper);
    }

    @Override
    public Inventory getInventoryByProductCode(String productCode) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductCode, productCode);
        return this.getOne(wrapper);
    }
}
