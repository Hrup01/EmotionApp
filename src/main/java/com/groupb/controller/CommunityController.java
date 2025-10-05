package com.groupb.controller;


import com.groupb.pojo.dto.CommentDTO;
import com.groupb.pojo.dto.MessageDTO;
import com.groupb.pojo.dto.PostDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 情感社区控制器
 */

@Slf4j
@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    /**
     * 获取当前用户ID
     * @return
     */
    private Long getCurrentUserId() {
        // TODO: 结合JWT获取真实用户，这里先返回1用于联调
        return 1L;
    }

    /**
     * 发布帖子
     * @param body
     * @return
     */

    @PostMapping("/posts")
    public Result<PostDTO> createPost(@RequestBody Map<String, Object> body) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            String content = (String) body.getOrDefault("content", "");
            @SuppressWarnings("unchecked")
            List<String> images = (List<String>) body.get("imageUrls");
            PostDTO post = communityService.createPost(userId, content, images);
            return Result.success(post, "发布成功");
        } catch (Exception e) {
            log.error("发布帖子失败", e);
            return Result.error("发布失败: " + e.getMessage());
        }
    }

    /**
     * 删除帖子
     * @param postId
     * @return
     */
    @DeleteMapping("/posts/{postId}")
    public Result<Void> deletePost(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        boolean ok = communityService.deletePost(userId, postId);
        return ok ? Result.success(null, "删除成功") : Result.error("无权删除或帖子不存在");
    }

    /**
     * 获取动态（默认为推荐列表）
     * @param type
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/feed")
    public Result<List<PostDTO>> feed(@RequestParam(defaultValue = "recommend") String type,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        List<PostDTO> list = communityService.getFeed(userId, type, page, size);
        return Result.success(list, "获取动态成功");
    }

    /**
     * 点赞
     * @param postId
     * @return
     */
    @PostMapping("/posts/{postId}/like")
    public Result<Void> like(@PathVariable Long postId) {
        try {
            communityService.likePost(getCurrentUserId(), postId);
            return Result.success(null, "已点赞");
        } catch (Exception e) {
            return Result.error("点赞失败: " + e.getMessage());
        }
    }


    /**
     * 取消点赞
     * @param postId
     * @return
     */
    @DeleteMapping("/posts/{postId}/like")
    public Result<Void> unlike(@PathVariable Long postId) {
        communityService.unlikePost(getCurrentUserId(), postId);
        return Result.success(null, "已取消点赞");
    }

    /**
     * 评论
     * @param postId
     * @param body
     * @return
     */

    @PostMapping("/posts/{postId}/comments")
    public Result<CommentDTO> comment(@PathVariable Long postId, @RequestBody Map<String, Object> body) {
        try {
            String content = (String) body.getOrDefault("content", "");
            Long replyTo = body.get("replyToCommentId") == null ? null : Long.valueOf(body.get("replyToCommentId").toString());
            CommentDTO c = communityService.addComment(getCurrentUserId(), postId, content, replyTo);
            return Result.success(c, "评论成功");
        } catch (Exception e) {
            return Result.error("评论失败: " + e.getMessage());
        }
    }

    /**
     * 获取评论
     * @param postId
     * @param page
     * @param size
     * @return
     */

    @GetMapping("/posts/{postId}/comments")
    public Result<List<CommentDTO>> listComments(@PathVariable Long postId,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(communityService.getComments(postId, page, size), "获取评论成功");
    }

    /**
     * 关注
     * @param targetUserId
     * @return
     */

    @PostMapping("/follow/{targetUserId}")
    public Result<Void> follow(@PathVariable Long targetUserId) {
        communityService.follow(getCurrentUserId(), targetUserId);
        return Result.success(null, "已关注");
    }

    /**
     * 取消关注
     * @param targetUserId
     * @return
     */

    @DeleteMapping("/follow/{targetUserId}")
    public Result<Void> unfollow(@PathVariable Long targetUserId) {
        communityService.unfollow(getCurrentUserId(), targetUserId);
        return Result.success(null, "已取消关注");
    }

    /**
     * 获取封禁状态
     * @return
     */

    @GetMapping("/ban-status")
    public Result<Map<String, Object>> banStatus() {
        boolean banned = communityService.isBannedFromCommunity(getCurrentUserId());
        return Result.success(Map.of("banned", banned), "查询成功");
    }

    /**
     * 发送私信
     * @param toUserId
     * @param body
     * @return
     */
    @PostMapping("/dm/{toUserId}")
    public Result<Void> sendDm(@PathVariable Long toUserId, @RequestBody Map<String, Object> body) {
        try {
            String content = (String) body.getOrDefault("content", "");
            communityService.sendMessage(getCurrentUserId(), toUserId, content);
            return Result.success(null, "发送成功");
        } catch (Exception e) {
            return Result.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 获取私信
     * @param peerUserId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/dm/{peerUserId}")
    public Result<List<MessageDTO>> listDm(@PathVariable Long peerUserId,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "20") Integer size) {
        List<MessageDTO> list = communityService.getConversation(getCurrentUserId(), peerUserId, page, size);
        return Result.success(list, "获取会话成功");
    }

    /**
     * 获取最近联系人
     * @return
     */
    @GetMapping("/dm/recent-contacts")
    public Result<List<Long>> recentContacts() {
        return Result.success(communityService.getRecentContacts(getCurrentUserId()), "获取联系人成功");
    }
}
