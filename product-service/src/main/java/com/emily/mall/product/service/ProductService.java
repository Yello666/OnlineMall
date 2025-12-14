package com.emily.mall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.product.entity.Product;

public interface ProductService extends IService<Product> {

    Page<Product> getProductPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId, Integer status);

    Product getProductByCode(String code);
}
