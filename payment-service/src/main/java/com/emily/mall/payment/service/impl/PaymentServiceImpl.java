package com.emily.mall.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.payment.entity.Payment;
import com.emily.mall.payment.mapper.PaymentMapper;
import com.emily.mall.payment.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

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
}
