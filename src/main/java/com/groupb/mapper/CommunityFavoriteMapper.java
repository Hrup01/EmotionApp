package com.groupb.mapper;

import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 社区收藏功能Mapper
 */
@Mapper
public interface CommunityFavoriteMapper {

    /**
     * 添加收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    @Insert("INSERT INTO community_favorite(user_id, post_id, created_at) VALUES(#{userId}, #{postId}, #{createdAt})")
    int insert(@Param("userId") Long userId, @Param("postId") Long postId, @Param("createdAt") LocalDateTime createdAt);

    /**
     * 删除收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    @Delete("DELETE FROM community_favorite WHERE user_id=#{userId} AND post_id=#{postId}")
    int delete(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏记录数
     */
    @Select("SELECT COUNT(*) FROM community_favorite WHERE user_id=#{userId} AND post_id=#{postId}")
    int countByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 获取用户的收藏列表
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 收藏的帖子ID列表
     */
    @Select("SELECT post_id FROM community_favorite WHERE user_id=#{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Long> findPostIdsByUser(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 获取帖子的收藏数量
     * @param postId 帖子ID
     * @return 收藏数量
     */
    @Select("SELECT COUNT(*) FROM community_favorite WHERE post_id=#{postId}")
    int countByPost(@Param("postId") Long postId);

    /**
     * 获取用户的收藏总数
     * @param userId 用户ID
     * @return 收藏总数
     */
    @Select("SELECT COUNT(*) FROM community_favorite WHERE user_id=#{userId}")
    int countByUser(@Param("userId") Long userId);
}
