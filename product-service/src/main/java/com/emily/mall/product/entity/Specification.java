//package com.emily.mall.product.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.emily.mall.common.entity.BaseEntity;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
///**
// * 规格实体
// */
//@Data
//@EqualsAndHashCode(callSuper = true)
//@TableName("tb_specification")
//public class Specification extends BaseEntity {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 规格ID
//     */
//    @TableId(type = IdType.ASSIGN_ID)
//    private Long id;
//
//    /**
//     * 规格名称（如：黑色、白色、S码、M码等）
//     */
//    private String name;
//
//    /**
//     * 规格描述
//     */
//    private String description;
//
//    /**
//     * 规格权重，越大的越靠前
//     */
//    private Integer sort;
//}
