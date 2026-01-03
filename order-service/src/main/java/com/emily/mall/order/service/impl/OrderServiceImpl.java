package com.emily.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.Enum.OrderStatusEnum;
import com.emily.mall.common.dto.InventoryLockDto;
import com.emily.mall.common.dto.PaymentCreateDto;
import com.emily.mall.common.exeption.BusinessException;
import com.emily.mall.common.feign.CartClient;
import com.emily.mall.common.feign.InventoryClient;
import com.emily.mall.common.feign.PaymentClient;
import com.emily.mall.common.result.Result;
import com.emily.mall.order.entity.Order;
import com.emily.mall.order.entity.OrderItem;
import com.emily.mall.order.mapper.OrderMapper;
import com.emily.mall.order.service.OrderItemService;
import com.emily.mall.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.emily.mall.common.utils.utils.generateSerialNo;
import static com.emily.mall.common.utils.utils.getCurrentUserIdSafely;

/**
 * 订单服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    private final OrderItemService orderItemService;
    private final InventoryClient inventoryClient;
    private final OrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;

    private final String EXCHANGE_NAME="mall.order.fanout";
    private final PaymentClient paymentClient;

    //用户下单
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 1. 前置校验：避免空指针
        if (order == null) {
            log.error("下单失败：订单对象不能为空");
            throw new BusinessException("下单失败：订单对象不能为空");
        }

        try {
            // 2. 获取当前用户ID并设置
            Long userId = getCurrentUserIdSafely();
            if (userId == null) {
                log.error("下单失败：未获取到当前用户ID");
                throw new BusinessException("下单失败：请先登录");
            }
            order.setUserId(userId);

            // 3. 设置订单基础信息（替换状态硬编码为枚举）
            order.setStatus(OrderStatusEnum.WAIT_PAY.getCode()); // 0-待支付
            String orderNo = "E" + generateSerialNo();
            order.setOrderNo(orderNo);

            // 4. 插入订单到数据库（修正判断条件：!save() 表示插入失败）
            boolean saveOrderSuccess = save(order);
            if (!saveOrderSuccess) {
                log.error("下单失败：数据库异常，插入订单失败，订单号：{}，用户ID：{}", orderNo, userId);
                throw new BusinessException("下单失败：系统异常，无法创建订单");
            }
            // 订单插入成功后，获取生成的订单ID（确保非空）
            Long orderId = order.getId();
            if (orderId == null) {
                log.error("下单失败：订单插入成功，但未生成订单ID，订单号：{}，用户ID：{}", orderNo, userId);
                throw new BusinessException("下单失败：系统异常，订单ID生成失败");
            }

            // 5. 处理订单明细
            List<OrderItem> orderItems = order.getOrderItems();
            // 校验订单明细非空
            if (orderItems == null || orderItems.isEmpty()) {
                log.error("下单失败：订单无商品明细，订单号：{}，用户ID：{}", orderNo, userId);
                throw new BusinessException("下单失败：订单中无商品，请选择商品后再下单");
            }
            // 先为所有订单明细设置订单ID
            for (OrderItem item : orderItems) {
                if (item == null) {
                    continue; // 跳过空明细，避免空指针
                }
                item.setOrderId(orderId); // 关联订单ID
            }
            // 再批量保存订单明细（此时明细已关联订单ID）
            boolean saveOrderItemSuccess = orderItemService.saveBatch(orderItems);
            if (!saveOrderItemSuccess) {
                log.error("下单失败：保存订单明细失败，订单ID：{}，订单号：{}", orderId, orderNo);
                throw new BusinessException("下单失败：系统异常，无法保存订单明细");
            }

            // 6. 准备锁定库存的数据（优化后，商品ID已收集，直接构建库存锁定DTO）
            List<InventoryLockDto> lockDtoList = new ArrayList<>();
            List<Long> productIds=new ArrayList<>();
            for (OrderItem item : orderItems) {
                if (item == null || item.getProductId() == null || item.getQuantity() == null) {
                    log.warn("下单提示：无效订单明细，已跳过，订单ID：{}", orderId);
                    continue;
                }
                InventoryLockDto lockDto = new InventoryLockDto();
                lockDto.setProductId(item.getProductId());
                lockDto.setQuantity(item.getQuantity());
                lockDtoList.add(lockDto);
                productIds.add(item.getProductId());
            }
            // 校验库存锁定DTO非空
            if (lockDtoList.isEmpty()) {
                log.error("下单失败：无有效商品库存锁定数据，订单ID：{}，订单号：{}", orderId, orderNo);
                throw new BusinessException("下单失败：无有效商品，请选择商品后再下单");
            }

            // 7. 调用库存服务，锁定商品库存（强一致性场景）
            Result<Boolean> stockLockResult = inventoryClient.lockStock(lockDtoList);
            if (stockLockResult == null || !stockLockResult.getSuccess()) {
                String errMsg = stockLockResult != null ? stockLockResult.getMessage() : "库存锁定接口返回空";
                log.error("下单失败：调用库存服务锁定库存失败，订单号：{}，原因：{}", orderNo, errMsg);
                throw new BusinessException("下单失败：库存锁定失败：" + errMsg);
            }

            // 8. 调用支付服务，创建支付实体（待支付状态）
            PaymentCreateDto dto = new PaymentCreateDto();
            dto.setAmount(order.getPayAmount());
            dto.setOrderNo(orderNo);
            dto.setOrderId(orderId);
            Result<Boolean> paymentCreateResult = paymentClient.createPayment(dto);
            if (paymentCreateResult == null || !paymentCreateResult.getSuccess()) {
                String errMsg = paymentCreateResult != null ? paymentCreateResult.getMessage() : "支付记录创建接口返回空";
                log.error("下单失败：调用支付服务创建支付记录失败，订单号：{}，原因：{}", orderNo, errMsg);
                throw new BusinessException("下单失败：创建支付记录失败：" + errMsg);
            }

            // 9. 异步清理购物车（弱一致性场景，使用消息队列）
            if (!productIds.isEmpty()) {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", productIds);
                log.info("下单成功：已发送购物车清理消息，订单号：{}，商品ID列表：{}", orderNo, productIds);
            }

            // 10. TODO 使用延迟队列，添加订单超时取消任务（如10分钟未支付则取消订单）
            // rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_ROUTING_KEY, orderId, message -> {
            //     message.getMessageProperties().setDelay(10 * 60 * 1000); // 10分钟延迟
            //     return message;
            // });

            log.info("用户下单成功，用户ID：{}，订单ID：{}，订单号：{}", userId, orderId, orderNo);
            // 查询最新订单信息返回（确保返回数据是数据库最新状态，包含订单明细）
            return getById(orderId);

        } catch (Exception e) {
            // 补充订单号/用户ID的容错处理，避免空指针
            String orderNo =order.getOrderNo();
            Long userId = order.getUserId();
            log.error("用户下单异常，用户ID：{}，订单号：{}，异常信息：{}", userId, orderNo, e.getMessage(), e);
            // 重新抛出异常，触发Seata分布式事务回滚
            throw new BusinessException("下单失败：" + e.getMessage());
        }
    }


    //改变用户的订单状态
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrderStatus(Long OrderId,Integer newStatus){
        Order order=orderMapper.selectById(OrderId);
        if(order==null){
            log.error("订单不存在，无法改变其状态");
            return false;
        }
        order.setStatus(newStatus);
        if(orderMapper.updateById(order)<=0){
            log.error("订单状态修改失败，数据库出错");
        }
        log.info("成功修改订单{}状态为{}",OrderId,newStatus);
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
    public Order getOrderByOrderId(Long id) {
        Order order=orderMapper.selectById(id);
        return order;
    }
    @Override
    public Order getOrderByOrderNo(String orderNo){
        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo,orderNo);
        return orderMapper.selectOne(wrapper);
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
