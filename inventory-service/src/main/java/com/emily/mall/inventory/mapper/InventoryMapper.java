package com.emily.mall.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emily.mall.inventory.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存Mapper
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}
