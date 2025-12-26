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
     * 商品ID（唯一标识一个商品）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

//    商品编码（由于商品可以有很多个分类，一个商品编码代表一种商品）商品编码+商品分类ID也可以唯一标识一个商品
    //每一个code的商品名是一样的，category对应的名字可以不一样。
    private String code;

    /**
     * 商品分类ID
     */
    private Long categoryId; //1-黑色  2-白色

    /**
     * 品牌ID
     */
    private Long brandId; //可以规定1001是苹果，1002是乌萨奇的滑溜溜布丁



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
     * 商品状态(0:下架 1:上架)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;//每个商品的权重，越小越靠前

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品现价
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    //    /**
//     * 商品详情
//     */
//    private String detail;

}
