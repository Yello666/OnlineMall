package com.emily.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Category;

/**
 * 商品分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 3.添加商品规格
     * @param category 商品规格信息
     * @return 添加结果
     */
    Result<Category> createCategory(Category category);

    /**
     * 4.修改商品规格
     * @param category 商品规格信息
     * @return 修改结果
     */
    Result<Category> updateCategory(Category category);

    /**
     * 5.删除商品规格(根据ID)
     * @param id 商品规格ID
     * @return 删除结果
     */
    Result<Boolean> deleteCategoryById(Long id);

    /**
     * 6.删除商品规格(根据名称)
     * @param name 商品规格名称
     * @return 删除结果
     */
    Result<Boolean> deleteCategoryByName(String name);

    /**
     * 7.查看商品规格(根据ID)
     * @param id 商品规格ID
     * @return 商品规格信息
     */
    Result<Category> getCategoryById(Long id);

    /**
     * 8.查看商品规格(根据名称)
     * @param name 商品规格名称
     * @return 商品规格信息
     */
    Result<Category> getCategoryByName(String name);
}
