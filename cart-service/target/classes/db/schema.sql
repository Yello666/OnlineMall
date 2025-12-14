-- 购物车数据库
CREATE DATABASE IF NOT EXISTS `mall_cart` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_cart`;

-- 购物车商品表
CREATE TABLE IF NOT EXISTS `tb_cart_item` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '购物车项ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
  `product_code` VARCHAR(64) DEFAULT NULL COMMENT '商品编码',
  `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
  `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `quantity` INT(11) NOT NULL DEFAULT 1 COMMENT '购买数量',
  `selected` TINYINT(1) DEFAULT 1 COMMENT '是否选中(0:未选中 1:已选中)',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车商品表';
