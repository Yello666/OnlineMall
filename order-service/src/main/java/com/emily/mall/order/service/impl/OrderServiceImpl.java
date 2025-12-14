package com.emily.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.mapper.OrderMapper;
import com.emily.mall.order.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        return this.getOne(wrapper);
    }

    @Override
    public Page<Order> getOrdersByUserId(Long userId, Integer pageNum, Integer pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreateTime);
        return this.page(page, wrapper);
    }
}
