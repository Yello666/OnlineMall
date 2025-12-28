package com.emily.mall.common.dto;

import lombok.Data;

@Data
public class InventoryUpdateDto {
    private Long productId;
    private Integer newStock;
}
