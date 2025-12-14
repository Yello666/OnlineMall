package com.emily.mall.cart.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.cart.dto.CartTotalDTO;
import com.emily.mall.cart.entity.CartItem;

/**
 * 购物车服务接口
 */
public interface CartItemService extends IService<CartItem> {

    /**
     * 添加商品到购物车
     */
    void addCartItem(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车商品数量
     */
    void updateQuantity(Long id, Integer quantity);

    /**
     * 获取用户购物车列表
     */
    Page<CartItem> getCartItemsByUserId(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 计算购物车总价
     */
    CartTotalDTO calculateTotal(Long userId);

    /**
     * 清空购物车
     */
    void clearCart(Long userId);
}
