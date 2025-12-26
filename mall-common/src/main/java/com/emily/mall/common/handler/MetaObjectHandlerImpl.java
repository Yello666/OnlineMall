package com.emily.mall.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.emily.mall.common.UserContext.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

import static com.emily.mall.common.UserContext.UserContextHolder.getUserId;

/**
 * MyBatis Plus字段自动填充处理器
 */
@Component
@Slf4j
public class MetaObjectHandlerImpl implements MetaObjectHandler {
    // 系统默认用户ID：代表“系统操作”（无登录用户时使用）
    private static final Long SYSTEM_USER_ID = 0L;
    // 字段名常量（避免硬编码，后续改字段名只需改这里）
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_BY = "updateBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);//mybatis-plus自动维护
        // 插入时，自动填充 createBy 和 updateBy 为当前登录用户ID
        Long currentUserId = getCurrentUserIdSafely();//安全获取操作用户id，如果用户登录了，就是登录用户的id，没有登录就是系统id
        strictInsertFill(metaObject, CREATE_BY, Long.class,currentUserId);
        strictInsertFill(metaObject, UPDATE_BY, Long.class, currentUserId);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        Long currentUserId = getCurrentUserIdSafely();
        strictUpdateFill(metaObject, UPDATE_BY, Long.class, currentUserId);

    }

    /**
     * 安全获取当前登录用户ID：处理空值、类型转换异常，默认返回系统用户ID
     */
    private Long getCurrentUserIdSafely() {
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
