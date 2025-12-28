package com.emily.mall.common.utils;

import com.emily.mall.common.UserContext.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class utils {
    // 系统默认用户ID：代表“系统操作”（无登录用户时使用）
    public static final Long SYSTEM_USER_ID = 0L;
    /**
          * 安全获取当前登录用户ID：处理空值、类型转换异常，默认返回系统用户ID
          */

    public static Long getCurrentUserIdSafely() {
        try {
            String uidStr = UserContextHolder.getUserId();
            // 非空且是数字字符串，转成Long
            if (uidStr != null && uidStr.matches("\\d+")) {
                return Long.valueOf(uidStr);
            }
        } catch (Exception e) {
            // 捕获类型转换异常、UserContextHolder获取异常等，避免影响主流程
            log.warn("获取当前登录用户ID失败，使用系统默认用户ID", e);
        }
        // 异常场景/无登录用户时，返回系统默认用户ID
        return SYSTEM_USER_ID;
    }
}
