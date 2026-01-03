package com.emily.mall.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.emily.mall.common.result.Result;
import com.emily.mall.user.dto.LoginByUserNameRequest;
import com.emily.mall.user.dto.RegisterRequest;
import com.emily.mall.user.dto.LoginByUserNameResponse;
import com.emily.mall.user.entity.User;

import java.math.BigDecimal;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    //用户余额支付
    Result<Boolean> payByUserBalance(BigDecimal amount,Long userId);

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
