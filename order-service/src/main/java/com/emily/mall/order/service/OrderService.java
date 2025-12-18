package com.emily.mall.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.order.entity.Order;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    //用户下单
    boolean createOrder(Order order);


    /**
     * 分页查询订单
     */
    Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status);

    /**
     * 根据订单号查询订单
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     */
    Page<Order> getOrdersByUserId(Long userId, Integer pageNum, Integer pageSize);
}
