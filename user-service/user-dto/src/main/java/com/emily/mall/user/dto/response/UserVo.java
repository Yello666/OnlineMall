package com.emily.mall.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UserVo {
    private String username;
    private String id;
    private BigDecimal balance;
    private String role;
    private String email;
    private String phone;
    private Integer gender;
    private String avatar;

    // Remove constructor that depends on User entity to avoid circular dependency
    //引用User会导致循坏依赖，可以使用BeanUtils的字段映射器来解决
    // public UserVo(User u){...}


}
