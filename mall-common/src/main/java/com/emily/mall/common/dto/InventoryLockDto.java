package com.emily.mall.common.dto;

import lombok.Data;

@Data
public class InventoryLockDto {
    private Long productId;
    private Integer quantity;
}
