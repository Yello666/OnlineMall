package com.emily.mall.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Boolean> createOrder(@RequestBody Order order) {
        boolean success = orderService.save(order);
        return success ? Result.ok(success) : Result.fail("创建订单失败");
    }

    /**
     * 批量创建订单
     */
    @PostMapping("/batch")
    public Result<Boolean> createOrderBatch(@RequestBody List<Order> orders) {
        boolean success = orderService.saveBatch(orders);
        return success ? Result.ok(success) : Result.fail("批量创建订单失败");
    }

    /**
     * 根据ID删除订单
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrder(@PathVariable Long id) {
        boolean success = orderService.removeById(id);
        return success ? Result.ok(success) : Result.fail("删除订单失败");
    }

    /**
     * 批量删除订单
     */
    @DeleteMapping("/batch")
    public Result<Boolean> deleteOrderBatch(@RequestBody List<Long> ids) {
        boolean success = orderService.removeByIds(ids);
        return success ? Result.ok(success) : Result.fail("批量删除订单失败");
    }

    /**
     * 更新订单
     */
    @PutMapping
    public Result<Boolean> updateOrder(@RequestBody Order order) {
        boolean success = orderService.updateById(order);
        return success ? Result.ok(success) : Result.fail("更新订单失败");
    }

    /**
     * 批量更新订单
     */
    @PutMapping("/batch")
    public Result<Boolean> updateOrderBatch(@RequestBody List<Order> orders) {
        boolean success = orderService.updateBatchById(orders);
        return success ? Result.ok(success) : Result.fail("批量更新订单失败");
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return order != null ? Result.ok(order) : Result.fail("订单不存在");
    }

    /**
     * 批量查询订单
     */
    @GetMapping("/batch")
    public Result<List<Order>> getOrderBatch(@RequestParam List<Long> ids) {
        List<Order> orders = orderService.listByIds(ids);
        return Result.ok(orders);
    }

    /**
     * 查询所有订单
     */
    @GetMapping("/list")
    public Result<List<Order>> getOrderList() {
        List<Order> orders = orderService.list();
        return Result.ok(orders);
    }

    /**
     * 分页查询订单
     */
    @GetMapping("/page")
    public Result<Page<Order>> getOrderPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Order> page = orderService.getOrderPage(pageNum, pageSize, userId, status);
        return Result.ok(page);
    }

    /**
     * 根据订单号查询订单
     */
    @GetMapping("/orderNo/{orderNo}")
    public Result<Order> getOrderByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        return order != null ? Result.ok(order) : Result.fail("订单不存在");
    }

    /**
     * 根据用户ID查询订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<Page<Order>> getOrdersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Order> page = orderService.getOrdersByUserId(userId, pageNum, pageSize);
        return Result.ok(page);
    }
}
