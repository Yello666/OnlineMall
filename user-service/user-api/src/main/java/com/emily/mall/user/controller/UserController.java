package com.emily.mall.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.user.dto.request.LoginByUserNameRequest;
import com.emily.mall.user.dto.request.RegisterRequest;
import com.emily.mall.user.dto.response.LoginByUserNameResponse;
import com.emily.mall.user.dto.response.UserVo;
import com.emily.mall.user.entity.User;
import com.emily.mall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //所有用户登陆
    @PostMapping("/login")
    public Result<LoginByUserNameResponse> loginByUserName(@RequestBody LoginByUserNameRequest request){
        log.info("用户登陆");
        LoginByUserNameResponse  response=userService.loginByUserName(request);
        if(response==null){
            return Result.fail("用户名或密码错误");
        } else{
            return Result.ok("登陆成功",response);
        }

    }
    //所有用户注册(普通用户、商家、管理员，在role里面填写)
    @PostMapping("/register")
    public Result<UserVo> userRegister(@RequestBody RegisterRequest request){
        if(request.getRole()==null ||request.getUsername()==null||request.getPassword()==null){
            return Result.fail("缺少必填字段");
        }
        User u=userService.userRegister(request);
        UserVo vo=new UserVo();


        if(u==null){
            return Result.fail("注册失败");
        } else{
            BeanUtils.copyProperties(u,vo);
            //这里vo的id是null，因为BeanUtils不支持将Long类型变成字符串类型，需要手动转为字符串
            vo.setId(String.valueOf(u.getId()));
            return Result.ok("注册成功",vo);
        }

    }

    //查看个人信息
    //根据用户id进行查询，前端传递字符串类型也可以完成自动转换
    @GetMapping("/id")
    public Result<UserVo> getUser(@RequestParam Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);//将名字一样的字段直接映射过去，避免循环依赖了
        userVo.setId(String.valueOf(user.getId()));
        return Result.ok(userVo);
    }

     //根据用户名查询用户
    @GetMapping("/username")
    public Result<UserVo> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setId(String.valueOf(user.getId()));
        return Result.ok(userVo);
    }

//    /**
//     * 批量查询用户
//     */
//    @GetMapping("/batch")
//    public Result<List<User>> getUserBatch(@RequestParam List<Long> ids) {
//        List<User> users = userService.listByIds(ids);
//        return Result.ok(users);
//    }

//    /**
//     * 查询所有用户
//     */
//    @GetMapping("/list")
//    public Result<List<User>> getUserList() {
//        List<User> users = userService.list();
//        return Result.ok(users);
//    }

//    /**
//     * 分页查询用户
//     */
//    @GetMapping("/page")
//    public Result<Page<User>> getUserPage(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(required = false) String keyword) {
//        Page<User> page = userService.getUserPage(pageNum, pageSize, keyword);
//        return Result.ok(page);
//    }







//    /**
//     * 创建用户
//     */
//    @PostMapping
//    public Result<Boolean> createUser(@RequestBody User user) {
//        boolean success = userService.save(user);
//        return success ? Result.ok(success) : Result.fail("创建用户失败");
//    }


    /**
     * 批量创建用户
     */
    @PostMapping("/batch")
    public Result<Boolean> createUserBatch(@RequestBody List<User> users) {
        boolean success = userService.saveBatch(users);
        return success ? Result.ok(success) : Result.fail("批量创建用户失败");
    }

    /**
     * 根据ID删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        return success ? Result.ok(success) : Result.fail("删除用户失败");
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public Result<Boolean> deleteUserBatch(@RequestBody List<Long> ids) {
        boolean success = userService.removeByIds(ids);
        return success ? Result.ok(success) : Result.fail("批量删除用户失败");
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<Boolean> updateUser(@RequestBody User user) {
        boolean success = userService.updateById(user);
        return success ? Result.ok(success) : Result.fail("更新用户失败");
    }

    /**
     * 批量更新用户
     */
    @PutMapping("/batch")
    public Result<Boolean> updateUserBatch(@RequestBody List<User> users) {
        boolean success = userService.updateBatchById(users);
        return success ? Result.ok(success) : Result.fail("批量更新用户失败");
    }

}
