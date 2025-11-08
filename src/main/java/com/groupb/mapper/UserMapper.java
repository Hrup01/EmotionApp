package com.groupb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupb.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT id, username, password, phone, avatar_url as avatarUrl, nickname, gender, birthday, points, " +
            "status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime " +
            "FROM users WHERE username = #{username} LIMIT 1")
    User findByUsername(String username);

    @Select("SELECT id, username, password, phone, avatar_url as avatarUrl, nickname, gender, birthday, points, " +
            "status, last_login_time as lastLoginTime, create_time as createTime, update_time as updateTime " +
            "FROM users WHERE id = #{id}")
    User findById(long id);

    @Insert("INSERT INTO users (username, password, phone, avatar_url, nickname, gender, birthday, points, " +
            "status, last_login_time, create_time, update_time) " +
            "VALUES (#{username}, #{password}, #{phone}, #{avatarUrl}, #{nickname}, #{gender}, #{birthday}, #{points}, " +
            "#{status}, #{lastLoginTime}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Update("UPDATE users SET last_login_time = #{lastLoginTime}, status = #{status}, update_time = #{updateTime} " +
            "WHERE id = #{id}")
    int updateUser(User user);

    @Update("UPDATE users SET password = #{encodedPassword}, update_time = #{updateTime} WHERE id = #{id}")
    int updatePasswordById(long id, String encodedPassword, LocalDateTime updateTime);

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);

    @Update("UPDATE users SET avatar_url = #{avatarUrl}, update_time = #{updateTime} WHERE id = #{id}")
    int updateAvatarUrl(long id, String avatarUrl, LocalDateTime updateTime);

    @Update("UPDATE users SET nickname = #{nickname}, update_time = #{updateTime} WHERE id = #{id}")
    int updateNickname(long id, String nickname, LocalDateTime updateTime);

    @Update("UPDATE users SET gender = #{gender}, update_time = #{updateTime} WHERE id = #{id}")
    int updateGender(long id, String gender, LocalDateTime updateTime);

    @Update("UPDATE users SET birthday = #{birthday}, update_time = #{updateTime} WHERE id = #{id}")
    int updateBirthday(long id, LocalDate birthday, LocalDateTime updateTime);

    @Update("UPDATE users SET " +
            "avatar_url = IFNULL(#{avatarUrl}, avatar_url), " +
            "nickname = IFNULL(#{nickname}, nickname), " +
            "gender = IFNULL(#{gender}, gender), " +
            "birthday = IFNULL(#{birthday}, birthday), " +
            "update_time = #{updateTime} WHERE id = #{id}")
    int updateProfile(long id, String avatarUrl, String nickname, String gender, LocalDate birthday, LocalDateTime updateTime);
}
