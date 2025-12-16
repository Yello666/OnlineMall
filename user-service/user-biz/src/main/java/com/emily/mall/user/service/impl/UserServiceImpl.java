package com.emily.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emily.mall.common.jwtUtils.jwtUtils;
import com.emily.mall.user.dto.request.LoginByUserNameRequest;
import com.emily.mall.user.dto.request.RegisterRequest;
import com.emily.mall.user.dto.response.LoginByUserNameResponse;
import com.emily.mall.user.entity.User;
import com.emily.mall.user.mapper.UserMapper;
import com.emily.mall.user.service.UserService;
import com.emily.mall.user.utils.EncodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final jwtUtils jwtUtils;

    //注册
    @Override
    public User userRegister(RegisterRequest request){
        User u=new User(request);
        //密码加密存储
        u.setPassword(EncodeUtils.Encode(u.getPassword()));
        //存储信息
        int res=userMapper.insert(u);
        //存储失败返回null
        if(res<=0){
            return null;
        } else{
            return u;//存储成功，返回user
        }

    }

    //使用用户名密码登陆
    @Override
    public LoginByUserNameResponse loginByUserName(LoginByUserNameRequest request){
        String username=request.getUsername();
        String password=request.getPassword();
        //1.获得数据库里的userName
        User u=getUserByUsername(username);

        if(!EncodeUtils.Match(password,u.getPassword())){
            log.info("用户名或密码错误");
            return null;
        }
        String token=jwtUtils.generateToken(u.getId(),u.getUsername(),u.getRole());
        LoginByUserNameResponse response=new LoginByUserNameResponse();
        response.setId(u.getId());
        response.setUsername(u.getUsername());
        response.setToken(token);
        response.setRole(u.getRole());
        return response;

    }

    @Override
    public Page<User> getUserPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                   .or()
                   .like(User::getPhone, keyword);
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
}
