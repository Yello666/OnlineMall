package com.emily.mall.payment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.dto.PaymentCreateDto;
import com.emily.mall.common.dto.PaymentPayDto;
import com.emily.mall.common.result.Result;
import com.emily.mall.payment.entity.Payment;

/**
 * 支付服务接口
 */
public interface PaymentService extends IService<Payment> {

    //创建支付实体（用户下单触发）-创建后设置支付状态为0（待支付）
    Boolean createPayment(PaymentCreateDto dto);


    //支付订单
    Payment payOrder(PaymentPayDto dto);

    /**
     * 分页查询支付记录
     */
    Page<Payment> getPaymentPage(Integer pageNum, Integer pageSize, Long userId, Integer status);

    /**
     * 根据支付流水号查询
     */
    Payment getPaymentByPaymentNo(String paymentNo);

    /**
     * 根据订单号查询
     */
    Payment getPaymentByOrderNo(String orderNo);

//    Result<Boolean> updateStatus(Long paymentId, Integer newStatus);

    Payment getPaymentByOrderId(Long orderId);
}
