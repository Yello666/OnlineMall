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
import java.util.ArrayList;
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
            // 新增商品,要查询product并赋值
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
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
            //如果更新后的数量为0，直接删除该商品
            if(quantity==0){
                this.removeById(cartItem);
            }
            cartItem.setQuantity(quantity);
            Boolean success=this.updateById(cartItem);
            if(success){
                return cartItem;
            }
        }
        log.warn("此购物车不存在");
        return null;
    }

    //根据购物车id删除购物车结构体
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

// 3. 计算选中商品的总价（选中商品由前端传入 productIds）
    @Override
    public CartTotalDTO calculateSelectedTotalByUserId(Long userId, List<Long> productIds) {
        // 参数校验
        if (productIds == null || productIds.isEmpty()) {
            CartTotalDTO dto = new CartTotalDTO();
            dto.setSelectedQuantity(0);
            dto.setSelectedAmount(BigDecimal.ZERO);
            return dto;
        }

        // 1. 查询该用户、且商品ID在 productIds 中的购物车项
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
                .in(CartItem::getProductId, productIds); // 关键：只查选中的商品

        List<CartItem> selectedItems = this.list(wrapper);

        int selectedQuantity = 0;
        BigDecimal selectedAmount = BigDecimal.ZERO;

        // 2. 累加数量和金额
        for (CartItem item : selectedItems) {
            // 防止空指针
            if (item.getQuantity() == null || item.getPrice() == null) {
                continue;
            }
            selectedQuantity += item.getQuantity();
            selectedAmount = selectedAmount.add(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        // 3. 返回结果
        CartTotalDTO dto = new CartTotalDTO();
        dto.setSelectedQuantity(selectedQuantity);
        dto.setSelectedAmount(selectedAmount);
        return dto;
    }

    //4.根据userId查询购物车列表
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


    //5.清空一个用户所有的商品
    @Override
    public void clearCart(Long userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId);
        this.remove(wrapper);
    }


    //6.删除productId对应的商品（直接删除商品，不是减少数量）
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

    //7.根据商品ID列表将购物车商品数量减一,如果数量为0则删除
    @Override
    public Boolean minusByProductIds(Long userId, List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return false;
        }

        // 查询当前用户、指定商品ID列表的购物车项
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId)
                .in(CartItem::getProductId, productIds);

        List<CartItem> cartItems = this.list(wrapper);
        if (cartItems == null || cartItems.isEmpty()) {
            return true; // 没有匹配项，视为成功
        }

        List<Long> toDeleteIds = new ArrayList<>();
        List<CartItem> toUpdateList = new ArrayList<>();

        //先改变结构体的值
        for (CartItem item : cartItems) {
            Integer quantity = item.getQuantity();
            if (quantity == null || quantity <= 0) {
                // 异常数据，直接删除
                toDeleteIds.add(item.getId());
            } else if (quantity == 1) {
                toDeleteIds.add(item.getId());
            } else {
                item.setQuantity(quantity - 1);
                toUpdateList.add(item);
            }
        }
        //再统一进行修改和删除操作
        boolean success = true;
        if (!toDeleteIds.isEmpty()) {
            success &= this.removeByIds(toDeleteIds);
        }
        if (!toUpdateList.isEmpty()) {
            success &= this.updateBatchById(toUpdateList);
        }
        return success;
    }

//    //改变是否选择商品（现状选择-->不选择,不选择-->选择
//    public boolean selectCartItem(Long id, Integer crtSelectedStatus) {
//        CartItem cartItem = this.getById(id);
//        if (cartItem == null) {
//            return false;
//        }
//        crtSelectedStatus=!!crtSelectedStatus;
//        cartItem.setSelected(crtSelectedStatus);
//        return this.updateById(cartItem);
//    }

//    public boolean selectAllCartItems(Long userId, Integer selected) {
//        LambdaUpdateWrapper<CartItem> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(CartItem::getUserId, userId)
//               .set(CartItem::getSelected, selected);
//        return this.update(wrapper);
//    }
}
