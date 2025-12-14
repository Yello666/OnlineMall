package com.emily.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 分页查询商品列表
     */
    @GetMapping("/page")
    public Result<Page<Product>> getProductPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        Page<Product> page = productService.getProductPage(pageNum, pageSize, keyword, categoryId, status);
        return Result.ok(page);
    }

    /**
     * 根据ID查询商品
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    /**
     * 根据商品编码查询
     */
    @GetMapping("/code/{code}")
    public Result<Product> getByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        return Result.ok(product);
    }

    /**
     * 新增商品
     */
    @PostMapping
    public Result<Boolean> save(@RequestBody Product product) {
        boolean result = productService.save(product);
        return Result.ok(result);
    }

    /**
     * 修改商品
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody Product product) {
        boolean result = productService.updateById(product);
        return Result.ok(result);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = productService.removeById(id);
        return Result.ok(result);
    }
}
