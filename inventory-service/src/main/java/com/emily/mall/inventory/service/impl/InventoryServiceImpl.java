package com.emily.mall.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.inventory.entity.Inventory;
import com.emily.mall.inventory.mapper.InventoryMapper;
import com.emily.mall.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

import com.emily.mall.common.dto.InventoryDeductDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(List<InventoryDeductDTO> items) {
        for (InventoryDeductDTO item : items) {
            // 查询库存
            LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getProductId, item.getProductId());
            Inventory inventory = this.getOne(wrapper);

            if (inventory == null) {
                throw new RuntimeException("商品 " + item.getProductId() + " 库存不存在");
            }

            if (inventory.getAvailableStock() < item.getQuantity()) {
                throw new RuntimeException("商品 " + item.getProductId() + " 库存不足");
            }

            // 扣减库存
            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            this.updateById(inventory);
        }
    }

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
