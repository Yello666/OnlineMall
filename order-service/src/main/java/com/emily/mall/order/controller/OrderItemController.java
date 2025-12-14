package com.emily.mall.order.controller;

import com.emily.mall.common.result.Result;
import com.emily.mall.order.entity.OrderItem;
import com.emily.mall.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 订单明细控制器
 */
@RestController
@RequestMapping("/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 根据订单ID查询订单明细
     */
    @GetMapping("/order/{orderId}")
    public Result<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return Result.ok(orderItems);
    }
}
