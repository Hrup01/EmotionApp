package com.groupb.controller;


import com.groupb.pojo.dto.CommentDTO;
import com.groupb.pojo.dto.MessageDTO;
import com.groupb.pojo.dto.PostDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.pojo.User;
import com.groupb.service.CommunityService;
import com.groupb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 社区模块控制器
 */

@Slf4j
@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private UserService userService;

    private Long getCurrentUserId() {
        try {
            // 从SecurityContext中获取认证信息
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                log.warn("无法获取当前用户认证信息");
                return null;
            }
            
            // 从认证信息中获取用户名
            String username = auth.getPrincipal().toString();
            log.debug("当前用户: {}", username);
            
            // 通过用户名查询用户ID
            User user = userService.findByUsername(username);
            if (user != null) {
                return user.getId();
            }
            
            log.warn("未找到用户: {}", username);
            return null;
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            return null;
        }
    }

    /**
     * 创建帖子（支持文件上传）
     * API接口：POST /api/community/posts
     * 
     * 请求参数：
     * - content: 帖子内容（必填）
     * - images: 图片文件（可选，支持多文件上传）
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 处理上传的图片文件
     * 3. 调用服务层创建帖子
     * 4. 处理异常情况
     * 
     * @param content 帖子内容
     * @param images 图片文件数组
     * @return 创建结果，包含帖子数据或错误信息
     */
    @PostMapping("/posts")
    public Result<PostDTO> createPost(@RequestParam("content") String content,
                                      @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            //1. 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            
            //2. 验证内容
            if (content == null || content.trim().isEmpty()) {
                return Result.error("帖子内容不能为空");
            }
            
            //3. 处理图片文件
            List<String> imageUrls = null;
            if (images != null && images.length > 0) {
                // 这里可以调用FileUploadController的逻辑，或者直接处理
                // 为了简化，这里先返回错误，建议前端先上传图片再创建帖子
                return Result.error("请先上传图片，然后使用图片URL创建帖子");
            }
            
            //4. 创建帖子
            PostDTO post = communityService.createPost(userId, content, imageUrls);
            return Result.success(post, "发布成功");
        } catch (IllegalArgumentException ex) {
            return Result.error("内容包含敏感词，请修改后再试");
        } catch (Exception e) {
            log.error("发布帖子失败", e);
            return Result.error("发布失败，请稍后重试");
        }
    }

    /**
     * 创建帖子（使用图片URL）
     * API接口：POST /api/community/posts/with-urls
     * 
     * 请求参数：
     * - content: 帖子内容（必填）
     * - imageUrls: 图片URL列表（可选）
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 提取请求参数
     * 3. 调用服务层创建帖子
     * 4. 处理异常情况
     * 
     * @param body 请求体，包含帖子内容等信息
     * @return 创建结果，包含帖子数据或错误信息
     */
    @PostMapping("/posts/with-urls")
    public Result<PostDTO> createPostWithUrls(@RequestBody Map<String, Object> body) {
        try {
            //1. 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            //2.基于id创建帖子
            String content = (String) body.getOrDefault("content", "");
            @SuppressWarnings("unchecked")
            List<String> images = (List<String>) body.get("imageUrls");
            PostDTO post = communityService.createPost(userId, content, images);
            return Result.success(post, "发布成功");
        } catch (IllegalArgumentException ex) {
            return Result.error("内容包含敏感词，请修改后再试");
        } catch (Exception e) {
            log.error("发布帖子失败", e);
            return Result.error("发布失败，请稍后重试");
        }
    }
    /**
     * 删除帖子
     * API接口：DELETE /api/community/posts/{postId}
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 调用服务层删除帖子（只允许作者删除）
     * 3. 返回删除结果
     * 
     * @param postId 要删除的帖子ID
     * @return 删除结果
     */

    @DeleteMapping("/posts/{postId}")
    public Result<Void> deletePost(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        boolean ok = communityService.deletePost(userId, postId);
        return ok ? Result.success(null, "删除成功") : Result.error("无权删除或帖子不存在");
    }

    /**
     * 获取动态流
     * API接口：GET /api/community/feed
     * 
     * 支持两种动态类型：
     * 1. following - 关注用户的动态
     * 2. recommend - 推荐动态（默认）
     * 
     * 请求参数：
     * - type: 动态类型（following/recommend，默认recommend）
     * - page: 页码（默认0）
     * - size: 每页大小（默认10）
     * 
     * @param type 动态类型
     * @param page 页码
     * @param size 每页大小
     * @return 动态列表
     */
    @GetMapping("/feed")
    public Result<List<PostDTO>> feed(@RequestParam(defaultValue = "recommend") String type,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        List<PostDTO> list = communityService.getFeed(userId, type, page, size);
        return Result.success(list, "获取动态成功");
    }

    /**
     * 获取单个帖子详情
     * API接口：GET /api/community/posts/{postId}
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 调用服务层获取帖子详情
     * 3. 处理异常情况
     * 
     * @param postId 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/posts/{postId}")
    public Result<PostDTO> getPostDetail(@PathVariable Long postId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            PostDTO post = communityService.getPostById(postId, userId);
            return Result.success(post, "获取帖子详情成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取帖子详情失败", e);
            return Result.error("获取帖子详情失败，请稍后重试");
        }
    }

    /**
     * 点赞帖子
     * API接口：POST /api/community/posts/{postId}/like
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 调用服务层点赞帖子
     * 3. 处理异常情况
     * 
     * @param postId 帖子ID
     * @return 点赞结果
     */
    @PostMapping("/posts/{postId}/like")
    public Result<Void> like(@PathVariable Long postId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            communityService.likePost(userId, postId);
            return Result.success(null, "已点赞");
        } catch (Exception e) {
            return Result.error("点赞失败: " + e.getMessage());
        }
    }

    /**
     * 取消点赞帖子
     * API接口：DELETE /api/community/posts/{postId}/like
     * 
     * @param postId 帖子ID
     * @return 取消点赞结果
     */
    @DeleteMapping("/posts/{postId}/like")
    public Result<Void> unlike(@PathVariable Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        communityService.unlikePost(userId, postId);
        return Result.success(null, "已取消点赞");
    }

    /**
     * 添加评论
     * API接口：POST /api/community/posts/{postId}/comments
     * 
     * 请求参数：
     * - content: 评论内容（必填）
     * - replyToCommentId: 回复的评论ID（可选）
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 提取请求参数
     * 3. 调用服务层添加评论
     * 4. 处理异常情况
     * 
     * @param postId 帖子ID
     * @param body 请求体，包含评论内容等信息
     * @return 评论结果
     */
    @PostMapping("/posts/{postId}/comments")
    public Result<CommentDTO> comment(@PathVariable Long postId, @RequestBody Map<String, Object> body) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            String content = (String) body.getOrDefault("content", "");
            Long replyTo = body.get("replyToCommentId") == null ? null : Long.valueOf(body.get("replyToCommentId").toString());
            CommentDTO c = communityService.addComment(userId, postId, content, replyTo);
            return Result.success(c, "评论成功");
        } catch (Exception e) {
            return Result.error("评论失败: " + e.getMessage());
        }
    }

    /**
     * 获取帖子的评论列表
     * API接口：GET /api/community/posts/{postId}/comments
     * 
     * 请求参数：
     * - page: 页码（默认0）
     * - size: 每页大小（默认20）
     * 
     * @param postId 帖子ID
     * @param page 页码
     * @param size 每页大小
     * @return 评论列表
     */
    @GetMapping("/posts/{postId}/comments")
    public Result<List<CommentDTO>> listComments(@PathVariable Long postId,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(communityService.getComments(postId, page, size), "获取评论成功");
    }

    /**
     * 关注用户
     * API接口：POST /api/community/follow/{targetUserId}
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 调用服务层关注目标用户
     * 
     * @param targetUserId 要关注的用户ID
     * @return 关注结果
     */
    @PostMapping("/follow/{targetUserId}")
    public Result<Void> follow(@PathVariable Long targetUserId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        communityService.follow(userId, targetUserId);
        return Result.success(null, "已关注");
    }

    /**
     * 取消关注用户
     * API接口：DELETE /api/community/follow/{targetUserId}
     * 
     * @param targetUserId 要取消关注的用户ID
     * @return 取消关注结果
     */
    @DeleteMapping("/follow/{targetUserId}")
    public Result<Void> unfollow(@PathVariable Long targetUserId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        communityService.unfollow(userId, targetUserId);
        return Result.success(null, "已取消关注");
    }

    /**
     * 获取用户的社区封禁状态
     * API接口：GET /api/community/ban-status
     * 
     * @return 封禁状态信息
     */
    @GetMapping("/ban-status")
    public Result<Map<String, Object>> banStatus() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        boolean banned = communityService.isBannedFromCommunity(userId);
        return Result.success(Map.of("banned", banned), "查询成功");
    }

    /**
     * 发送私信
     * API接口：POST /api/community/dm/{toUserId}
     * 
     * 请求参数：
     * - content: 私信内容（必填）
     * 
     * 实现步骤：
     * 1. 获取当前用户ID
     * 2. 提取私信内容
     * 3. 调用服务层发送私信
     * 4. 处理异常情况
     * 
     * @param toUserId 接收者用户ID
     * @param body 请求体，包含私信内容
     * @return 发送结果
     */
    @PostMapping("/dm/{toUserId}")
    public Result<Void> sendDm(@PathVariable Long toUserId, @RequestBody Map<String, Object> body) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            String content = (String) body.getOrDefault("content", "");
            communityService.sendMessage(userId, toUserId, content);
            return Result.success(null, "发送成功");
        } catch (Exception e) {
            return Result.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 获取与指定用户的私信对话
     * API接口：GET /api/community/dm/{peerUserId}
     * 
     * 请求参数：
     * - page: 页码（默认0）
     * - size: 每页大小（默认20）
     * 
     * @param peerUserId 对方用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 私信列表
     */
    @GetMapping("/dm/{peerUserId}")
    public Result<List<MessageDTO>> listDm(@PathVariable Long peerUserId,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "20") Integer size) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        List<MessageDTO> list = communityService.getConversation(userId, peerUserId, page, size);
        return Result.success(list, "获取会话成功");
    }

    /**
     * 获取最近联系人列表
     * API接口：GET /api/community/dm/recent-contacts
     * 
     * @return 最近联系人用户ID列表
     */
    @GetMapping("/dm/recent-contacts")
    public Result<List<Long>> recentContacts() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        return Result.success(communityService.getRecentContacts(userId), "获取联系人成功");
    }

    /**
     * 收藏帖子
     * API接口：POST /api/community/posts/{postId}/favorite
     * 
     * @param postId 帖子ID
     * @return 收藏结果
     */
    @PostMapping("/posts/{postId}/favorite")
    public Result<Void> favoritePost(@PathVariable Long postId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean success = communityService.favoritePost(userId, postId);
            return success ? Result.success(null, "收藏成功") : Result.error("收藏失败");
        } catch (Exception e) {
            log.error("收藏帖子失败", e);
            return Result.error("收藏失败：" + e.getMessage());
        }
    }

    /**
     * 取消收藏帖子
     * API接口：DELETE /api/community/posts/{postId}/favorite
     * 
     * @param postId 帖子ID
     * @return 取消收藏结果
     */
    @DeleteMapping("/posts/{postId}/favorite")
    public Result<Void> unfavoritePost(@PathVariable Long postId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean success = communityService.unfavoritePost(userId, postId);
            return success ? Result.success(null, "取消收藏成功") : Result.error("取消收藏失败");
        } catch (Exception e) {
            log.error("取消收藏帖子失败", e);
            return Result.error("取消收藏失败：" + e.getMessage());
        }
    }

    /**
     * 检查帖子收藏状态
     * API接口：GET /api/community/posts/{postId}/favorite-status
     * 
     * @param postId 帖子ID
     * @return 收藏状态
     */
    @GetMapping("/posts/{postId}/favorite-status")
    public Result<Map<String, Object>> getFavoriteStatus(@PathVariable Long postId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean isFavorited = communityService.isPostFavorited(userId, postId);
            Map<String, Object> result = Map.of("isFavorited", isFavorited);
            return Result.success(result, "获取收藏状态成功");
        } catch (Exception e) {
            log.error("获取收藏状态失败", e);
            return Result.error("获取收藏状态失败：" + e.getMessage());
        }
    }
}
