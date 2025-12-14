package com.emily.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 商品实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品图片列表(逗号分隔)
     */
    private String images;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 商品状态(0:下架 1:上架)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;
}
