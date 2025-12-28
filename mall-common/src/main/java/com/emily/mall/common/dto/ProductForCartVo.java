package com.emily.mall.common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductForCartVo {
    private Long id;//商品id
    private String name;//商品名称
    private String image;//商品图片
    private BigDecimal price;//商品价格


}
