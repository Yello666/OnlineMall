package com.emily.mall.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoForPayment {
    private static final long serialVersionUID = 1L;

    /**
     * 订单明细列表
     */
    private List<OrderItemForPayment> orderItems;



}

