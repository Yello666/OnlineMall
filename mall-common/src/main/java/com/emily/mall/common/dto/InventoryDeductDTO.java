package com.emily.mall.common.dto;

import lombok.Data;

@Data
public class InventoryDeductDTO {
    private Long productId;
    private Integer quantity;
}
