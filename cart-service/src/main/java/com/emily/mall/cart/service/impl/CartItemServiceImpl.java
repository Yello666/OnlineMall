package com.emily.mall.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.cart.dto.CartTotalDTO;
import com.emily.mall.cart.entity.CartItem;
import com.emily.mall.cart.mapper.CartItemMapper;
import com.emily.mall.cart.service.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCartItem(Long userId, Long productId, Integer quantity) {
        // 查询是否已存在该商品
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .eq(CartItem::getProductId, productId);
        
        CartItem existItem = this.getOne(wrapper);
        
        if (existItem != null) {
            // 如果已存在,增加数量
            existItem.setQuantity(existItem.getQuantity() + quantity);
            this.updateById(existItem);
        } else {
            // 新增商品,默认选中
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(1);
            this.save(cartItem);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addCartItem(CartItem cartItem) {
        // 查询是否已存在该商品
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, cartItem.getUserId())
               .eq(CartItem::getProductId, cartItem.getProductId());
        
        CartItem existItem = this.getOne(wrapper);
        
        if (existItem != null) {
            // 如果已存在,增加数量
            existItem.setQuantity(existItem.getQuantity() + cartItem.getQuantity());
            return this.updateById(existItem);
        } else {
            // 新增商品,默认选中
            if (cartItem.getSelected() == null) {
                cartItem.setSelected(1);
            }
            return this.save(cartItem);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addCartItemBatch(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            if (!addCartItem(cartItem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) {
        CartItem cartItem = this.getById(id);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            this.updateById(cartItem);
        }
    }

    public boolean removeCartItems(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public CartTotalDTO calculateTotal(Long userId) {
        List<CartItem> cartItems = getCartItemsByUserId(userId);
        
        CartTotalDTO dto = new CartTotalDTO();
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        int selectedQuantity = 0;
        BigDecimal selectedAmount = BigDecimal.ZERO;
        
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalQuantity += item.getQuantity();
            totalAmount = totalAmount.add(itemTotal);
            
            if (item.getSelected() != null && item.getSelected() == 1) {
                selectedQuantity += item.getQuantity();
                selectedAmount = selectedAmount.add(itemTotal);
            }
        }
        
        dto.setTotalQuantity(totalQuantity);
        dto.setTotalAmount(totalAmount);
        dto.setSelectedQuantity(selectedQuantity);
        dto.setSelectedAmount(selectedAmount);
        
        return dto;
    }

    public CartTotalDTO calculateSelectedTotal(Long userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .eq(CartItem::getSelected, 1);
        
        List<CartItem> selectedItems = this.list(wrapper);
        
        CartTotalDTO dto = new CartTotalDTO();
        int selectedQuantity = 0;
        BigDecimal selectedAmount = BigDecimal.ZERO;
        
        for (CartItem item : selectedItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            selectedQuantity += item.getQuantity();
            selectedAmount = selectedAmount.add(itemTotal);
        }
        
        dto.setSelectedQuantity(selectedQuantity);
        dto.setSelectedAmount(selectedAmount);
        dto.setTotalQuantity(selectedQuantity);
        dto.setTotalAmount(selectedAmount);
        
        return dto;
    }

    @Override
    public Page<CartItem> getCartItemsByUserId(Long userId, Integer pageNum, Integer pageSize) {
        Page<CartItem> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .orderByDesc(CartItem::getCreateTime);
        return this.page(page, wrapper);
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .orderByDesc(CartItem::getCreateTime);
        return this.list(wrapper);
    }

    public Page<CartItem> getCartItemPage(Integer pageNum, Integer pageSize, Long userId) {
        Page<CartItem> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(CartItem::getUserId, userId);
        }
        
        wrapper.orderByDesc(CartItem::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void clearCart(Long userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId);
        this.remove(wrapper);
    }

    @Override
    public void removeByProductIds(Long userId, List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .in(CartItem::getProductId, productIds);
        this.remove(wrapper);
    }

    public boolean selectCartItem(Long id, Integer selected) {
        CartItem cartItem = this.getById(id);
        if (cartItem == null) {
            return false;
        }
        cartItem.setSelected(selected);
        return this.updateById(cartItem);
    }

    public boolean selectAllCartItems(Long userId, Integer selected) {
        LambdaUpdateWrapper<CartItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .set(CartItem::getSelected, selected);
        return this.update(wrapper);
    }
}
