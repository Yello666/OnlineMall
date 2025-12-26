package com.emily.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.mapper.BrandMapper;
import com.emily.mall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 品牌服务实现类
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 1.创建品牌
     */
    @Override
    public Result<Brand> createBrand(Brand brand) {
        // 检查品牌名是否已存在
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Brand::getName, brand.getName());
        Brand existBrand = brandMapper.selectOne(wrapper);
        if (existBrand != null) {
            return Result.fail("品牌名已存在");
        }
        int result = brandMapper.insert(brand);
        return result > 0 ? Result.ok(brand) : Result.fail("创建品牌失败");
    }

    /**
     * 2.修改品牌
     */
    @Override
    public Result<Brand> updateBrand(Brand brand) {
        if (brand.getId() == null) {
            return Result.fail("品牌ID不能为空");
        }
        Brand existBrand = brandMapper.selectById(brand.getId());
        if (existBrand == null) {
            return Result.fail("品牌不存在");
        }
        int result = brandMapper.updateById(brand);
        return result > 0 ? Result.ok(brand) : Result.fail("修改品牌失败");
    }
}
