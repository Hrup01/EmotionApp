package com.groupb.mapper;

import com.groupb.pojo.UserCommunityBan;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserCommunityBanMapper {

    @Select("SELECT user_id as userId, reason, strike_count as strikeCount, banned_until as bannedUntil, updated_at as updatedAt FROM user_community_ban WHERE user_id=#{userId}")
    UserCommunityBan findByUserId(Long userId);

    @Insert("INSERT INTO user_community_ban(user_id, reason, strike_count, banned_until) VALUES(#{userId}, #{reason}, #{strikeCount}, #{bannedUntil})")
    int insert(UserCommunityBan record);

    @Update("UPDATE user_community_ban SET reason=COALESCE(#{reason}, reason), strike_count=#{strikeCount}, banned_until=#{bannedUntil} WHERE user_id=#{userId}")
    int update(UserCommunityBan record);

    @Update("INSERT INTO user_community_ban(user_id, reason, strike_count) VALUES(#{userId}, #{reason}, 1) ON DUPLICATE KEY UPDATE strike_count = strike_count + 1, reason = VALUES(reason)")
    int upsertAndIncrementStrike(@Param("userId") Long userId, @Param("reason") String reason);
}


