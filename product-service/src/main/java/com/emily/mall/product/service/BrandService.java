package com.emily.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Brand;

/**
 * 品牌服务接口
 */
public interface BrandService extends IService<Brand> {

    /**
     * 1.创建品牌
     * @param brand 品牌信息
     * @return 创建结果
     */
    Result<Brand> createBrand(Brand brand);

    /**
     * 2.修改品牌
     * @param brand 品牌信息
     * @return 修改结果
     */
    Result<Brand> updateBrand(Brand brand);
}
