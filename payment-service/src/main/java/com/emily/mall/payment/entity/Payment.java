package com.emily.mall.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_payment")
public class Payment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 支付流水号
     */
    private String paymentNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式(1:支付宝 2:微信 3:余额点支付)
     */
    private Integer payType;

    /**
     * 支付状态(0:待支付 1:支付中 2:支付成功 3:支付失败 4:已退款)
     */
    private Integer status;

    /**
     * 第三方支付流水号
     */
    private String tradeNo;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 备注
     */
    private String remark;
}
