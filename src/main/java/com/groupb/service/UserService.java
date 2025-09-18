package com.groupb.service;

import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     * @param user 用户信息（用户名和密码）
     * @return 登录结果，包含JWT token
     */
    LoginDTO login(User user);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果，包含JWT token
     */
    LoginDTO register(User user);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);
}
