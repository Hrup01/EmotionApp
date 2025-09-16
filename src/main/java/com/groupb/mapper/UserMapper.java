package com.groupb.mapper;


import com.groupb.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select id,username,password,phone from `user` where username=#{username} and password=#{password}")
    User findUsernameAndPassword(User user);

    @Select("select id,username,password,phone from `user` where username=#{username} limit 1")
    User findByUsername(String username);

    @Update("update `user` set password=#{encodedPassword} where id=#{id}")
    int updatePasswordById(long id, String encodedPassword);
}
