package com.emily.mall.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientListProductVo {
    private String id;
    private String code;
    private String name;
    private String brandId;
    private String brandName;
    private String image;
    private Integer sales;
    private BigDecimal originalPrice;//规格的最低价
    private BigDecimal price;//规格的最低价
    private String description;//描述的前20个字符
}
