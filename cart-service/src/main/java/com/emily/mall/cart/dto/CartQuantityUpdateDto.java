package com.emily.mall.cart.dto;

import lombok.Data;

@Data
public class CartQuantityUpdateDto {
    private Long id;
    private Integer newQuantity;
}
