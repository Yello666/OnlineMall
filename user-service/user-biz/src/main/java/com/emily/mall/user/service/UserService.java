package com.emily.mall.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户
     */
    Page<User> getUserPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
}
