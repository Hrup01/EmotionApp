package com.groupb.pojo.dto;


import lombok.Data;

/**
 * 用户数据传输对象
 * 用于在系统中传输用户相关数据
 */

@Data
public class UserDTO {

    private String phone;

    private String username;

    private String password;



    private String avatarUrl;
}
