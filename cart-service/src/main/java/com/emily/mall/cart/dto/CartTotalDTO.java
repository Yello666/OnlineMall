package com.emily.mall.cart.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车总价计算结果DTO
 */
@Data
public class CartTotalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车商品总数量
     */
    private Integer totalQuantity;

    /**
     * 购物车商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 已选中商品数量
     */
    private Integer selectedQuantity;

    /**
     * 已选中商品总金额
     */
    private BigDecimal selectedAmount;
}
