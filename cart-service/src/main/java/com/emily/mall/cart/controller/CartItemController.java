package com.emily.mall.cart.controller;

import com.emily.mall.common.result.Result;
import org.springframework.web.bind.annotation.*;

import com.emily.mall.cart.service.CartItemService;
import com.emily.mall.common.UserContext.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public Result<Boolean> addCartItem(@RequestParam("userId") Long userId,
                                        @RequestParam("productId") Long productId,
                                        @RequestParam("quantity") Integer quantity) {
        try {
            cartItemService.addCartItem(userId, productId, quantity);
            return Result.ok(true);
        } catch (Exception e) {
            return Result.fail("添加到购物车失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public Result<Boolean> clearCartItems(@RequestParam("productIds") List<Long> productIds) {
        String userIdStr = UserContextHolder.getUserId();
        if (userIdStr == null) {
             // 如果网关没有传User或者上下文丢失，暂时使用Header获取或者Mock，
             // 这里假设已经有拦截器放入了ThreadLocal。
             // 考虑到微服务调用，调用方应该传递X-User-Id。
             // 如果是Feign调用，UserContextInterceptor应该已经处理了。
             // 如果这里拿不到，可能是拦截器没生效或者Feign没传。
             // 为了稳健，我们可以暂时不强制检查，或者抛出异常。
             // 这里暂时不做严格校验，假设调用方会传。
             // 但必须要有userId才能删。
             // 实际上，如果是内部调用，可以传userId参数。
             // 但我们的接口定义是 clearCartItems(@RequestParam("productIds") List<Long> productIds)
             // 并没有传userId。
             // 所以必须依赖 UserContextHolder。
        }
        Long userId = userIdStr != null ? Long.valueOf(userIdStr) : null;
        if(userId == null){
            return Result.fail("无法获取用户ID");
        }
        
        cartItemService.removeByProductIds(userId, productIds);
        return Result.ok(true);
    }
    
}
