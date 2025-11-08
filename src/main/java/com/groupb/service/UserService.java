package com.groupb.service;

import com.groupb.pojo.User;
import com.groupb.pojo.dto.LoginDTO;

import java.time.LocalDate;

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

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(long id);

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 是否更新成功
     */
    boolean updateAvatar(long userId, String avatarUrl);

    /**
     * 更新用户昵称
     * @param userId 用户ID
     * @param nickname 昵称
     * @return 是否更新成功
     */
    boolean updateNickname(long userId, String nickname);

    /**
     * 更新用户性别
     * @param userId 用户ID
     * @param gender 性别（MALE/FEMALE/OTHER）
     * @return 是否更新成功
     */
    boolean updateGender(long userId, String gender);

    /**
     * 更新用户生日
     * @param userId 用户ID
     * @param birthday 生日
     * @return 是否更新成功
     */
    boolean updateBirthday(long userId, LocalDate birthday);

    /**
     * 更新用户个人资料
     * @param userId 用户ID
     * @param avatarUrl 头像URL（可选）
     * @param nickname 昵称（可选）
     * @param gender 性别（可选）
     * @param birthday 生日（可选）
     * @return 是否更新成功
     */
    boolean updateProfile(long userId, String avatarUrl, String nickname, String gender, LocalDate birthday);
}
