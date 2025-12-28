-- 商品数据库
CREATE DATABASE IF NOT EXISTS `mall_product` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_product`;

-- 品牌表
CREATE TABLE IF NOT EXISTS `tb_brand` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` VARCHAR(50) NOT NULL COMMENT '品牌名称',
  `logo` VARCHAR(500) NULL COMMENT '品牌图片url',
  `description` VARCHAR(200) NULL COMMENT '品牌介绍',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_brand_name` (`name`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表';

-- 商品基础信息表
CREATE TABLE IF NOT EXISTS `tb_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品基础ID',
  `code` VARCHAR(64) NOT NULL COMMENT '商品码（同种商品的码相同）',
  `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '商品描述',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '商品主图',
  `brand_id` BIGINT(20) DEFAULT NULL COMMENT '品牌ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `sales` INT(11) DEFAULT 0 COMMENT '销量',
  `status` TINYINT(1) DEFAULT 1 COMMENT '商品状态(0:下架 1:上架)',
  `sort` INT(11) DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`code`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品基础信息表';

-- 规格表--处在废弃状态
CREATE TABLE IF NOT EXISTS `tb_specification` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `name` VARCHAR(50) NOT NULL COMMENT '规格名称（如：黑色、白色、S码、M码等）',
  `description` VARCHAR(200) NULL COMMENT '规格描述',
  `sort` INT(11) DEFAULT 0 COMMENT '规格权重，越大的越靠前',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规格表';

-- 商品规格关联表（具体商品，包含价格、库存等信息）--处在废弃状态
CREATE TABLE IF NOT EXISTS `tb_product_spec` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '具体商品ID（唯一标识一件商品）',
  `product_code` VARCHAR(64) NOT NULL COMMENT '商品码',
  `specification_id` BIGINT(20) NOT NULL COMMENT '规格ID',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `stock` INT(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `sales` INT(11) DEFAULT 0 COMMENT '销量',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '规格图片',
  `status` TINYINT(1) DEFAULT 1 COMMENT '商品状态(0:下架 1:上架)',
  `sort` INT(11) DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
  `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标记(0:未删除 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code_spec_id` (`product_code`, `specification_id`),
  KEY `idx_product_code` (`product_code`),
  KEY `idx_specification_id` (`specification_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`),
  CONSTRAINT `fk_product_spec_product_code` FOREIGN KEY (`product_code`) REFERENCES `tb_product` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_spec_specification_id` FOREIGN KEY (`specification_id`) REFERENCES `tb_specification` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格关联表（具体商品信息）';

-- 创建索引以优化查询
CREATE INDEX idx_product_spec_product_code_status ON tb_product_spec(product_code, status);