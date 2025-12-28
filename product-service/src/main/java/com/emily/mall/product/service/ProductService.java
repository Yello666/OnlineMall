package com.emily.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Product;

public interface ProductService extends IService<Product> {

    // 创建商品
    Result<Product> createProduct(Product product);

    // 根据ID删除商品
    Result<Boolean> deleteProductById(Long id);
    
    // 根据商品码删除商品
    Result<Boolean> deleteProductByCode(String code);

    // 根据ID下架商品
    Result<Boolean> removeProductById(Long id);

    // 根据商品码获取商品
    Product getProductByCode(String code);

}