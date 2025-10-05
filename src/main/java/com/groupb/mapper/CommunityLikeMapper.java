package com.groupb.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface CommunityLikeMapper {

    @Insert("INSERT IGNORE INTO community_like(post_id, user_id) VALUES(#{postId}, #{userId})")
    int like(@Param("postId") Long postId, @Param("userId") Long userId);

    @Delete("DELETE FROM community_like WHERE post_id=#{postId} AND user_id=#{userId}")
    int unlike(@Param("postId") Long postId, @Param("userId") Long userId);

    @Select("SELECT COUNT(1) FROM community_like WHERE post_id=#{postId}")
    int countLikes(Long postId);

    @Select("SELECT 1 FROM community_like WHERE post_id=#{postId} AND user_id=#{userId} LIMIT 1")
    Integer liked(@Param("postId") Long postId, @Param("userId") Long userId);
}


