//package com.emily.mall.order.config;
//
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ReturnedMessage;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
////对fanout不起作用，因为fanout没有路由机制，群发给所有队列
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class MQConfig {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    @PostConstruct
//    public void init(){
//        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback(){
//            @Override
//            public void returnedMessage(ReturnedMessage returned){
//                log.error("触发return callback，消息发送到exchange但没有路由到queue");
//                log.debug("exchange:{}",returned.getExchange());
//                log.debug("routingKey:{}",returned.getRoutingKey());
//                log.debug("message:{}",returned.getMessage());
//                log.debug("replyCode:{}",returned.getReplyCode());
//                log.debug("replyText:{}",returned.getReplyText());
//            }
//        });
//    }
//}
//
