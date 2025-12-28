package com.emily.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品基础信息实体(无规格版)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品基础ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品码（同种商品的码相同）
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品主图
     */
    private String image;

    /**
     * 品牌ID
     */
    private Long brandId;

    //价格（只有一个）
    private BigDecimal price;

    //销量
    private Integer sales;

    /**
     * 商品状态(0:下架 1:上架)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;



//
//    /**
//     * 商品图片列表(逗号分隔)
//     */
//    private String images;

//    /**
//     * 商品详情
//     */
//    private String detail;

    //商品库存（不是准确的数值）
//    private Integer stock;




}
