//package com.emily.mall.product.service.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.emily.mall.common.result.Result;
//import com.emily.mall.product.entity.Specification;
//import com.emily.mall.product.mapper.SpecificationMapper;
//import com.emily.mall.product.service.SpecificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {
//
//    private final SpecificationMapper specificationMapper;
//
//    @Override
//    public Result<Specification> createSpecification(Specification specification) {
//        int result = specificationMapper.insert(specification);
//        if (result <= 0) {
//            return Result.fail("创建规格失败");
//        }
//        return Result.ok("创建规格成功", specification);
//    }
//
//    @Override
//    public Result<Specification> updateSpecification(Specification specification) {
//        int result = specificationMapper.updateById(specification);
//        if (result <= 0) {
//            return Result.fail("更新规格失败");
//        }
//        return Result.ok("更新规格成功", specification);
//    }
//
//    @Override
//    public Result<Boolean> deleteSpecificationById(Long id) {
//        int result = specificationMapper.deleteById(id);
//        if (result <= 0) {
//            return Result.fail("删除规格失败");
//        }
//        return Result.ok(true);
//    }
//}