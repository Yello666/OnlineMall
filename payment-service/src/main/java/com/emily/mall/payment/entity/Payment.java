package com.emily.mall.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.dto.OrderInfoForPayment;
import com.emily.mall.common.dto.OrderItemForPayment;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

//    @TableField(exist = false)
//    private List<OrderItemForPayment> orderItems;


    /**
     * 支付流水号
     */
    private String paymentNo;
    //可以是第三方流水号
//    id（支付 ID）：对内、无业务含义、数据库唯一，用于系统内部数据操作；如果外漏可能会泄漏数据库信息
//    paymentNo（支付流水号）：对外、有业务含义、全局唯一，用于业务追溯、外部对接、用户对账；可能是PAY_3_20251103这样有业务信息的字符串
//    两者不可替代，是 “内部标识” 与 “外部业务凭证” 的分工配合，符合企业级系统的设计规范。
//    微信支付、支付宝、银联等第三方支付平台，以及银行结算系统、财务报税系统等，都明确要求
//    提供具有业务属性的唯一支付流水号，不接受数据库内部的 id（Long 类型自增 / 雪花 ID）。
    /**
     * 订单流水号（向外展示的时候是展示流水号）
     */

    private Long orderId;

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
     * 第三方支付流水号（调用第三方api的时候才使用吧，现在采用余额支付）
     */
//    private String tradeNo;

//    /**
//     * 支付时间BaseEntity有create_time)
//     */
//    private LocalDateTime payTime;

    /**
     * 备注
     */
//    private String remark;
}
