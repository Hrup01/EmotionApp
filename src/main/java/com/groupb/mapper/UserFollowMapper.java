package com.groupb.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFollowMapper {

    @Insert("INSERT IGNORE INTO user_follow(user_id, target_user_id) VALUES(#{userId}, #{targetUserId})")
    int follow(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);

    @Delete("DELETE FROM user_follow WHERE user_id=#{userId} AND target_user_id=#{targetUserId}")
    int unfollow(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);

    @Select("SELECT target_user_id FROM user_follow WHERE user_id=#{userId}")
    List<Long> listFollowings(Long userId);
}


