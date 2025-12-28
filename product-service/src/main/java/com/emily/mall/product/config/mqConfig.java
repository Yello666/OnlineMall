//package com.emily.mall.product.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class mqConfig {
//    //声明库存更新交换机
//    @Bean
//    public FanoutExchange inventoryUpdateExchange(){
//        return new FanoutExchange("mall.inventory.update");
//    }
//
//    //声明商品微服务同步队列
//    @Bean
//    public Queue productServiceSyncQueue(){
//        return new Queue("product-service-sync.queue");
//    }
//
//    //库存更新的时候，商品微服务同步队列收到消息
//    @Bean
//    public Binding bindingQueue1(Queue productServiceSyncQueue,FanoutExchange inventoryUpdateExchange){
//        return BindingBuilder.bind(productServiceSyncQueue).to(inventoryUpdateExchange);
//    }
//}
