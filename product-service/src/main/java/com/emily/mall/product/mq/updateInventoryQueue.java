//package com.emily.mall.product.mq;
//
//import com.emily.mall.common.dto.InventoryUpdateDto;
//import com.emily.mall.product.entity.Product;
//import com.emily.mall.product.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class updateInventoryQueue {
//
//    private final ProductService productService;
//
//
//    @RabbitListener(queues="product-service-sync.queue")
//    public void updateInventory(InventoryUpdateDto inventoryUpdateDto){
//        try{
//            log.info("商品微服务收到库存微服务消息，开始更新商品库存");//商品显示的库存是available库存
//            Product product=productService.getById(inventoryUpdateDto.getProductId());
//            product.setStock(inventoryUpdateDto.getNewStock());
//            Boolean success=productService.updateById(product);
//            if(!success){
//                log.error("商品服务更新库存失败");
//            }
//            log.info("商品服务更新库存成功，最新库存:{}",product.getStock());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
