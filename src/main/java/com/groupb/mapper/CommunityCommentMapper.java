package com.groupb.mapper;

import com.groupb.pojo.CommunityComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommunityCommentMapper {

    @Insert("INSERT INTO community_comment(post_id, author_id, content, reply_to_comment_id, status) " +
            "VALUES(#{postId}, #{authorId}, #{content}, #{replyToCommentId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CommunityComment comment);

    @Select("SELECT id, post_id as postId, author_id as authorId, content, reply_to_comment_id as replyToCommentId, status, created_at as createdAt " +
            "FROM community_comment WHERE post_id=#{postId} AND status=1 ORDER BY created_at ASC LIMIT #{limit} OFFSET #{offset}")
    List<CommunityComment> listByPost(@Param("postId") Long postId, @Param("offset") int offset, @Param("limit") int limit);
}


