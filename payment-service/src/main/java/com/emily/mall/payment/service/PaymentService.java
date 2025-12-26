package com.emily.mall.payment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.payment.entity.Payment;
import org.springframework.core.annotation.Order;

/**
 * 支付服务接口
 */
public interface PaymentService extends IService<Payment> {
    //支付订单
    Payment payOrder(Order order);

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
}
