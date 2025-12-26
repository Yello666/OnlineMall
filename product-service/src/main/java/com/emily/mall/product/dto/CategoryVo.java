package com.emily.mall.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CategoryVo {
    private String id;//商品id
    private String categoryId;
    private String categoryName;//商品规格名字（黑色/白色）
    private String image;//对应规格的照片
    private BigDecimal price;//对应规格的现价
    private BigDecimal originalPrice;//对应规格的原价
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
