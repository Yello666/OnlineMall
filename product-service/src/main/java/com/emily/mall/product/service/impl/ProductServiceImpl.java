package com.emily.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.mapper.ProductMapper;
import com.emily.mall.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Page<Product> getProductPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId, Integer status) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword)
                   .or()
                   .like(Product::getCode, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        }
        
        wrapper.orderByDesc(Product::getSort, Product::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Product getProductByCode(String code) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, code);
        return this.getOne(wrapper);
    }
}
