package com.emily.mall.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_inventory")
public class Inventory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 可用库存
     */
    private Integer availableStock;

    /**
     * 锁定库存
     */
    private Integer lockedStock;

    /**
     * 总库存
     */
    private Integer totalStock;

    /**
     * 库存预警值
     */
    private Integer warningStock;

    /**
     * 备注
     */
    private String remark;
}
