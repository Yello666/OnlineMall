package com.emily.mall.payment.service;

import com.emily.mall.common.result.Result;

/**
 * 支付状态更新服务接口
 * 用于处理需要事务控制的支付状态更新操作，避免内部方法调用导致的事务失效问题
 */
public interface UpdateStatusService {

    /**
     * 更新支付状态
     * @param paymentId 支付ID
     * @param newStatus 新状态
     * @return 操作结果
     */
    Result<Boolean> updateStatus(Long paymentId, Integer newStatus);
}