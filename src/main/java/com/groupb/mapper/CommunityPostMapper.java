package com.groupb.mapper;

import com.groupb.pojo.CommunityPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommunityPostMapper {

    @Insert("INSERT INTO community_post(author_id, content, images_json, like_count, comment_count, status) " +
            "VALUES(#{authorId}, #{content}, #{imagesJson}, #{likeCount}, #{commentCount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CommunityPost post);

    @Select("SELECT id, author_id as authorId, content, images_json as imagesJson, like_count as likeCount, comment_count as commentCount, status, created_at as createdAt, updated_at as updatedAt " +
            "FROM community_post WHERE id=#{id} AND status=1")
    CommunityPost findById(Long id);

    @Update("UPDATE community_post SET like_count = like_count + #{delta} WHERE id=#{postId}")
    int incrLikeCount(@Param("postId") Long postId, @Param("delta") int delta);

    @Update("UPDATE community_post SET comment_count = comment_count + #{delta} WHERE id=#{postId}")
    int incrCommentCount(@Param("postId") Long postId, @Param("delta") int delta);

    @Update("UPDATE community_post SET status=0 WHERE id=#{postId} AND author_id=#{userId}")
    int softDelete(@Param("postId") Long postId, @Param("userId") Long userId);

    @Select("SELECT id, author_id as authorId, content, images_json as imagesJson, like_count as likeCount, comment_count as commentCount, status, created_at as createdAt, updated_at as updatedAt " +
            "FROM community_post WHERE status=1 ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<CommunityPost> listRecommend(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT id, author_id as authorId, content, images_json as imagesJson, like_count as likeCount, comment_count as commentCount, status, created_at as createdAt, updated_at as updatedAt " +
            "FROM community_post WHERE status=1 AND author_id IN (${authorIds}) ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<CommunityPost> listFollowing(@Param("authorIds") String authorIdsCsv, @Param("offset") int offset, @Param("limit") int limit);
}


