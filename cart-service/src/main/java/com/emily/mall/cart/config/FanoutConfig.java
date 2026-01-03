package com.emily.mall.cart.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {
    //fanoutExchange
    @Bean
    public FanoutExchange orderFanoutExchange(){
        return new FanoutExchange("mall.order.fanout");
    }

    //声明一个队列（清空购物车消息的存放处）
    @Bean
    public Queue clearCartQueue(){
        return new Queue("cart.clear");
    }
    //绑定队列和交换机
    @Bean
    public Binding bindingQueue1(Queue clearCartQueue,FanoutExchange orderFanoutExchange){
        return BindingBuilder.bind(clearCartQueue).to(orderFanoutExchange);
    }


}
