package com.emily.mall.product.dto;

import com.emily.mall.product.entity.Brand;
import lombok.Data;

@Data
public class BrandVo {
    private String id;
    private String name;        // 品牌名称，如 "Apple"
    private String logo;        // 品牌Logo图片URL
    private String description;// 品牌介绍

    public BrandVo(Brand brand){
        this.description= brand.getDescription();
        this.id=String.valueOf(brand.getId());
        this.logo= brand.getLogo();
        this.name= brand.getName();
    }

}
