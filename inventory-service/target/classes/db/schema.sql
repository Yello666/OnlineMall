-- 库存数据库
CREATE DATABASE IF NOT EXISTS `mall_inventory` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_inventory`;

-- 库存表
CREATE TABLE IF NOT EXISTS `tb_inventory` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
  `product_code` VARCHAR(64) NOT NULL COMMENT '商品编码',
  `warehouse_id` BIGINT(20) DEFAULT NULL COMMENT '仓库ID',
  `available_stock` INT(11) NOT NULL DEFAULT 0 COMMENT '可用库存',
  `locked_stock` INT(11) NOT NULL DEFAULT 0 COMMENT '锁定库存',
  `total_stock` INT(11) NOT NULL DEFAULT 0 COMMENT '总库存',
  `warning_stock` INT(11) DEFAULT 10 COMMENT '库存预警值',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_warehouse` (`product_id`, `warehouse_id`),
  KEY `idx_product_code` (`product_code`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';
