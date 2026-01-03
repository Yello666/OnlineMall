package com.emily.mall.cart.MQListener;

import com.emily.mall.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.emily.mall.common.utils.utils.getCurrentUserIdSafely;

@Slf4j
@Component
@RequiredArgsConstructor
public class cartClearQueueListener {
    private final CartItemService cartItemService;

    @RabbitListener
    public void listenCartClearQueue(List<Long> productIds){
        log.info("购物车微服务收到清空购物车消息");
        Long userId=getCurrentUserIdSafely();
        Boolean success=cartItemService.minusByProductIds(userId,productIds);
        if(!success){
            log.error("清空购物车执行失败");
        }
    }
}
