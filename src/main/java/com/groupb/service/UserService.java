package com.groupb.service;


import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;

public interface UserService {

    /**
     * 用户使用账号密码登录
     * @param user
     * @return
     */

    LoginDTO login(User user);
}
