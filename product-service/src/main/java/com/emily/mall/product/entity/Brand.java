package com.emily.mall.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_brand")
public class Brand extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;        // 品牌名称，如 "Apple"
    private String logo;        // 品牌Logo图片URL
    private String description;// 品牌介绍

}