package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class User {
    private long id;//id(主键)
    private String phone;//手机号
    private String username;//用户名
    private String password;//密码
    private String avatarUrl;//头像url
    private String nickname;//昵称
    private String gender;//性别（MALE/FEMALE/OTHER）
    private LocalDate birthday;//生日
    private Integer points = 0;//积分
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
