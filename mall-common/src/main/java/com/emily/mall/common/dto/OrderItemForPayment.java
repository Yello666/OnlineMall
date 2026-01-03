package com.emily.mall.common.dto;

import lombok.Data;

@Data
public class OrderItemForPayment {
    private Long productId;
    private Integer quantity;
}
