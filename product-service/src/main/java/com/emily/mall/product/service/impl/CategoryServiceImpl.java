package com.emily.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Category;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.mapper.CategoryMapper;
import com.emily.mall.product.mapper.ProductMapper;
import com.emily.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 3.添加商品规格
     */
    @Override
    public Result<Category> createCategory(Category category) {
        // 检查规格名是否已存在
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        Category existCategory = categoryMapper.selectOne(wrapper);
        if (existCategory != null) {
            return Result.fail("商品规格名已存在");
        }
        int result = categoryMapper.insert(category);
        return result > 0 ? Result.ok(category) : Result.fail("添加商品规格失败");
    }

    /**
     * 4.修改商品规格
     */
    @Override
    public Result<Category> updateCategory(Category category) {
        if (category.getId() == null) {
            return Result.fail("分类ID不能为空");
        }
        Category existCategory = categoryMapper.selectById(category.getId());
        if (existCategory == null) {
            return Result.fail("商品规格不存在");
        }
        int result = categoryMapper.updateById(category);
        return result > 0 ? Result.ok(category) : Result.fail("修改商品规格失败");
    }

    /**
     * 5.删除商品规格(根据ID)
     */
    @Override
    public Result<Boolean> deleteCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            return Result.fail("商品规格不存在");
        }
        // 检查是否有商品使用该规格
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, id);
        Long count = productMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.fail("该规格下还有商品，不能删除");
        }
        int result = categoryMapper.deleteById(id);
        return result > 0 ? Result.ok(true) : Result.fail("删除商品规格失败");
    }

    /**
     * 6.删除商品规格(根据名称)
     */
    @Override
    public Result<Boolean> deleteCategoryByName(String name) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, name);
        Category category = categoryMapper.selectOne(wrapper);
        if (category == null) {
            return Result.fail("商品规格不存在");
        }
        return deleteCategoryById(category.getId());
    }

    /**
     * 7.查看商品规格(根据ID)
     */
    @Override
    public Result<Category> getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        return category != null ? Result.ok(category) : Result.fail("商品规格不存在");
    }

    /**
     * 8.查看商品规格(根据名称)
     */
    @Override
    public Result<Category> getCategoryByName(String name) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, name);
        Category category = categoryMapper.selectOne(wrapper);
        return category != null ? Result.ok(category) : Result.fail("商品规格不存在");
    }
}
