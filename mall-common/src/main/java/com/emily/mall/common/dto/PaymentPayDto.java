package com.emily.mall.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentPayDto {
    private Integer payType;
    private Long orderId;
    private List<OrderItemForPayment> orderItems;
}
