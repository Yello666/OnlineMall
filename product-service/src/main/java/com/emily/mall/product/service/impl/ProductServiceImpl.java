package com.emily.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.feign.InventoryClient;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.mapper.ProductMapper;
import com.emily.mall.product.service.BrandService;
import com.emily.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final InventoryClient inventoryClient;

    /**
     * 1.创建商品(需要查看product中的brandId是否存在)
     */
    @Override
    public Result<Product> createProduct(Product product) {
        // 检查品牌ID是否存在
        if (product.getBrandId() != null) {
            Brand brand = brandService.getById(product.getBrandId());
            if (brand == null) {
                return Result.fail("品牌不存在");
            }
        }
        
        // 检查商品码是否已存在
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(Product::getCode, product.getCode());
        List<Product> existingProducts = productMapper.selectList(productWrapper);
        if (!existingProducts.isEmpty()) {
            return Result.fail("商品码已存在");
        }
        
        // 创建商品
        int result = productMapper.insert(product);
        if(result<=0){
            return Result.fail("创建商品失败");
        }
        
        return Result.ok("创建商品成功",product);

    }

    /**
     * 2.根据商品码删除商品
     */
    @Override
    public Result<Boolean> deleteProductByCode(String code) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, code);
        Product product=productMapper.selectOne(wrapper);
        int result = productMapper.delete(wrapper);
        //删除库存
        inventoryClient.deleteInventoryByProductId(product.getId());
        return result > 0 ? Result.ok(true) : Result.fail("删除商品失败");
    }

    /**
     * 3.根据ID删除商品
     */
    @Override
    public Result<Boolean> deleteProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        int result = productMapper.deleteById(id);
        //删除库存
        inventoryClient.deleteInventoryByProductId(id);
        return result > 0 ? Result.ok(true) : Result.fail("删除商品失败");
    }

    @Override
    public Product getProductByCode(String code) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, code);
        List<Product> list = this.list(wrapper);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }
    /**
     * 4.下架商品(将商品的status设置为0) - 根据ID
     */
    @Override
    public Result<Boolean> removeProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        product.setStatus(0);
        int result = productMapper.updateById(product);
        return result > 0 ? Result.ok(true) : Result.fail("下架商品失败");
    }

    /**
     * 5. 商品列表分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页条数
     * @return 分页商品数据
     */
    @Override
    public Result<Page<Product>> getProductPageList(Long pageNum, Long pageSize) {
        // 1. 校验分页参数
        if (pageNum == null || pageNum < 1) {
            pageNum = 1L; // 默认第一页
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10L; // 默认每页10条，最大限制100条
        }

        // 2. 构建分页对象
        Page<Product> productPage = new Page<>(pageNum, pageSize);

        // 3. 执行分页查询（可根据需求添加查询条件，此处默认查询所有商品）
        // 如需按条件查询（如按品牌、状态），可添加LambdaQueryWrapper参数
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 示例：只查询上架商品（status=1），如需查询所有可删除此行
        wrapper.eq(Product::getStatus, 1);
        // 按创建时间倒序排序
        wrapper.orderByDesc(Product::getCreateTime);

        Page<Product> resultPage = productMapper.selectPage(productPage, wrapper);

        // 4. 返回分页结果
        return Result.ok("商品分页查询成功", resultPage);
    }





}
