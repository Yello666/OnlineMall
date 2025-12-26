package com.emily.mall.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClientDetailProductVo {
    private String code;
    private String name;//商品种类名字（电脑）
    private String description;//商品描述
//    商品图片列表(逗号分隔)
    private String images;
    private BigDecimal price;//商品规格的最低价
    private BigDecimal originalPrice;//商品规格的最低价

}
