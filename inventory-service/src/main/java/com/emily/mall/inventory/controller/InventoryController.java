package com.emily.mall.inventory.controller;


import com.emily.mall.common.dto.InventoryLockDto;
import com.emily.mall.common.dto.InventoryUpdateDto;
import com.emily.mall.common.result.Result;
import com.emily.mall.inventory.entity.Inventory;
import com.emily.mall.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.emily.mall.common.dto.InventoryDeductDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {


    private final InventoryService inventoryService;

    //1.扣减库存（付款引发）
    @PutMapping("/deduct")
    public Result<Boolean> deductStock(@RequestBody List<InventoryDeductDTO> items) {
        inventoryService.deductStock(items);
        return Result.ok(true);
    }
    //2.锁定库存（下单引发）
    @PutMapping("/lock")
    public Result<Boolean> lockStock(@RequestBody List<InventoryLockDto> items){
        Boolean success=inventoryService.lockStock(items);
        return success? Result.ok(true):Result.fail("下单失败锁定库存失败");

    }

    //2.创建库存
    @PostMapping("/create")
    public Result<Boolean> createInventory(@RequestBody Inventory inventory) {
        boolean success = inventoryService.save(inventory);
        return success ? Result.ok(success) : Result.fail("创建库存失败");
    }

    //3.根据商品ID更新库存
    @PutMapping("/update")
    public Result<Boolean> updateInventoryByProductId(@RequestBody InventoryUpdateDto inventoryUpdateDto) {
        Boolean success=inventoryService.updateInventory(inventoryUpdateDto);
        return success ? Result.ok(success) : Result.fail("更新商品库存失败");
    }
    //4.根据productId删除库存
    @DeleteMapping
    public Result<Boolean> deleteInventory(@RequestParam Long productId) {
        boolean success = inventoryService.deleteInventoryByProductId(productId);
        return success ? Result.ok(success) : Result.fail("删除库存失败");
    }

    //5.查看库存（根据productId）
    @GetMapping("/product")
    public Result<Inventory> getInventoryByProductId(@RequestParam Long productId) {
        Inventory inventory= inventoryService.getInventoryByProductId(productId);
        return inventory!=null? Result.ok(inventory) : Result.fail("获取库存失败");
    }

    //删除库存By库存ID
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteInventoryByIid(@PathVariable Long id) {
        boolean success = inventoryService.removeById(id);
        return success ? Result.ok(success) : Result.fail("删除库存失败");
    }


//    @PostMapping("/batch")
//    public Result<Boolean> createInventoryBatch(@RequestBody List<Inventory> inventories) {
//        boolean success = inventoryService.saveBatch(inventories);
//        return success ? Result.ok(success) : Result.fail("批量创建库存失败");
//    }



//    @DeleteMapping("/batch")
//    public Result<Boolean> deleteInventoryBatch(@RequestBody List<Long> ids) {
//        boolean success = inventoryService.removeByIds(ids);
//        return success ? Result.ok(success) : Result.fail("批量删除库存失败");
//    }

//    @PutMapping
//    public Result<Boolean> updateInventory(@RequestBody Inventory inventory) {
//        boolean success = inventoryService.updateById(inventory);
//        return success ? Result.ok(success) : Result.fail("更新库存失败");
//    }

//    @PutMapping("/batch")
//    public Result<Boolean> updateInventoryBatch(@RequestBody List<Inventory> inventories) {
//        boolean success = inventoryService.updateBatchById(inventories);
//        return success ? Result.ok(success) : Result.fail("批量更新库存失败");
//    }

//    @GetMapping("/{id}")
//    public Result<Inventory> getInventory(@PathVariable Long id) {
//        Inventory inventory = inventoryService.getById(id);
//        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
//    }
//
//    @GetMapping("/batch")
//    public Result<List<Inventory>> getInventoryBatch(@RequestParam List<Long> ids) {
//        List<Inventory> inventories = inventoryService.listByIds(ids);
//        return Result.ok(inventories);
//    }
//
//    @GetMapping("/list")
//    public Result<List<Inventory>> getInventoryList() {
//        List<Inventory> inventories = inventoryService.list();
//        return Result.ok(inventories);
//    }

//    @GetMapping("/page")
//    public Result<Page<Inventory>> getInventoryPage(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(required = false) Long productId,
//            @RequestParam(required = false) Long warehouseId) {
//        Page<Inventory> page = inventoryService.getInventoryPage(pageNum, pageSize, productId, warehouseId);
//        return Result.ok(page);
//    }

//    @GetMapping("/product/{productId}")
//    public Result<Inventory> getInventoryByProductId(@PathVariable Long productId) {
//        Inventory inventory = inventoryService.getInventoryByProductId(productId);
//        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
//    }

//    @GetMapping("/productCode/{productCode}")
//    public Result<Inventory> getInventoryByProductCode(@PathVariable String productCode) {
//        Inventory inventory = inventoryService.getInventoryByProductCode(productCode);
//        return inventory != null ? Result.ok(inventory) : Result.fail("库存不存在");
//    }
}
