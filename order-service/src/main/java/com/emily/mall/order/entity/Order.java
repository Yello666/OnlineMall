package com.emily.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;

/**
 * 订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_order")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单明细列表
     */
    @TableField(exist = false)
    private List<OrderItem> orderItems;

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 订单状态(0:待支付 1:待发货 2:待收货 3:已完成 4:已取消 5:已关闭)
     */
    private Integer status;

    /**
     * 支付方式(1:支付宝 2:微信 3:银联)
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remark;
}
