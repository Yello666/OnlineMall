//package com.emily.mall.product.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.emily.mall.common.entity.BaseEntity;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import java.math.BigDecimal;
//
///**
// * 商品规格关联实体（具体商品信息）
// */
//@Data
//@EqualsAndHashCode(callSuper = true)
//@TableName("tb_product_spec")
//public class ProductSpec extends BaseEntity {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 具体商品ID（唯一标识一件商品）
//     */
//    @TableId(type = IdType.ASSIGN_ID)
//    private Long id;
//
//    /**
//     * 商品码
//     */
//    private String productCode;
//
//    /**
//     * 规格ID
//     */
//    private Long specificationId;
//
//    /**
//     * 商品价格
//     */
//    private BigDecimal price;
//
//    /**
//     * 原价
//     */
//    private BigDecimal originalPrice;
//
//    /**
//     * 库存数量
//     */
//    private Integer stock;
//
//    /**
//     * 销量
//     */
//    private Integer sales;
//
//    /**
//     * 规格图片
//     */
//    private String image;
//
//    /**
//     * 商品状态(0:下架 1:上架)
//     */
//    private Integer status;
//
//    /**
//     * 排序
//     */
//    private Integer sort;
//}