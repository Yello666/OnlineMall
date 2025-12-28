package com.emily.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.dto.BrandVo;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌控制器 - 提供品牌的增删查改功能
 */
@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final BrandService brandService;

    // 1.创建品牌
    @PostMapping
    public Result<BrandVo> createBrand(@RequestBody Brand brand) {
        return brandService.createBrand(brand);
    }

    // 2.修改品牌信息
    @PutMapping
    public Result<BrandVo> updateBrand(@RequestBody Brand brand) {
        return brandService.updateBrand(brand);
    }

    // 3.删除品牌
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteBrand(@PathVariable Long id) {
        boolean result = brandService.removeById(id);
        return Result.ok(result);
    }

    // 4.查看品牌信息
    @GetMapping("/{id}")
    public Result<BrandVo> getBrand(@PathVariable Long id) {
        Brand brand = brandService.getById(id);
        if (brand != null) {
            BrandVo vo = new BrandVo(brand);
            return Result.ok(vo);
        }
        return Result.fail("品牌不存在");
    }

    // 5.根据品牌名称查询
    @GetMapping("/name/{name}")
    public Result<BrandVo> getBrandByName(@PathVariable String name) {
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Brand::getName, name);
        Brand brand = brandService.getOne(wrapper);
        if (brand != null) {
            BrandVo vo = new BrandVo(brand);
            return Result.ok(vo);
        }
        return Result.fail("品牌不存在");
    }

    // 6.查询所有品牌
    @GetMapping("/list")
    public Result<List<BrandVo>> getBrandList() {
        List<Brand> brands = brandService.list();
        List<BrandVo> brandVos = new ArrayList<>();
        for (Brand brand : brands) {
            brandVos.add(new BrandVo(brand));
        }
        return Result.ok(brandVos);
    }

}