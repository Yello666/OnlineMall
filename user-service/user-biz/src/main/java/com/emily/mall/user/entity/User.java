package com.emily.mall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.emily.mall.common.entity.BaseEntity;
import com.emily.mall.user.dto.request.RegisterRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_user")
@NoArgsConstructor
public class User extends BaseEntity {
    //jdk对象序列化必须声明的序列化版本。用于序列化和反序列化。因为继承了BaseEntity
    private static final long serialVersionUID = 1L;

    //用户余额点
    private BigDecimal balance;

    /**
     * 用户ID
     */
    //IdType.ASSIGN_ID	雪花算法（Snowflake）生成主键（分布式环境首选）分布式系统、高并发场景，需要全局唯一 ID	无需依赖数据库，MyBatis-Plus 自动生成
    //IdType.ASSIGN_UUID	生成 UUID 作为主键（字符串类型）128 位长度的全局唯一字符串
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 邮箱 TODO如何设置邮箱登陆？
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

    /**
     * 状态(0:禁用 1:正常)
     */
    private Integer status;

    //删除标志（0-没有删除，1-已经删除）
    private Integer deleted;

    public User(RegisterRequest request){
        this.avatar=request.getAvatar();
        this.email=request.getEmail();
        this.gender=request.getGender();

        this.password=request.getPassword();
        this.phone=request.getPhone();
        this.username=request.getUsername();
        this.role=request.getRole();
        this.status=1;
        this.deleted=0;
        this.balance= BigDecimal.valueOf(1000);//默认初始余额为1000
    }
}
