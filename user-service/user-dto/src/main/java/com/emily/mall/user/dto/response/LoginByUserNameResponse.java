package com.emily.mall.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginByUserNameResponse {
    private Long id;
    private String username;
    private String role;
    private String token;

}
