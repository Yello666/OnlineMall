package com.emily.mall.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ClientCategoryProductVo {
//    private String id;
    private String name;//商品总体名字（电脑）
    private String code;//商品总体编号
    private List<CategoryVo> categoryVos;//规格列表

}
