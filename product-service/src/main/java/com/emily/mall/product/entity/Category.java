package com.emily.mall.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_category")
public class Category extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;        // 分类名称，如 "手机"
    private Integer sort;       // 排序
    private String image;           //图片

//    private Long parentId;      // 父分类ID，用于构建树形结构
//    private Integer level;      // 层级：1=一级分类，2=二级...

//    private Integer status;     // 是否启用
}
