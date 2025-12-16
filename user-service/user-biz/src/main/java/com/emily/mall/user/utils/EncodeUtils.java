package com.emily.mall.user.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//密码加密与比对的工具函数
public class EncodeUtils {
    public static String Encode(String str){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(str);
    }

    public static boolean Match(String rawStr,String encodedStr){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.matches(rawStr,encodedStr);
    }
}
