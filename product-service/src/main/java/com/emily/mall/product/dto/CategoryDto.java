package com.emily.mall.product.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private String name;        // 分类名称，如 "手机"
    private Integer sort;       // 排序

}
