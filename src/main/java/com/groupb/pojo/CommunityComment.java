package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 社区帖子评论实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityComment {
    private Long id;//id
    private Long postId;//回复id
    private Long authorId;//评论人id
    private String content;//评论内容
    private Long replyToCommentId;//回复的评论id
    private Integer status;//状态
    private LocalDateTime createdAt;//创建时间
}


