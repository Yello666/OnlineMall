package com.emily.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.dto.*;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.entity.Category;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.service.BrandService;
import com.emily.mall.product.service.CategoryService;
import com.emily.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    // 1.新建品牌
    @PostMapping("/brand")
    public Result<Brand> createBrand(@RequestBody Brand brand) {
        return brandService.createBrand(brand);
    }

    // 2.修改品牌信息
    @PutMapping("/brand")
    public Result<Brand> updateBrand(@RequestBody Brand brand) {
        return brandService.updateBrand(brand);
    }

    // 3.删除品牌
    @DeleteMapping("/brand/{id}")
    public Result<Boolean> deleteBrand(@PathVariable Long id) {
        return Result.ok(brandService.removeById(id));
    }

    // 4.查看品牌信息
    @GetMapping("/brand/{id}")
    public Result<Brand> getBrand(@PathVariable Long id) {
        return Result.ok(brandService.getById(id));
    }

    // 5.新建商品规格
    @PostMapping("/category")
    public Result<Category> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // 6.删除商品规格
    @DeleteMapping("/category/{id}")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategoryById(id);
    }

    // 7.修改商品规格
    @PutMapping("/category")
    public Result<Category> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    // 8.创建商品
    @PostMapping("/create")
    public Result<Category> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // 9.下架商品
    @PutMapping("/remove/{id}")
    public Result<Boolean> removeProduct(@PathVariable Long id) {
        return productService.removeProductById(id);
    }

    // 10.删除商品
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    // 11.顾客视角查看商品列表
    @GetMapping("/client/list")
    public Result<Page<ClientListProductVo>> getClientProductList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return productService.getClientProductList(pageNum, pageSize, keyword);
    }

    // 12.顾客视角查看商品详情页
    @GetMapping("/client/detail/{code}")
    public Result<ClientDetailProductVo> getClientProductDetail(@PathVariable String code) {
        Product product = new Product();
        product.setCode(code);
        return productService.getProductDetailInfo(product);
    }

    // 13.顾客视角查看商品规格
    @GetMapping("/client/category")
    public Result<ClientCategoryProductVo> getClientCategoryProduct(
            @RequestParam String code,
            @RequestParam(required = false) Long categoryId) {
        return productService.getProductCategoryInfo(code, categoryId);
    }

    // 14.商家视角查看商品列表
    @GetMapping("/merchant/list")
    public Result<Page<ClientListProductVo>> getMerchantProductList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return productService.getMerchantProductList(pageNum, pageSize, keyword);
    }

    // 15.商家视角查看商品详情页
    @GetMapping("/merchant/detail/{productId}")
    public Result<MerchantDetailProductVo> getMerchantProductDetail(@PathVariable Long productId) {
        return productService.merchantGetProductDetailInfo(productId);
    }

    // 16.管理员视角查看商品详情页
    @GetMapping("/admin/detail/{code}")
    public Result<AdminDetailProductVo> getAdminProductDetail(@PathVariable String code) {
        return productService.adminGetProductDetailInfo(code);
    }

    // 17.将商品添加进购物车（移动到购物车接口）
    @PostMapping("/cart")
    public Result<Boolean> addProductToCart(@RequestBody Product product) {
        return productService.addProductToCart(product);
    }

    // 18.修改商品库存（移动到库存服务，商品服务通过消息队列监听库存服务，库存修改的时候，异步同步库存）
    @PutMapping("/inventory")
    public Result<Boolean> changeProductInventory(@RequestParam Long id, @RequestParam Integer newStock) {
        return productService.changeProductInventory(id, newStock);
    }

    /**
     * 分页查询商品列表 (通用)
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
     * 根据ID查询商品 (通用)
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    /**
     * 根据商品编码查询 (通用)
     */
    @GetMapping("/code/{code}")
    public Result<Product> getByCode(@PathVariable String code) {
        Product product = productService.getProductByCode(code);
        return Result.ok(product);
    }

    /**
     * 修改商品 (通用)
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody Product product) {
        boolean result = productService.updateById(product);
        return Result.ok(result);
    }
}
