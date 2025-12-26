package com.emily.mall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.result.Result;
import com.emily.mall.product.dto.AdminDetailProductVo;
import com.emily.mall.product.dto.ClientCategoryProductVo;
import com.emily.mall.product.dto.ClientDetailProductVo;
import com.emily.mall.product.dto.ClientListProductVo;
import com.emily.mall.product.dto.MerchantDetailProductVo;
import com.emily.mall.product.entity.Brand;
import com.emily.mall.product.entity.Category;
import com.emily.mall.product.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {

    //9.创建商品(需要查看product中的brandId，CategoryId是否存在)
    Result<Category> createProduct(Product product);
    
    //10.顾客视角查看商品列表Page（输入：商品的关键词，输出：商品ID，商品code，商品名，商品品牌名，商品图片（1张），商品销量，商品原价、商品现价、商品描述（前20字）组合在ClientListProductVo结构体里面）
    Result<Page<ClientListProductVo>> getClientProductList(Integer pageNum, Integer pageSize, String keyword);
    
    //11.商家视角查看自己的商品列表Page（从 ThreadLocal 中取出 UserId，搜索 CreateBy 的 Id 是 UserId 的商品，显示顾客视角查看商品列表Page一样的信息）
    Result<Page<ClientListProductVo>> getMerchantProductList(Integer pageNum, Integer pageSize, String keyword);


    //12.顾客视角查看详细商品信息（输入:商品code，输出：商品名，商品详细描述，图片集合，商品销量，
    // 商品原价(有多个规格则显示规格中的最低价)，商品现价（有多个规格则显示规格中的最低价），组合在vo结构体里面）
    Result<ClientDetailProductVo> getProductDetailInfo(Product product);

    //13.顾客视角点击添加购物车页面时，查看的商品规格列表信息(输入：商品code+规格ID）输出多个商品对应的规格以及其原价和现价
    Result<ClientCategoryProductVo> getProductCategoryInfo(String code,Long categoryId);

    //14.商家视角查看自己的商品详细信息（从ThreadLocal中取出UserId，搜索CreateBy的Id是UserId的商品，输出：商品的全部信息，+品牌名+所有规格名）
    Result<MerchantDetailProductVo> merchantGetProductDetailInfo(Long userid);

    //15.管理员查看商品的详细信息（输入商品code，输出输出：商品的全部信息，+品牌名+所有规格名）
    Result<AdminDetailProductVo> adminGetProductDetailInfo(String code);

    //16.下架商品(将商品的status设置为0），有两个接口
    Result<Boolean> removeProductById(Long id);

    Result<Boolean> removeProductByName(String name);

    //17.批量下架商品
    Result<Boolean> batchRemoveProducts(List<Long> ids);

    //18.删除商品，有两个接口
    Result<Boolean> deleteProductById(Long id);
    Result<Boolean> deleteProductByName(String name);

    //19.批量删除商品
    Result<Boolean> batchDeleteProducts(List<Long> ids);

    //20.将商品加入购物车（调用购物车微服务）
    Result<Boolean> addProductToCart(Product product);

    //21.修改商品库存（调用库存微服务）
    Result<Boolean> changeProductInventory(Long id,Integer newStock);

    // 其他辅助方法
    Page<Product> getProductPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId, Integer status);
    
    Product getProductByCode(String code);
}
