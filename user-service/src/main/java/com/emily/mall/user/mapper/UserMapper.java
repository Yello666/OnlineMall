package com.emily.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emily.mall.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
