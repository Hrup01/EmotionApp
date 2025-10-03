package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 发布帖子实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPost {
    private Long id;//id
    private Long authorId;//发帖人id
    private String content;//帖子内容
    private String imagesJson;//图片json
    private Integer likeCount;//点赞数
    private Integer commentCount;//评论数
    private Integer status;//状态
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}


