package com.emily.mall.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginByUserNameRequest {
    private String username;
    private String password;

}
