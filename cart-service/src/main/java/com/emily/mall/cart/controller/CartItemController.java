package com.emily.mall.cart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.cart.dto.CartItemDto;
import com.emily.mall.cart.dto.CartQuantityUpdateDto;
import com.emily.mall.cart.dto.CartTotalDTO;
import com.emily.mall.cart.entity.CartItem;
import com.emily.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.emily.mall.cart.service.CartItemService;

import java.util.List;

import static com.emily.mall.common.utils.utils.getCurrentUserIdSafely;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * 1.添加一个商品到购物车
     */
    @PostMapping("/add")
    public Result<CartItem> addCartItem(@RequestBody CartItemDto dto) {
        Long userId=getCurrentUserIdSafely();
        if(userId == null){
            return Result.fail("无法获取用户ID");
        }
        CartItem res=cartItemService.addCartItem(userId, dto.getProductId(), dto.getQuantity());
        if(res!=null){
            return Result.ok(res);
        }
        return Result.fail("添加到购物车失败");

    }

//    2.修改购物车商品数量
    @PutMapping("/quantity")
    public Result<CartItem> updateCartItemQuantityByCartId(@RequestBody CartQuantityUpdateDto dto){
        try {
            CartItem response=cartItemService.updateQuantityById(dto.getId(), dto.getNewQuantity());
            return Result.ok(response);
        } catch (Exception e) {
            return Result.fail("修改购物车数量失败 " + e.getMessage());
        }
    }

    //3.购物车移除商品列表（改为数量减一）
    @DeleteMapping("/clear")
    public Result<Boolean> clearCartItems(@RequestParam("productIds") List<Long> productIds) {

        Long userId = getCurrentUserIdSafely();
        if(userId == null){
            return Result.fail("无法获取用户ID");
        }
//        Boolean success=cartItemService.removeByProductIds(userId, productIds);
        Boolean success=cartItemService.minusByProductIds(userId,productIds);
        if (success) {
            return Result.ok(true);
        }
        return Result.fail("移除商品失败");
    }

    //4.计算购物车选中商品的总价格：输入：用户id
    @GetMapping("/count")
    public Result<CartTotalDTO> countCartPrice(@RequestParam("productIds") List<Long> productIds){
        Long userId = getCurrentUserIdSafely();
        if(userId == null){
            return Result.fail("无法获取用户ID");
        }
        CartTotalDTO res=cartItemService.calculateSelectedTotalByUserId(userId,productIds);
        if(res!=null){
            return Result.ok(res);
        }
        return Result.fail("计算商品价格失败");
    }

    //5.获取用户购物车列表（page）
    @GetMapping("/list")
    public Result<Page<CartItem>> getCartListByUserId(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        Long userId = getCurrentUserIdSafely();
        if(userId == null){
            return Result.fail("无法获取用户ID");
        }
        Page<CartItem> res=cartItemService.getCartItemsByUserId(userId,pageNum,pageSize);
        if(res!=null){
            return Result.ok(res);
        }
        return Result.fail("获取购物车列表失败");

    }

    
}
