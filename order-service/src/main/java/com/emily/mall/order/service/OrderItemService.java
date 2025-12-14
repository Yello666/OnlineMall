package com.emily.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.order.entity.OrderItem;
import java.util.List;

/**
 * 订单明细服务接口
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单明细列表
     */
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}
