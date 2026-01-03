package com.emily.mall.common.utils;

import com.emily.mall.common.UserContext.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

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
        // 异常场景/无登录用户时，返回系统默认用户ID（测试的时候可以使用，生产环境要判断0L为未登陆用户）
        return SYSTEM_USER_ID;
    }

//订单流水号/支付流水号生成函数
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final int RANDOM_DIGITS = 6; // 随机数的位数，可根据需要调整


    public static String generateSerialNo() {
        String timePart = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        //生成一个 0 到 (10^RANDOM_DIGITS - 1) 之间的随机整数。
        int randomPart = ThreadLocalRandom.current().nextInt((int) Math.pow(10, RANDOM_DIGITS));
        // 补零，确保固定位数，下面的代码是%06d的意思，即不够6位就补足0
        String randomStr = String.format("%0" + RANDOM_DIGITS + "d", randomPart);
        return  timePart + randomStr;
    }

}
