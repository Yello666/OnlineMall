package com.emily.mall.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.emily.mall.cart.dto.CartTotalDTO;
import com.emily.mall.cart.entity.CartItem;
import com.emily.mall.cart.mapper.CartItemMapper;
import com.emily.mall.cart.service.CartItemService;
import com.emily.mall.common.dto.ProductForCartVo;
import com.emily.mall.common.feign.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {
    private final ProductClient productClient;

    //1.添加商品到购物车
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItem addCartItem(Long userId, Long productId, Integer quantity) {
        // 查询是否已存在该商品
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .eq(CartItem::getProductId, productId);
        
        CartItem existItem = this.getOne(wrapper);
        
        if (existItem != null) {
            // 如果已存在,增加数量
            existItem.setQuantity(existItem.getQuantity() + quantity);
            Boolean success= this.updateById(existItem);
            if (success) {
                return existItem;
            }
            return null;

        } else {
            // 新增商品,默认选中,要查询product并赋值
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(1);
            //查询商品信息
            ProductForCartVo vo=null;
            try {
                vo = productClient.getProductForCart(productId);
            } catch (Exception e) {
                throw new RuntimeException("获取商品信息失败: " + e.getMessage());
            }
            if (vo == null) {
                throw new RuntimeException("商品不存在，ID: " + productId);
            }
            cartItem.setPrice(vo.getPrice());
            cartItem.setProductImage(vo.getImage());
            cartItem.setProductName(vo.getName());
            //保存
            Boolean success= this.save(cartItem);
            if (success) {
                return cartItem;
            }
            return null;

        }

    }


    //2.更新购物车的商品数量
    @Override
    public CartItem updateQuantityById(Long id, Integer quantity) {
        CartItem cartItem = this.getById(id);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            Boolean success=this.updateById(cartItem);
            if(success){
                return cartItem;
            }
        }
        log.warn("此购物车不存在");
        return null;
    }

    public boolean removeCartItems(List<Long> ids) {
        return this.removeByIds(ids);
    }

//    @Override
//    public BigDecimal calculateTotalByUserId(Long userId) {
//        List<CartItem> cartItems = getCartItemsByUserId(userId);
//
//        CartTotalDTO dto = new CartTotalDTO();
//        int totalQuantity = 0;
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        int selectedQuantity = 0;
//        BigDecimal selectedAmount = BigDecimal.ZERO;
//
//        for (CartItem item : cartItems) {
//            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
//            totalQuantity += item.getQuantity();
//            totalAmount = totalAmount.add(itemTotal);
//
//            if (item.getSelected() != null && item.getSelected() == 1) {
//                selectedQuantity += item.getQuantity();
//                selectedAmount = selectedAmount.add(itemTotal);
//            }
//        }
//
//        dto.setTotalQuantity(totalQuantity);
//        dto.setTotalAmount(totalAmount);
//        dto.setSelectedQuantity(selectedQuantity);
//        dto.setSelectedAmount(selectedAmount);
//
//        return dto;
//    }

    public CartTotalDTO calculateSelectedTotalByUserId(Long userId) {
        //1.找到所有选中的购物车item
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .eq(CartItem::getSelected, 1);
        
        List<CartItem> selectedItems = this.list(wrapper);
        
        CartTotalDTO dto = new CartTotalDTO();
        int selectedQuantity = 0;
        BigDecimal selectedAmount = BigDecimal.ZERO;

        //2.计算购物车选中商品的总件数和价格
        for (CartItem item : selectedItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            selectedQuantity += item.getQuantity();
            selectedAmount = selectedAmount.add(itemTotal);
        }
        //3.返回
        dto.setSelectedQuantity(selectedQuantity);
        dto.setSelectedAmount(selectedAmount);
        
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

//    public List<CartItem> getCartItemsByUserId(Long userId) {
//        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CartItem::getUserId, userId)
//               .orderByDesc(CartItem::getCreateTime);
//        return this.list(wrapper);
//    }

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

    //删除productId对应的商品（直接删除商品，不是减少数量）
    @Override
    public Boolean removeByProductIds(Long userId, List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return false;
        }
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
               .in(CartItem::getProductId, productIds);
        return this.remove(wrapper);
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
