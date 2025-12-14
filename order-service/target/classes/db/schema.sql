-- 订单数据库
CREATE DATABASE IF NOT EXISTS `mall_order` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_order`;

-- 订单表
CREATE TABLE IF NOT EXISTS `tb_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `pay_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实付金额',
  `freight` DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费',
  `discount_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
  `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货地址',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '订单状态(0:待支付 1:待发货 2:待收货 3:已完成 4:已取消 5:已关闭)',
  `pay_type` TINYINT(1) DEFAULT NULL COMMENT '支付方式(1:支付宝 2:微信 3:银联)',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS `tb_order_item` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
  `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
  `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
  `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
  `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `quantity` INT(11) NOT NULL DEFAULT 1 COMMENT '购买数量',
  `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计金额',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';
