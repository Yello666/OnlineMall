package com.emily.mall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emily.mall.cart.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车Mapper
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}
