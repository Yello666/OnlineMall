package com.emily.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.UserContext.UserContextHolder;
import com.emily.mall.common.dto.InventoryDeductDTO;
import com.emily.mall.common.feign.CartClient;
import com.emily.mall.common.feign.InventoryClient;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.entity.OrderItem;
import com.emily.mall.order.mapper.OrderMapper;
import com.emily.mall.order.service.OrderItemService;
import com.emily.mall.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@RequiredArgsConstructor
@GlobalTransactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    private final OrderItemService orderItemService;


    private final InventoryClient inventoryClient;


    private final CartClient cartClient;

    //用户下单
    @Override
    public boolean createOrder(Order order){
        // 获取当前用户ID
        String userIdStr = UserContextHolder.getUserId();
        if (userIdStr == null) {
            throw new RuntimeException("未登录");
        }
        Long userId = Long.valueOf(userIdStr);
        order.setUserId(userId);

        order.setStatus(0);//将状态设置为待支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 生成订单号
        if (order.getOrderNo() == null) {
            order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        }

        //1.插入订单到数据库
        this.save(order);

        //2.查询订单所包含的商品及购买个数
        // 这里假设前端传递的order对象中包含了orderItems
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            List<InventoryDeductDTO> deductDTOList = new ArrayList<>();
            List<Long> productIds = new ArrayList<>();

            for (OrderItem item : orderItems) {
                item.setOrderId(order.getId());
                item.setCreateTime(LocalDateTime.now());
                item.setUpdateTime(LocalDateTime.now());
                
                // 准备扣减库存的数据
                InventoryDeductDTO deductDTO = new InventoryDeductDTO();
                deductDTO.setProductId(item.getProductId());
                deductDTO.setQuantity(item.getQuantity());
                deductDTOList.add(deductDTO);
                
                // 准备清理购物车的数据
                productIds.add(item.getProductId());
            }
            // 保存订单明细
            orderItemService.saveBatch(orderItems);

            //3.调用库存服务，扣减响应商品库存
            inventoryClient.deductStock(deductDTOList);

            //4.调用购物车服务，清除掉购物车中的相关商品
            cartClient.clearCartItems(productIds);
        }

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
