package com.emily.mall.common.feign;

import com.emily.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// 2. 实现 FallbackFactory
@Component
@Slf4j
class UserFeignFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        // 匿名内部类实现降级逻辑
        return new UserClient() {
            @Override
            public Result<Boolean> payByUserBalance(BigDecimal amount,Long userId) {
                log.error("调用user-service失败，原因：", throwable);
                return Result.fail("用户服务调用失败，userId:" +userId+ "，原因：" + throwable.getMessage());
            }
 /*           @Override
            public Result<User> getUser(Long id) {
                // 打印异常（便于排查）+ 返回兜底数据
                log.error("调用user-service失败，原因：", throwable);
                return Result.fail("用户服务调用失败，id：" + id + "，原因：" + throwable.getMessage());
            }

            @Override
            public Result<List<User>> getUserBatch(List<Long> ids) {
                log.error("调用user-service失败，原因：", throwable);
                return Result.fail("用户服务调用失败，ids：" + ids + "，原因：" + throwable.getMessage());
            }

            @Override
            public Result<User> getUserByUsername(String username) {
                log.error("调用user-service失败，原因：", throwable);
                return Result.fail("用户服务调用失败，username：" + username + "，原因：" + throwable.getMessage());
            }*/


        };
    }
}
