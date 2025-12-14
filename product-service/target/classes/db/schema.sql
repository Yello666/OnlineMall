-- 商品数据库
CREATE DATABASE IF NOT EXISTS `mall_product` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_product`;

-- 商品表
CREATE TABLE IF NOT EXISTS `tb_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
  `code` VARCHAR(64) NOT NULL COMMENT '商品编码',
  `category_id` BIGINT(20) DEFAULT NULL COMMENT '商品分类ID',
  `brand_id` BIGINT(20) DEFAULT NULL COMMENT '品牌ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `stock` INT(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `sales` INT(11) DEFAULT 0 COMMENT '销量',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
  `images` TEXT DEFAULT NULL COMMENT '商品图片列表(逗号分隔)',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '商品描述',
  `detail` TEXT DEFAULT NULL COMMENT '商品详情',
  `status` TINYINT(1) DEFAULT 1 COMMENT '商品状态(0:下架 1:上架)',
  `sort` INT(11) DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
