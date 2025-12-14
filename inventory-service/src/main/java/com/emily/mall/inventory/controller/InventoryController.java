package com.emily.mall.inventory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.inventory.entity.Inventory;
import com.emily.mall.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public Result<Boolean> createInventory(@RequestBody Inventory inventory) {
        boolean success = inventoryService.save(inventory);
        return success ? Result.ok(success) : Result.fail("创建库存失败");
    }

    @PostMapping("/batch")
    public Result<Boolean> createInventoryBatch(@RequestBody List<Inventory> inventories) {
        boolean success = inventoryService.saveBatch(inventories);
        return success ? Result.ok(success) : Result.fail("批量创建库存失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteInventory(@PathVariable Long id) {
        boolean success = inventoryService.removeById(id);
        return success ? Result.ok(success) : Result.fail("删除库存失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> deleteInventoryBatch(@RequestBody List<Long> ids) {
        boolean success = inventoryService.removeByIds(ids);
        return success ? Result.ok(success) : Result.fail("批量删除库存失败");
    }

    @PutMapping
    public Result<Boolean> updateInventory(@RequestBody Inventory inventory) {
        boolean success = inventoryService.updateById(inventory);
        return success ? Result.ok(success) : Result.fail("更新库存失败");
    }

    @PutMapping("/batch")
    public Result<Boolean> updateInventoryBatch(@RequestBody List<Inventory> inventories) {
        boolean success = inventoryService.updateBatchById(inventories);
        return success ? Result.ok(success) : Result.fail("批量更新库存失败");
    }

    @GetMapping("/{id}")
    public Result<Inventory> getInventory(@PathVariable Long id) {
        Inventory inventory = inventoryService.getById(id);
        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
    }

    @GetMapping("/batch")
    public Result<List<Inventory>> getInventoryBatch(@RequestParam List<Long> ids) {
        List<Inventory> inventories = inventoryService.listByIds(ids);
        return Result.ok(inventories);
    }

    @GetMapping("/list")
    public Result<List<Inventory>> getInventoryList() {
        List<Inventory> inventories = inventoryService.list();
        return Result.ok(inventories);
    }

    @GetMapping("/page")
    public Result<Page<Inventory>> getInventoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId) {
        Page<Inventory> page = inventoryService.getInventoryPage(pageNum, pageSize, productId, warehouseId);
        return Result.ok(page);
    }

    @GetMapping("/product/{productId}")
    public Result<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);
        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
    }

    @GetMapping("/productCode/{productCode}")
    public Result<Inventory> getInventoryByProductCode(@PathVariable String productCode) {
        Inventory inventory = inventoryService.getInventoryByProductCode(productCode);
        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
    }
}
