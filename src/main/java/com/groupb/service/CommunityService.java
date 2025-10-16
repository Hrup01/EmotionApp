package com.groupb.service;

import com.groupb.pojo.dto.CommentDTO;
import com.groupb.pojo.dto.MessageDTO;
import com.groupb.pojo.dto.PostDTO;

import java.util.List;

public interface CommunityService {
    /**
     * 创建帖子
     * @param userId 用户id
     * @param content 帖子内容
     * @param imageUrls 图片url列表
     * @return 帖子DTO
     */
    PostDTO createPost(Long userId, String content, List<String> imageUrls);
    /**
     * 删除帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否成功
     */
    boolean deletePost(Long userId, Long postId);
    /**
     * 获取动态列表
     * @param userId 用户id
     * @param type 类型，可选值：following, all
     * @param page 页码
     * @param size 页大小
     * @return 帖子DTO列表
     */
    List<PostDTO> getFeed(Long userId, String type, int page, int size);
    
    /**
     * 获取单个帖子详情
     * @param postId 帖子ID
     * @param userId 当前用户ID（用于判断是否已点赞等）
     * @return 帖子DTO
     */
    PostDTO getPostById(Long postId, Long userId);
    /**
     * 点赞帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否成功
     */
    boolean likePost(Long userId, Long postId);
    /**
     * 取消点赞帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否成功
     */
    boolean unlikePost(Long userId, Long postId);
    /**
     * 添加评论
     * @param userId 用户id
     * @param postId 帖子id
     * @param content 评论内容
     * @param replyToCommentId 回复的评论id
     * @return 评论DTO
     */
    CommentDTO addComment(Long userId, Long postId, String content, Long replyToCommentId);
    /**
     * 获取评论列表
     * @param postId 帖子id
     * @param page 页码
     * @param size 页大小
     * @return 评论DTO列表
     */
    List<CommentDTO> getComments(Long postId, int page, int size);
    /**
     * 关注用户
     * @param userId 用户id
     * @param targetUserId 目标用户id
     */
    void follow(Long userId, Long targetUserId);
    /**
     * 取消关注用户
     * @param userId 用户id
     * @param targetUserId 目标用户id
     */
    void unfollow(Long userId, Long targetUserId);
    /**
     * 发送私信
     * @param fromUserId 发送者id
     * @param toUserId 接收者id
     * @param content 内容
     */
    void sendMessage(Long fromUserId, Long toUserId, String content);
    /**
     * 获取对话
     * @param userId 用户id
     * @param peerUserId 对方用户id
     * @param page 页码
     * @param size 页大小
     * @return 私信DTO列表
     */
    List<MessageDTO> getConversation(Long userId, Long peerUserId, int page, int size);
    /**
     * 获取最近联系人
     * @param userId 用户id
     * @return 最近联系人id列表
     */
    List<Long> getRecentContacts(Long userId);
    /**
     * 获取黑名单
     * @param userId 用户id
     * @return 黑名单id列表
     */
    boolean isBannedFromCommunity(Long userId);
    
    /**
     * 收藏帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否成功
     */
    boolean favoritePost(Long userId, Long postId);
    
    /**
     * 取消收藏帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否成功
     */
    boolean unfavoritePost(Long userId, Long postId);
    
    /**
     * 检查用户是否已收藏帖子
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否已收藏
     */
    boolean isPostFavorited(Long userId, Long postId);
}


