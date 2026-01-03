package com.emily.mall.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.Enum.OrderStatusEnum;
import com.emily.mall.common.Enum.PaymentStatusEnum;
import com.emily.mall.common.dto.InventoryDeductDTO;
import com.emily.mall.common.dto.OrderItemForPayment;
import com.emily.mall.common.dto.PaymentCreateDto;
import com.emily.mall.common.dto.PaymentPayDto;
import com.emily.mall.common.exeption.BusinessException;
import com.emily.mall.common.feign.InventoryClient;
import com.emily.mall.common.feign.OrderClient;
import com.emily.mall.common.feign.UserClient;
import com.emily.mall.common.result.Result;
import com.emily.mall.payment.entity.Payment;
import com.emily.mall.payment.mapper.PaymentMapper;
import com.emily.mall.payment.service.PaymentService;
import com.emily.mall.payment.service.UpdateStatusService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.emily.mall.common.utils.utils.generateSerialNo;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    private final UserClient userClient;
    private final InventoryClient inventoryClient;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final UpdateStatusService updateStatusService;
//    private final PaymentService paymentService;

    //支付订单
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Payment payOrder(PaymentPayDto dto) {
        // 1. 校验订单ID非空
        Long orderId=dto.getOrderId();
        if (orderId == null) {
            log.error("支付失败：订单ID不能为空");
            throw new BusinessException("支付失败：订单ID不能为空");
        }

        // 2. 查询支付记录
        Payment payment = this.getPaymentByOrderId(orderId);
        if (payment == null) {
            log.error("支付失败：订单ID{}不存在对应的支付记录", orderId);
            throw new BusinessException("支付失败：该订单未创建支付记录，请先下单");
        }
        // 记录关键参数，方便日志排查
        Long paymentId = payment.getId();
        Long userId = payment.getUserId();
        BigDecimal payAmount = payment.getAmount();

        try {
            // 3. 修改支付状态为【支付中】（直接调用更新方法，无需本地赋值）
            Result<Boolean> payStatusRes = updateStatusService.updateStatus(paymentId, PaymentStatusEnum.PAYING.getCode());
            if (payStatusRes == null || !payStatusRes.getSuccess()) {
                String errMsg = payStatusRes != null ? payStatusRes.getMessage() : "修改支付状态接口返回空";
                log.error("支付失败：订单ID{}，修改支付状态为支付中失败，原因：{}", orderId, errMsg);
                throw new BusinessException("支付失败：系统异常，请稍后重试");
            }

            // 4. 调用用户服务扣减余额（先校验返回值非空，避免空指针）
            Result<Boolean> balanceRes = userClient.payByUserBalance(payAmount, userId);
            if (balanceRes == null) {
                log.error("支付失败：订单ID{}，调用用户余额扣减接口返回空", orderId);
                throw new BusinessException("支付失败：用户服务异常");
            }
            if (!balanceRes.getSuccess()) {
                String errMsg = balanceRes.getMessage() != null ? balanceRes.getMessage() : "余额不足";
                log.error("支付失败：订单ID{}，用户余额扣减失败，原因：{}", orderId, errMsg);
                throw new BusinessException("支付失败：" + errMsg);
            }

            // 5. 调用库存服务扣减库存（组装参数+非空校验）
            List<InventoryDeductDTO> inventoryDeductDTOList = getInventoryDeductDTOS(dto);
            Result<Boolean> inventoryRes = inventoryClient.deductStock(inventoryDeductDTOList);
            if (inventoryRes == null) {
                log.error("支付失败：订单ID{}，调用库存扣减接口返回空", orderId);
                throw new BusinessException("支付失败：库存服务异常");
            }
            if (!inventoryRes.getSuccess()) {
                String errMsg = inventoryRes.getMessage() != null ? inventoryRes.getMessage() : "库存不足";
                log.error("支付失败：订单ID{}，库存扣减失败，原因：{}", orderId, errMsg);
                throw new BusinessException("支付失败：" + errMsg);
            }

            // 6. 修改支付状态为【支付成功】
            Result<Boolean> paySuccessRes = updateStatusService.updateStatus(paymentId, PaymentStatusEnum.PAY_SUCCESS.getCode());
            if (paySuccessRes == null || !paySuccessRes.getSuccess()) {
                String errMsg = paySuccessRes != null ? paySuccessRes.getMessage() : "修改支付成功状态接口返回空";
                log.error("支付异常：订单ID{}，修改支付状态为支付成功失败，原因：{}", orderId, errMsg);
                throw new BusinessException("支付异常：系统故障，请联系客服");
            }

            // 7. 修改订单状态为【待发货】
            Result<Boolean> orderStatusRes = orderClient.updateOrderStatus(orderId, OrderStatusEnum.WAIT_DELIVER.getCode());
            if (orderStatusRes == null) {
                log.error("支付异常：订单ID{}，调用订单状态更新接口返回空", orderId);
                throw new BusinessException("支付异常：订单服务异常");
            }
            if (!orderStatusRes.getSuccess() ) {
                String errMsg = orderStatusRes.getMessage() != null ? orderStatusRes.getMessage() : "订单状态更新失败";
                log.error("支付异常：订单ID{}，修改订单状态为待发货失败，原因：{}", orderId, errMsg);
                throw new BusinessException("支付异常：" + errMsg);
            }

            log.info("支付成功：订单ID{}，支付流水号{}，用户ID{}", orderId, payment.getPaymentNo(), userId);
            // 查询最新的支付记录返回（确保返回数据是数据库最新状态）
            return this.getPaymentByOrderId(orderId);

        } catch (Exception e) {
            // 仅记录异常日志，无需手动更新支付状态（Seata会自动回滚事务，恢复初始状态）
            log.error("支付流程异常：订单ID{}，支付流水号{}，异常信息：{}", orderId, payment.getPaymentNo(), e.getMessage(), e);
            // 关键：重新抛出异常，触发Seata分布式事务回滚
            throw new BusinessException("支付失败：" + e.getMessage());
        }
    }


    @NotNull
    private static List<InventoryDeductDTO> getInventoryDeductDTOS(PaymentPayDto dto) {
        List<OrderItemForPayment> orderItems=dto.getOrderItems();
        List<InventoryDeductDTO> inventoryDeductDTOList=new ArrayList<>();
        for(OrderItemForPayment orderItem:orderItems){
            InventoryDeductDTO inventoryDeductDTO=new InventoryDeductDTO();
            inventoryDeductDTO.setProductId(orderItem.getProductId());
            inventoryDeductDTO.setQuantity(orderItem.getQuantity());
            inventoryDeductDTOList.add(inventoryDeductDTO);
        }
        return inventoryDeductDTOList;
    }


    //创建支付实体
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPayment(PaymentCreateDto dto){
        Payment payment=new Payment();
        payment.setUserId(dto.getUserId());//设置用户Id
        payment.setPaymentNo("B" + generateSerialNo()); //b-balance);//设置支付流水号
        payment.setOrderNo(dto.getOrderNo());//设置订单流水线号
        payment.setOrderId(dto.getOrderId());//设置订单id
        payment.setAmount(dto.getAmount());//设置用户真正需要付的金额），要用于余额扣减
        payment.setPayType(-1);//-1表示未设置支付方式（一定要在这个给payType赋值，因为数据库将其设置为了必填字段）
        payment.setStatus(0);//待支付
        if(paymentMapper.insert(payment)<=0){
            log.error("支付记录创建失败");
            return false;
        }
        return true;
    }

//    //更新支付状态
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> updateStatus(Long paymentId, Integer newStatus){
//        // 这个方法将委托给UpdateStatusService处理，以确保事务正确生效
//        return updateStatusService.updateStatus(paymentId, newStatus);
//    }


    @Override
    public Page<Payment> getPaymentPage(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        Page<Payment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(Payment::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Payment::getStatus, status);
        }
        
        wrapper.orderByDesc(Payment::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Payment getPaymentByPaymentNo(String paymentNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getPaymentNo, paymentNo);
        return this.getOne(wrapper);
    }

    @Override
    public Payment getPaymentByOrderNo(String orderNo) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getOrderNo, orderNo);
        return this.getOne(wrapper);
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Payment::getOrderId,orderId);
        return this.getOne(wrapper);
    }


}