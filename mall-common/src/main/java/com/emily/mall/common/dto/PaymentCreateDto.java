package com.emily.mall.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateDto {
    private Long userId;
    private String orderNo;
    private Long orderId;
    private BigDecimal amount;
}
