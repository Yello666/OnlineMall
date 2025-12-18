package com.emily.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.entity.OrderItem;
import com.emily.mall.order.mapper.OrderItemMapper;
import com.emily.mall.order.service.OrderItemService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 订单明细服务实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {



    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        return this.list(wrapper);
    }

}
