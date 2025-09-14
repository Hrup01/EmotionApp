package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class user {
    private int id;//id(主键)
    private String username;//用户名
    private String password;//密码
    private String phone;//手机号
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private int status;//用户状态
}
