package com.emily.mall.payment.service.impl;

import com.emily.mall.common.result.Result;
import com.emily.mall.payment.entity.Payment;
import com.emily.mall.payment.mapper.PaymentMapper;
import com.emily.mall.payment.service.UpdateStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateStatusServiceImpl implements UpdateStatusService {

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStatus(Long paymentId, Integer newStatus) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null) {
            log.error("支付实体不存在");
            return Result.fail("支付实体不存在");
        }
        payment.setStatus(newStatus);
        if (paymentMapper.updateById(payment) <= 0) {
            log.error("更新失败");
            return Result.fail("更新失败");
        }
        return Result.ok(true);
    }
}