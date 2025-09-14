package com.groupb.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;//id(主键)
    private String phone;//手机号
    private String username;//用户名
    private String password;//密码
    private String avatarUrl;//头像url
    private UserStatus status = UserStatus.ONLINE;//用户状态
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    /**
     * 用户状态枚举类
     */
    public enum UserStatus {
       ONLINE,//在线
       OFFLINE,//离线
       DISABLED,//禁用
       DELETED; //删除
    }

}
