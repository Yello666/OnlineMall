package com.emily.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.mapper.OrderMapper;
import com.emily.mall.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

/**
 * 订单服务实现类
 */
@Service
@GlobalTransactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    //用户下单
    @Override
    public boolean createOrder(Order order){
        order.setStatus(0);//将状态设置为待支付
        //1.插入订单到数据库

        //2.查询订单所包含的商品及购买个数

        //3.调用库存服务，扣减响应商品库存

        //4.调用购物车服务，清除掉购物车中的相关商品

        //返回成功或者失败回滚（已经配置好了seata，并且所有涉及到的服务已经注册到seata，使用XA模式）
        return true;
    }

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
