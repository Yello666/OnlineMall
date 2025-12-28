package com.emily.mall.cart.dto;

import lombok.Data;



@Data
public class CartItemDto {
    private Long productId;
    private Integer quantity;
}
