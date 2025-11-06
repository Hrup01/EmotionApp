package com.groupb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupb.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT id, username, password, phone, avatar_url as avatarUrl, status, last_login_time as lastLoginTime, " +
            "create_time as createTime, update_time as updateTime FROM users WHERE username = #{username} LIMIT 1")
    User findByUsername(String username);

    @Insert("INSERT INTO users (username, password, phone, avatar_url, status, last_login_time, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{phone}, #{avatarUrl}, #{status}, #{lastLoginTime}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Update("UPDATE users SET last_login_time = #{lastLoginTime}, status = #{status}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    int updateUser(User user);

    @Update("UPDATE users SET password = #{encodedPassword}, update_time = #{updateTime} WHERE id = #{id}")
    int updatePasswordById(long id, String encodedPassword, LocalDateTime updateTime);

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);
}
