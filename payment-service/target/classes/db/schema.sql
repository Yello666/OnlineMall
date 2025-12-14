-- 支付数据库
CREATE DATABASE IF NOT EXISTS `mall_payment` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_payment`;

-- 支付记录表
CREATE TABLE IF NOT EXISTS `tb_payment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `payment_no` VARCHAR(64) NOT NULL COMMENT '支付流水号',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
  `pay_type` TINYINT(1) NOT NULL COMMENT '支付方式(1:支付宝 2:微信 3:银联)',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付状态(0:待支付 1:支付中 2:支付成功 3:支付失败 4:已退款)',
  `trade_no` VARCHAR(100) DEFAULT NULL COMMENT '第三方支付流水号',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';
