package com.groupb.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 登录数据传输对象
 * 封装账号密码登录所需的信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private long id;
    private String username;
    private String password;
    private String token;

}
