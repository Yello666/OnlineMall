package com.emily.mall.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.user.dto.request.LoginByUserNameRequest;
import com.emily.mall.user.dto.request.RegisterRequest;
import com.emily.mall.user.dto.response.LoginByUserNameResponse;
import com.emily.mall.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    //使用用户名密码登陆
    LoginByUserNameResponse loginByUserName(LoginByUserNameRequest request);
    //用户注册
    User userRegister(RegisterRequest request);

    /**
     * 分页查询用户
     */
    Page<User> getUserPage(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);


}
