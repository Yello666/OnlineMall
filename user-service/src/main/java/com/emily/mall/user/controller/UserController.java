package com.emily.mall.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.user.entity.User;
import com.emily.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Boolean> createUser(@RequestBody User user) {
        boolean success = userService.save(user);
        return success ? Result.ok(success) : Result.fail("创建用户失败");
    }

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

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return user != null ? Result.ok(user) : Result.fail("用户不存在");
    }

    /**
     * 批量查询用户
     */
    @GetMapping("/batch")
    public Result<List<User>> getUserBatch(@RequestParam List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return Result.ok(users);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        List<User> users = userService.list();
        return Result.ok(users);
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<Page<User>> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<User> page = userService.getUserPage(pageNum, pageSize, keyword);
        return Result.ok(page);
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return user != null ? Result.ok(user) : Result.fail("用户不存在");
    }
}
