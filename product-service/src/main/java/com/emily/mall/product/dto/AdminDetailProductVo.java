package com.emily.mall.product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminDetailProductVo {
    private String code;
    private String name;//商品种类名字（电脑）
    private String description;//商品描述
    //    商品图片列表(逗号分隔)
    private String images;
    private List<CategoryVo> categoryVos;//规格列表
    private Long createBy;
    private Long updateBy;
}

