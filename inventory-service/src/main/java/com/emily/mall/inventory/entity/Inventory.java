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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    //商品编码
//    private String productCode;

    /**
     * 仓库ID (库存所在的仓库)
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
    private Integer totalStock;//可用库存+锁定库存

    /**
     * 库存预警值
     */
    private Integer warningStock;//低于这个值可以触发补货提醒

    //备注
//    private String remark;
}
