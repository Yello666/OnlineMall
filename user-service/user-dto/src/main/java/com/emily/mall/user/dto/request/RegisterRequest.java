package com.emily.mall.user.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    //角色
    private String role;

    /**
     * 邮箱 如何设置邮箱登陆？
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别(0:女 1:男 2:未知)
     */
    private Integer gender;

    /**
     * 头像URL
     */
    private String avatar;

}
