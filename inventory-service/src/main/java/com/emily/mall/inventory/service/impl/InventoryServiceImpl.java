package com.emily.mall.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.dto.InventoryLockDto;
import com.emily.mall.common.dto.InventoryUpdateDto;
import com.emily.mall.common.dto.ProductForCartVo;
import com.emily.mall.common.feign.ProductClient;
import com.emily.mall.inventory.dto.ChangeInventoryVo;
import com.emily.mall.inventory.entity.Inventory;
import com.emily.mall.inventory.mapper.InventoryMapper;
import com.emily.mall.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.emily.mall.common.dto.InventoryDeductDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final ProductClient productClient;
//    private final RabbitTemplate rabbitTemplate;

    //库存锁定（用户下单触发）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean lockStock(List<InventoryLockDto> items){
        for (InventoryLockDto item : items) {
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
            // 锁定库存
            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            inventory.setLockedStock(item.getQuantity());
            this.updateById(inventory);
        }
        return true;
    }


    //库存扣减（用户支付触发）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deductStock(List<InventoryDeductDTO> items) {
        for (InventoryDeductDTO item : items) {
            // 查询库存
            LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getProductId, item.getProductId());
            Inventory inventory = this.getOne(wrapper);

            if (inventory == null) {
                throw new RuntimeException("商品 " + item.getProductId() + " 库存不存在");
            }

            if (inventory.getLockedStock()< item.getQuantity()) {
                throw new RuntimeException("商品 " + item.getProductId() + " 没有锁定的库存");
            }

            // 扣减库存
            inventory.setLockedStock(inventory.getLockedStock()- item.getQuantity());
            if(inventoryMapper.updateById(inventory)<=0){
                log.error("扣减库存失败");
                return false;
            }
        }
        return true;
    }


    //设置库存（商家操作）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateInventory(InventoryUpdateDto dto){
        Inventory inventory=getInventoryByProductId(dto.getProductId());
        if(inventory==null){
            log.warn("尝试设置不存在的商品的库存");
            return false;
        }
        //设置库存
        inventory.setAvailableStock(dto.getNewStock());
        inventory.setTotalStock(dto.getNewStock() + (inventory.getLockedStock() != null ? inventory.getLockedStock() : 0));
        int res = inventoryMapper.updateById(inventory);
//        //通知商品服务修改库存
//        String exchangeName="mall.inventory.update";
//        //发送消息
//        log.info("库存服务发松修改库存消息给商品服务");
//        rabbitTemplate.convertAndSend(exchangeName,"",dto);

        return res >= 0;

    }

    //根据商品id获取商品库存信息
    @Override
    public Inventory getInventoryByProductId(Long productId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, productId);
        return this.getOne(wrapper);
    }

    //删除商品时也删除库存
    @Override
    public Boolean deleteInventoryByProductId(Long productId){
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, productId);
        return inventoryMapper.delete(wrapper) > 0;
    }

    @Override
    public ChangeInventoryVo createInventory(Inventory inventory){
        //要调用商品服务查看商品id是否存在
        ProductForCartVo vo=productClient.getProductForCart(inventory.getProductId());//使用这个接口获得商品的基础信息
        if(vo==null){
            log.error("要设置库存的商品不存在,id={}",inventory.getProductId());
            return null;
        }
        if(inventoryMapper.insert(inventory)<=0){
            log.error("数据库异常，插入库存失败");
            return null;
        }

        return new ChangeInventoryVo(inventory,vo);
    }



//    @Override
//    public Page<Inventory> getInventoryPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId) {
//        Page<Inventory> page = new Page<>(pageNum, pageSize);
//        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
//
//        if (productId != null) {
//            wrapper.eq(Inventory::getProductId, productId);
//        }
//        if (warehouseId != null) {
//            wrapper.eq(Inventory::getWarehouseId, warehouseId);
//        }
//
//        wrapper.orderByDesc(Inventory::getCreateTime);
//        return this.page(page, wrapper);
//    }
//

//
//    @Override
//    public Inventory getInventoryByProductCode(String productCode) {
//        // productCode 字段已被注释，暂时返回 null
//        // LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
//        // wrapper.eq(Inventory::getProductCode, productCode);
//        // return this.getOne(wrapper);
//        return null;
//    }
}
