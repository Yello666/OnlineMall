package com.emily.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.dto.ProductForCartVo;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Product;
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

    // 创建商品
    @PostMapping("/create")
    public Result<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    /**
     * 修改商品 (通用)
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody Product product) {
        boolean result = productService.updateById(product);
        return Result.ok(result);
    }

    // 下架商品
    @PutMapping("/remove/{id}")
    public Result<Boolean> removeProduct(@PathVariable Long id) {
        return productService.removeProductById(id);
    }

    // 删除商品 - 根据ID
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    // 删除商品 - 根据code
    @DeleteMapping("/code/{code}")
    public Result<Boolean> deleteProductByCode(@PathVariable String code) {
        return productService.deleteProductByCode(code);
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

    //给购物车微服务的接口
    @GetMapping("/forCart")
    public ProductForCartVo getProductForCart(@RequestParam("id") Long id) {
        Product product = productService.getById(id);
        if(product==null){
            log.error("不存在的商品{}",id);
            return null;
        }
        ProductForCartVo productForCartVo = new ProductForCartVo();
        productForCartVo.setId(product.getId());
        productForCartVo.setName(product.getName());
        productForCartVo.setImage(product.getImage());
        productForCartVo.setPrice(product.getPrice());
        log.debug(String.valueOf(productForCartVo));
        return productForCartVo;
    }

    /**
     * 商品列表分页查询
     * @param pageNum 页码（默认1）
     * @param pageSize 每页条数（默认10）
     * @return 分页商品数据
     */
    @GetMapping("/page")
    public Result<Page<Product>> getProductPageList(
            @RequestParam(required = false, defaultValue = "1") Long pageNum,
            @RequestParam(required = false, defaultValue = "10") Long pageSize) {
        return productService.getProductPageList(pageNum, pageSize);
    }

}
