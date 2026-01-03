package com.emily.mall.inventory.dto;

import com.emily.mall.common.dto.ProductForCartVo;
import com.emily.mall.inventory.entity.Inventory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeInventoryVo {
    private String productName;
    private Long productId;
    private Integer availableStock;
    private Integer warningStock;//低于这个值可以触发补货提醒

    public ChangeInventoryVo(Inventory inventory, ProductForCartVo vo){
        this.availableStock=inventory.getAvailableStock();
        this.warningStock=inventory.getWarningStock();
        this.productId=vo.getId();
        this.productName=vo.getName();
    }
}
