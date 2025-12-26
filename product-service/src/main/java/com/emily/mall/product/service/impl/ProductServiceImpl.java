package com.emily.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.UserContext.UserContextHolder;
import com.emily.mall.common.feign.CartClient;
import com.emily.mall.common.feign.InventoryClient;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.dto.*;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.entity.Category;
import com.emily.mall.product.entity.Product;
import com.emily.mall.product.mapper.CategoryMapper;
import com.emily.mall.product.mapper.ProductMapper;
import com.emily.mall.product.service.BrandService;
import com.emily.mall.product.service.CategoryService;
import com.emily.mall.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private InventoryClient inventoryClient;

    /**
     * 9.创建商品(需要查看product中的brandId，CategoryId是否存在)
     */
    @Override
    public Result<Category> createProduct(Product product) {
        // 检查品牌ID是否存在
        if (product.getBrandId() != null) {
            Brand brand = brandService.getById(product.getBrandId());
            if (brand == null) {
                return Result.fail("品牌不存在");
            }
        }
        // 检查分类ID是否存在
        if (product.getCategoryId() != null) {
            Category category = categoryService.getById(product.getCategoryId());
            if (category == null) {
                return Result.fail("商品规格不存在");
            }
        }
        // 创建商品
        int result = productMapper.insert(product);
        return result > 0 ? Result.ok() : Result.fail("创建商品失败");
    }

    /**
     * 10.顾客视角查看商品列表Page
     * 输入：商品的关键词
     * 输出：商品ID、商品code、商品名、商品品牌名、商品图片（1张）、商品销量、商品原价、商品现价、商品描述（前20字）
     */
    public Result<Page<ClientListProductVo>> getClientProductList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询上架的商品
        wrapper.eq(Product::getStatus, 1);
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword)
                   .or().like(Product::getCode, keyword)
                   .or().like(Product::getDescription, keyword);
        }
        
        // 按权重和创建时间排序
        wrapper.orderByAsc(Product::getSort).orderByDesc(Product::getCreateTime);
        
        Page<Product> productPage = productMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<ClientListProductVo> voPage = new Page<>(pageNum, pageSize, productPage.getTotal());
        List<ClientListProductVo> voList = new ArrayList<>();
        
        for (Product product : productPage.getRecords()) {
            ClientListProductVo vo = new ClientListProductVo();
            vo.setId(product.getId().toString());
            vo.setCode(product.getCode());
            vo.setName(product.getName());
            vo.setImage(product.getImage());
            vo.setSales(product.getSales());
            
            // 获取品牌名
            if (product.getBrandId() != null) {
                Brand brand = brandService.getById(product.getBrandId());
                if (brand != null) {
                    vo.setBrandId(brand.getId().toString());
                    vo.setBrandName(brand.getName());
                }
            }
            
            // 查找该code下所有规格的最低价格
            LambdaQueryWrapper<Product> priceWrapper = new LambdaQueryWrapper<>();
            priceWrapper.eq(Product::getCode, product.getCode())
                       .eq(Product::getStatus, 1);
            List<Product> sameCodeProducts = productMapper.selectList(priceWrapper);
            
            BigDecimal minPrice = sameCodeProducts.stream()
                .map(Product::getPrice)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(product.getPrice());
                
            BigDecimal minOriginalPrice = sameCodeProducts.stream()
                .map(Product::getOriginalPrice)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(product.getOriginalPrice());
            
            vo.setPrice(minPrice);
            vo.setOriginalPrice(minOriginalPrice);
            
            // 描述前20个字符
            if (StringUtils.hasText(product.getDescription())) {
                String desc = product.getDescription();
                vo.setDescription(desc.length() > 20 ? desc.substring(0, 20) : desc);
            }
            
            voList.add(vo);
        }
        
        voPage.setRecords(voList);
        return Result.ok(voPage);
    }

    /**
     * 11.商家视角查看自己的商品列表Page
     * 从 ThreadLocal 中取出 UserId，搜索 CreateBy 的 Id 是 UserId 的商品
     */
    public Result<Page<ClientListProductVo>> getMerchantProductList(Integer pageNum, Integer pageSize, String keyword) {
        // 从 ThreadLocal 获取用户ID
        String userIdStr = UserContextHolder.getUserId();
        if (userIdStr == null) {
            return Result.fail("无法获取用户信息");
        }
        Long userId = Long.valueOf(userIdStr);
        
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询当前用户创建的商品
        wrapper.eq(Product::getCreateBy, userId);
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Product::getName, keyword)
                   .or().like(Product::getCode, keyword)
                   .or().like(Product::getDescription, keyword));
        }
        
        wrapper.orderByAsc(Product::getSort).orderByDesc(Product::getCreateTime);
        
        Page<Product> productPage = productMapper.selectPage(page, wrapper);
        
        // 转换为VO（复用顾客视角的VO结构）
        Page<ClientListProductVo> voPage = new Page<>(pageNum, pageSize, productPage.getTotal());
        List<ClientListProductVo> voList = new ArrayList<>();
        
        for (Product product : productPage.getRecords()) {
            ClientListProductVo vo = new ClientListProductVo();
            vo.setId(product.getId().toString());
            vo.setCode(product.getCode());
            vo.setName(product.getName());
            vo.setImage(product.getImage());
            vo.setSales(product.getSales());
            
            if (product.getBrandId() != null) {
                Brand brand = brandService.getById(product.getBrandId());
                if (brand != null) {
                    vo.setBrandId(brand.getId().toString());
                    vo.setBrandName(brand.getName());
                }
            }
            
            // 查找该code下所有规格的最低价格
            LambdaQueryWrapper<Product> priceWrapper = new LambdaQueryWrapper<>();
            priceWrapper.eq(Product::getCode, product.getCode());
            List<Product> sameCodeProducts = productMapper.selectList(priceWrapper);
            
            BigDecimal minPrice = sameCodeProducts.stream()
                .map(Product::getPrice)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(product.getPrice());
                
            BigDecimal minOriginalPrice = sameCodeProducts.stream()
                .map(Product::getOriginalPrice)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(product.getOriginalPrice());
            
            vo.setPrice(minPrice);
            vo.setOriginalPrice(minOriginalPrice);
            
            if (StringUtils.hasText(product.getDescription())) {
                String desc = product.getDescription();
                vo.setDescription(desc.length() > 20 ? desc.substring(0, 20) : desc);
            }
            
            voList.add(vo);
        }
        
        voPage.setRecords(voList);
        return Result.ok(voPage);
    }

    /**
     * 12.顾客视角查看详细商品信息
     * 输入:商品code
     * 输出：商品名、商品详细描述、图片集合、商品销量、商品原价(最低价)、商品现价(最低价)
     */
    @Override
    public Result<ClientDetailProductVo> getProductDetailInfo(Product product) {
        if (product == null || !StringUtils.hasText(product.getCode())) {
            return Result.fail("商品code不能为空");
        }
        
        // 查询该code下的所有商品（不同规格）
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, product.getCode())
               .eq(Product::getStatus, 1);
        List<Product> products = productMapper.selectList(wrapper);
        
        if (products == null || products.isEmpty()) {
            return Result.fail("商品不存在");
        }
        
        // 取第一个作为基础信息
        Product firstProduct = products.get(0);
        
        ClientDetailProductVo vo = new ClientDetailProductVo();
        vo.setCode(firstProduct.getCode());
        vo.setName(firstProduct.getName());
        vo.setDescription(firstProduct.getDescription());
        vo.setImages(firstProduct.getImages());
        
        // 查找最低价格
        BigDecimal minPrice = products.stream()
            .map(Product::getPrice)
            .filter(price -> price != null)
            .min(BigDecimal::compareTo)
            .orElse(null);
            
        BigDecimal minOriginalPrice = products.stream()
            .map(Product::getOriginalPrice)
            .filter(price -> price != null)
            .min(BigDecimal::compareTo)
            .orElse(null);
        
        vo.setPrice(minPrice);
        vo.setOriginalPrice(minOriginalPrice);
        
        return Result.ok(vo);
    }

    /**
     * 13.顾客视角点击添加购物车页面时，查看的商品规格列表信息
     * 输入：商品code+规格ID
     * 输出：多个商品对应的规格以及其原价和现价
     */
    @Override
    public Result<ClientCategoryProductVo> getProductCategoryInfo(String code, Long categoryId) {
        if (!StringUtils.hasText(code)) {
            return Result.fail("商品code不能为空");
        }
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, code)
               .eq(Product::getStatus, 1);
        
        // 如果指定了规格ID，只查询该规格
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        
        List<Product> products = productMapper.selectList(wrapper);
        
        if (products == null || products.isEmpty()) {
            return Result.fail("商品不存在");
        }
        
        ClientCategoryProductVo vo = new ClientCategoryProductVo();
        vo.setCode(code);
        vo.setName(products.get(0).getName());
        
        // 构建规格列表
        List<CategoryVo> categoryVos = new ArrayList<>();
        for (Product p : products) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(p.getId().toString());
            categoryVo.setCategoryId(p.getCategoryId().toString());
            
            // 获取规格名称
            if (p.getCategoryId() != null) {
                Category category = categoryService.getById(p.getCategoryId());
                if (category != null) {
                    categoryVo.setCategoryName(category.getName());
                    categoryVo.setImage(category.getImage());
                }
            }
            
            categoryVo.setPrice(p.getPrice());
            categoryVo.setOriginalPrice(p.getOriginalPrice());
            categoryVo.setCreateTime(p.getCreateTime());
            categoryVo.setUpdateTime(p.getUpdateTime());
            
            categoryVos.add(categoryVo);
        }
        
        vo.setCategoryVos(categoryVos);
        return Result.ok(vo);
    }

    /**
     * 14.商家视角查看自己的商品详细信息
     * 从 ThreadLocal 中取出 UserId，搜索 CreateBy 的 Id 是 UserId 的商品
     * 输出：商品的全部信息，+品牌名+所有规格名
     */
    @Override
    public Result<MerchantDetailProductVo> merchantGetProductDetailInfo(Long userid) {
        // 从 ThreadLocal 获取用户ID
        String userIdStr = UserContextHolder.getUserId();
        if (userIdStr == null) {
            return Result.fail("无法获取用户信息");
        }
        Long userId = Long.valueOf(userIdStr);
        
        // 查询该用户创建的商品（使用传入的userid作为商品ID查询）
        Product product = productMapper.selectById(userid);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        
        // 验证是否是该用户创建的
        if (!userId.equals(product.getCreateBy())) {
            return Result.fail("无权查看该商品");
        }
        
        // 查询该code下的所有商品（不同规格）
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, product.getCode())
               .eq(Product::getCreateBy, userId);
        List<Product> products = productMapper.selectList(wrapper);
        
        MerchantDetailProductVo vo = new MerchantDetailProductVo();
        vo.setCode(product.getCode());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setImages(product.getImages());
        
        // 构建规格列表
        List<CategoryVo> categoryVos = new ArrayList<>();
        for (Product p : products) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(p.getId().toString());
            categoryVo.setCategoryId(p.getCategoryId().toString());
            
            if (p.getCategoryId() != null) {
                Category category = categoryMapper.selectById(p.getCategoryId());
                if (category != null) {
                    categoryVo.setCategoryName(category.getName());
                    categoryVo.setImage(category.getImage());
                }
            }
            
            categoryVo.setPrice(p.getPrice());
            categoryVo.setOriginalPrice(p.getOriginalPrice());
            categoryVo.setCreateTime(p.getCreateTime());
            categoryVo.setUpdateTime(p.getUpdateTime());
            
            categoryVos.add(categoryVo);
        }
        
        vo.setCategoryVos(categoryVos);
        return Result.ok(vo);
    }

    /**
     * 15.管理员查看商品的详细信息
     * 输入商品code
     * 输出：商品的全部信息，+品牌名+所有规格名
     */
    @Override
    public Result<AdminDetailProductVo> adminGetProductDetailInfo(String code) {
        if (!StringUtils.hasText(code)) {
            return Result.fail("商品code不能为空");
        }
        
        // 查询该code下的所有商品（不同规格）
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCode, code);
        List<Product> products = productMapper.selectList(wrapper);
        
        if (products == null || products.isEmpty()) {
            return Result.fail("商品不存在");
        }
        
        Product firstProduct = products.get(0);
        
        AdminDetailProductVo vo = new AdminDetailProductVo();
        vo.setCode(firstProduct.getCode());
        vo.setName(firstProduct.getName());
        vo.setDescription(firstProduct.getDescription());
        vo.setImages(firstProduct.getImages());
        vo.setCreateBy(firstProduct.getCreateBy());
        vo.setUpdateBy(firstProduct.getUpdateBy());
        
        // 构建规格列表
        List<CategoryVo> categoryVos = new ArrayList<>();
        for (Product p : products) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(p.getId().toString());
            categoryVo.setCategoryId(p.getCategoryId().toString());
            
            if (p.getCategoryId() != null) {
                Category category = categoryMapper.selectById(p.getCategoryId());
                if (category != null) {
                    categoryVo.setCategoryName(category.getName());
                    categoryVo.setImage(category.getImage());
                }
            }
            
            categoryVo.setPrice(p.getPrice());
            categoryVo.setOriginalPrice(p.getOriginalPrice());
            categoryVo.setCreateTime(p.getCreateTime());
            categoryVo.setUpdateTime(p.getUpdateTime());
            
            categoryVos.add(categoryVo);
        }
        
        vo.setCategoryVos(categoryVos);
        return Result.ok(vo);
    }

    /**
     * 16.下架商品(将商品的status设置为0) - 根据ID
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
     * 16.下架商品 - 根据名称
     */
    @Override
    public Result<Boolean> removeProductByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getName, name);
        List<Product> products = productMapper.selectList(wrapper);
        
        if (products == null || products.isEmpty()) {
            return Result.fail("商品不存在");
        }
        
        // 批量下架
        for (Product product : products) {
            product.setStatus(0);
            productMapper.updateById(product);
        }
        
        return Result.ok(true);
    }

    /**
     * 17.批量下架商品
     */
    public Result<Boolean> batchRemoveProducts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.fail("商品ID列表不能为空");
        }
        
        List<Product> products = productMapper.selectBatchIds(ids);
        for (Product product : products) {
            product.setStatus(0);
        }
        
        boolean result = this.updateBatchById(products);
        return result ? Result.ok(true) : Result.fail("批量下架商品失败");
    }

    /**
     * 18.删除商品 - 根据ID
     */
    @Override
    public Result<Boolean> deleteProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        int result = productMapper.deleteById(id);
        return result > 0 ? Result.ok(true) : Result.fail("删除商品失败");
    }

    /**
     * 18.删除商品 - 根据名称
     */
    @Override
    public Result<Boolean> deleteProductByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getName, name);
        int result = productMapper.delete(wrapper);
        return result > 0 ? Result.ok(true) : Result.fail("删除商品失败");
    }

    /**
     * 19.批量删除商品
     */
    public Result<Boolean> batchDeleteProducts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.fail("商品ID列表不能为空");
        }
        
        int result = productMapper.deleteBatchIds(ids);
        return result > 0 ? Result.ok(true) : Result.fail("批量删除商品失败");
    }

    /**
     * 20.将商品加入购物车（调用购物车微服务）
     */
    @Override
    public Result<Boolean> addProductToCart(Product product) {
        if (product == null || product.getId() == null) {
            return Result.fail("商品信息不完整");
        }
        
        // 从 ThreadLocal 获取用户ID
        String userIdStr = UserContextHolder.getUserId();
        if (userIdStr == null) {
            return Result.fail("用户未登录");
        }
        Long userId = Long.valueOf(userIdStr);
        
        // 检查商品是否存在且上架
        Product existProduct = productMapper.selectById(product.getId());
        if (existProduct == null) {
            return Result.fail("商品不存在");
        }
        if (existProduct.getStatus() != 1) {
            return Result.fail("商品已下架");
        }
        
        // 调用购物车微服务
        try {
            Integer quantity = product.getStock() != null && product.getStock() > 0 ? product.getStock() : 1;
            Result<Boolean> result = cartClient.addCartItem(userId, product.getId(), quantity);
            return result;
        } catch (Exception e) {
            return Result.fail("调用购物车服务失败: " + e.getMessage());
        }
    }

    /**
     * 21.修改商品库存（调用库存微服务）
     */
    @Override
    public Result<Boolean> changeProductInventory(Long id, Integer newStock) {
        if (id == null || newStock == null) {
            return Result.fail("参数不完整");
        }
        
        // 检查商品是否存在
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        
        // 调用库存微服务
        try {
            Result<Boolean> result = inventoryClient.updateInventory(id, newStock);
            return result;
        } catch (Exception e) {
            return Result.fail("调用库存服务失败: " + e.getMessage());
        }
    }

    @Override
    public Page<Product> getProductPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId, Integer status) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
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
