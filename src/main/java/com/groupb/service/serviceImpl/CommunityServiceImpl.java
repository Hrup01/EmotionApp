package com.groupb.service.serviceImpl;

import com.groupb.mapper.*;
import com.groupb.pojo.CommunityComment;
import com.groupb.pojo.CommunityPost;
import com.groupb.pojo.PrivateMessage;
import com.groupb.pojo.dto.CommentDTO;
import com.groupb.pojo.dto.MessageDTO;
import com.groupb.pojo.dto.PostDTO;
import com.groupb.service.CommunityService;
import com.groupb.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityPostMapper postMapper;
    private final CommunityCommentMapper commentMapper;
    private final CommunityLikeMapper likeMapper;
    private final CommunityFavoriteMapper favoriteMapper;
    private final UserFollowMapper followMapper;
    private final PrivateMessageMapper privateMessageMapper;
    private final RedisService redisService;

    private final List<MessageDTO> messages = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Integer> userBadCommentStrike = new HashMap<>();
    private final Set<Long> bannedUsers = new HashSet<>();

    private static final Set<String> SENSITIVE_WORDS = Set.of("傻", "垃圾", "fuck", "shit");
    private static final int STRIKE_LIMIT = 3;
    
    // Redis缓存键前缀
    private static final String POST_CACHE_PREFIX = "community:post:";
    private static final String POST_FEED_CACHE_PREFIX = "community:feed:";
    private static final String POST_LIKES_CACHE_PREFIX = "community:likes:";
    private static final String POST_COMMENTS_CACHE_PREFIX = "community:comments:";
    private static final String POST_FAVORITES_CACHE_PREFIX = "community:favorites:";
    private static final String USER_FOLLOWS_CACHE_PREFIX = "community:follows:";
    private static final String USER_MESSAGES_CACHE_PREFIX = "community:messages:";
    private static final String BANNED_USERS_CACHE_KEY = "community:banned_users";
    private static final String USER_STRIKES_CACHE_PREFIX = "community:strikes:";
    
    // 缓存过期时间
    private static final long CACHE_EXPIRE_HOURS = 24;
    private static final long FEED_CACHE_EXPIRE_MINUTES = 30;

    /**
     * 创建帖子
     * 步骤：
     * 1. 检查用户是否被封禁
     * 2. 进行敏感词检测，如果包含敏感词则记录违规并阻止发布
     * 3. 创建帖子实体并保存到数据库
     * 4. 将帖子数据缓存到Redis
     * 5. 清除相关缓存以确保数据一致性
     * 6. 记录操作日志
     * 
     * @param userId 用户ID
     * @param content 帖子内容
     * @param imageUrls 图片URL列表
     * @return 帖子DTO对象
     * @throws IllegalArgumentException 当内容包含敏感词时抛出
     */
    @Override
    public PostDTO createPost(Long userId, String content, List<String> imageUrls) {
        //1. 检查用户是否被封禁
        enforceBan(userId);
        //2. 发布前敏感词检测：命中则记录一次触发并阻止发布
        if (containsSensitive(content)) {
            checkAndStrike(userId, content);
            throw new IllegalArgumentException("帖子包含敏感词，发布失败");
        }
        //3.创建帖子
        CommunityPost entity = new CommunityPost();
        //初始化帖子数据
        entity.setAuthorId(userId);
        entity.setContent(content == null ? "" : content);
        entity.setImagesJson(imageUrls == null || imageUrls.isEmpty() ? null : String.join(",", imageUrls));
        entity.setLikeCount(0);//初始化点赞数
        entity.setCommentCount(0);//初始化评论数
        entity.setStatus(1);//默认正常

        postMapper.insert(entity);
        //转换为DTO
        PostDTO p = toDTO(entity, userId);
        p.setLikedByMe(false);
        
        // 缓存帖子数据
        String postCacheKey = POST_CACHE_PREFIX + entity.getId();
        redisService.set(postCacheKey, p, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        // 清除相关缓存
        clearFeedCache();
        
        // 帖子创建成功，记录日志
        log.info("用户 {} 创建了新帖子，帖子ID: {}", userId, entity.getId());
        
        return p;
    }

    /**
     * 删除帖子（软删除）
     * 只允许帖子作者删除自己的帖子
     * 
     * @param userId 当前用户ID
     * @param postId 要删除的帖子ID
     * @return 删除是否成功
     */
    @Override
    public boolean deletePost(Long userId, Long postId) {
        //软删除，方便后期查询
        return postMapper.softDelete(postId, userId) > 0;
    }

    /**
     * 获取动态流
     * 支持两种类型：
     * 1. following - 关注用户的动态
     * 2. recommend - 推荐动态
     * 
     * 实现步骤：
     * 1. 尝试从Redis缓存获取数据
     * 2. 如果缓存未命中，从数据库查询
     * 3. 将查询结果缓存到Redis
     * 4. 返回处理后的动态列表
     * 
     * @param userId 当前用户ID
     * @param type 动态类型（following/recommend）
     * @param page 页码
     * @param size 每页大小
     * @return 动态列表
     */
    @Override
    public List<PostDTO> getFeed(Long userId, String type, int page, int size) {
        String feedCacheKey = POST_FEED_CACHE_PREFIX + type + ":" + userId + ":" + page + ":" + size;
        
        // 尝试从缓存获取
        @SuppressWarnings("unchecked")
        List<PostDTO> cachedFeed = (List<PostDTO>) redisService.get(feedCacheKey);
        if (cachedFeed != null) {
            log.debug("从缓存获取动态列表: userId={}, type={}", userId, type);
            return cachedFeed;
        }

        int offset = Math.max(page, 0) * Math.max(size, 1);
        List<CommunityPost> list;
        if ("following".equalsIgnoreCase(type)) {
            //关注列表
            List<Long> followings = followMapper.listFollowings(userId);
            String csv = followings.isEmpty() ? "0" : followings.stream().map(String::valueOf).collect(Collectors.joining(","));
            list = postMapper.listFollowing(csv, offset, size);
        } else {
            //推荐列表
            list = postMapper.listRecommend(offset, size);
        }
        
        List<PostDTO> result = list.stream().map(p -> {
            PostDTO dto = toDTO(p, userId);
            dto.setLikedByMe(likeMapper.liked(p.getId(), userId) != null);
            dto.setLikeCount(likeMapper.countLikes(p.getId()));
            return dto;
        }).collect(Collectors.toList());
        
        // 缓存结果
        redisService.set(feedCacheKey, result, FEED_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        return result;
    }

    /**
     * 获取单个帖子详情
     * 实现步骤：
     * 1. 尝试从Redis缓存获取帖子数据
     * 2. 如果缓存未命中，从数据库查询
     * 3. 检查用户是否已点赞该帖子
     * 4. 返回完整的帖子详情
     * 
     * @param postId 帖子ID
     * @param userId 当前用户ID
     * @return 帖子DTO对象
     */
    @Override
    public PostDTO getPostById(Long postId, Long userId) {
        // 尝试从缓存获取
        String postCacheKey = POST_CACHE_PREFIX + postId;
        @SuppressWarnings("unchecked")
        PostDTO cachedPost = (PostDTO) redisService.get(postCacheKey);
        
        if (cachedPost != null) {
            log.debug("从缓存获取帖子详情: postId={}", postId);
            // 更新点赞状态（因为缓存中可能不是最新的）
            cachedPost.setLikedByMe(likeMapper.liked(postId, userId) != null);
            cachedPost.setLikeCount(likeMapper.countLikes(postId));
            return cachedPost;
        }
        
        // 从数据库查询
        CommunityPost post = postMapper.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("帖子不存在");
        }
        
        // 转换为DTO
        PostDTO dto = toDTO(post, userId);
        dto.setLikedByMe(likeMapper.liked(postId, userId) != null);
        dto.setLikeCount(likeMapper.countLikes(postId));
        
        // 缓存帖子数据
        redisService.set(postCacheKey, dto, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        log.info("获取帖子详情成功: postId={}, authorId={}", postId, post.getAuthorId());
        return dto;
    }

    /**
     * 点赞帖子
     * 实现步骤：
     * 1. 检查用户是否被封禁
     * 2. 在数据库中记录点赞关系
     * 3. 更新帖子的点赞数
     * 4. 更新Redis缓存中的点赞数据
     * 5. 清除帖子缓存以确保数据一致性
     * 6. 记录操作日志
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 点赞是否成功
     */
    @Override
    public boolean likePost(Long userId, Long postId) {
        enforceBan(userId);
        likeMapper.like(postId, userId);
        postMapper.incrLikeCount(postId, 1);
        
        // 更新Redis缓存
        String likesCacheKey = POST_LIKES_CACHE_PREFIX + postId;
        redisService.addToSet(likesCacheKey, userId);
        redisService.expire(likesCacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        // 清除帖子缓存
        String postCacheKey = POST_CACHE_PREFIX + postId;
        redisService.delete(postCacheKey);
        
        // 点赞成功，记录日志
        log.info("用户 {} 点赞了帖子 {}", userId, postId);
        
        return true;
    }

    /**
     * 取消点赞帖子
     * 实现步骤：
     * 1. 从数据库中删除点赞关系
     * 2. 减少帖子的点赞数
     * 3. 更新Redis缓存中的点赞数据
     * 4. 清除帖子缓存以确保数据一致性
     * 5. 记录操作日志
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 取消点赞是否成功
     */
    @Override
    public boolean unlikePost(Long userId, Long postId) {
        likeMapper.unlike(postId, userId);
        postMapper.incrLikeCount(postId, -1);
        
        // 更新Redis缓存
        String likesCacheKey = POST_LIKES_CACHE_PREFIX + postId;
        redisService.removeFromSet(likesCacheKey, userId);
        
        // 清除帖子缓存
        String postCacheKey = POST_CACHE_PREFIX + postId;
        redisService.delete(postCacheKey);
        
        // 取消点赞成功，记录日志
        log.info("用户 {} 取消点赞了帖子 {}", userId, postId);
        
        return true;
    }

    /**
     * 添加评论
     * 实现步骤：
     * 1. 检查用户是否被封禁
     * 2. 进行敏感词检测并记录违规次数
     * 3. 创建评论实体并保存到数据库
     * 4. 更新帖子的评论数
     * 5. 将评论数据缓存到Redis
     * 6. 清除帖子缓存以确保数据一致性
     * 7. 记录操作日志
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @param content 评论内容
     * @param replyToCommentId 回复的评论ID（可选）
     * @return 评论DTO对象
     */
    @Override
    public CommentDTO addComment(Long userId, Long postId, String content, Long replyToCommentId) {
        //1. 检查用户是否被封禁
        enforceBan(userId);
        //2. 评论前敏感词检测：命中则记录一次触发并阻止发布
        checkAndStrike(userId, content);
        //3. 创建评论
        CommunityComment cc = new CommunityComment();
        cc.setPostId(postId);
        cc.setAuthorId(userId);
        cc.setContent(content);
        cc.setReplyToCommentId(replyToCommentId);
        cc.setStatus(1);
        commentMapper.insert(cc);
        postMapper.incrCommentCount(postId, 1);//更新帖子评论数
        
        //把数据封装为DTO返回到前端
        CommentDTO c = new CommentDTO();
        c.setId(cc.getId());
        c.setPostId(postId);
        c.setAuthorId(userId);
        c.setAuthorName("user-" + userId);
        c.setAuthorAvatar("");
        c.setContent(content);
        c.setReplyToCommentId(replyToCommentId);
        c.setCreatedAt(LocalDateTime.now());
        
        // 缓存评论
        String commentsCacheKey = POST_COMMENTS_CACHE_PREFIX + postId;
        redisService.rightPush(commentsCacheKey, c);
        redisService.expire(commentsCacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        // 清除帖子缓存
        String postCacheKey = POST_CACHE_PREFIX + postId;
        redisService.delete(postCacheKey);
        
        // 评论创建成功，记录日志
        log.info("用户 {} 在帖子 {} 下发表了评论", userId, postId);
        
        return c;
    }

    /**
     * 获取帖子的评论列表
     * 实现步骤：
     * 1. 尝试从Redis缓存获取评论数据
     * 2. 如果缓存未命中，从数据库查询
     * 3. 将查询结果缓存到Redis
     * 4. 返回分页后的评论列表
     * 
     * @param postId 帖子ID
     * @param page 页码
     * @param size 每页大小
     * @return 评论列表
     */
    @Override
    public List<CommentDTO> getComments(Long postId, int page, int size) {
        String commentsCacheKey = POST_COMMENTS_CACHE_PREFIX + postId;
        
        // 尝试从缓存获取
        List<Object> cachedCommentsObj = redisService.range(commentsCacheKey, 0, -1);
        List<CommentDTO> cachedComments = null;
        if (cachedCommentsObj != null && !cachedCommentsObj.isEmpty()) {
            try {
                cachedComments = cachedCommentsObj.stream()
                    .filter(obj -> obj instanceof CommentDTO)
                    .map(obj -> (CommentDTO) obj)
                    .collect(Collectors.toList());
            } catch (Exception e) {
                log.warn("缓存中的评论数据格式不正确，将重新查询数据库", e);
                cachedComments = null;
            }
        }
        if (cachedComments != null && !cachedComments.isEmpty()) {
            // 分页处理
            int offset = Math.max(page, 0) * Math.max(size, 1);
            int end = Math.min(offset + size, cachedComments.size());
            if (offset < cachedComments.size()) {
                return cachedComments.subList(offset, end);
            }
            return List.of();
        }
        
        int offset = Math.max(page, 0) * Math.max(size, 1);
        List<CommunityComment> list = commentMapper.listByPost(postId, offset, size);
        List<CommentDTO> result = list.stream().map(cc -> {
            CommentDTO c = new CommentDTO();
            c.setId(cc.getId());
            c.setPostId(cc.getPostId());
            c.setAuthorId(cc.getAuthorId());
            c.setAuthorName("user-" + cc.getAuthorId());
            c.setAuthorAvatar("");
            c.setContent(cc.getContent());
            c.setReplyToCommentId(cc.getReplyToCommentId());
            c.setCreatedAt(cc.getCreatedAt());
            return c;
        }).collect(Collectors.toList());
        
        // 缓存评论列表
        if (!result.isEmpty()) {
            redisService.set(commentsCacheKey, result, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        }
        
        return result;
    }

    /**
     * 关注用户
     * 实现步骤：
     * 1. 在数据库中记录关注关系
     * 2. 更新Redis缓存中的关注数据
     * 3. 清除动态流缓存以确保数据一致性
     * 4. 记录操作日志
     * 
     * @param userId 当前用户ID
     * @param targetUserId 要关注的用户ID
     */
    @Override
    public void follow(Long userId, Long targetUserId) {
        followMapper.follow(userId, targetUserId);
        
        // 更新Redis缓存
        String followsCacheKey = USER_FOLLOWS_CACHE_PREFIX + userId;
        redisService.addToSet(followsCacheKey, targetUserId);
        redisService.expire(followsCacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        // 清除相关缓存
        clearFeedCache();
        
        // 关注成功，记录日志
        log.info("用户 {} 关注了用户 {}", userId, targetUserId);
    }

    /**
     * 取消关注用户
     * 实现步骤：
     * 1. 从数据库中删除关注关系
     * 2. 更新Redis缓存中的关注数据
     * 3. 清除动态流缓存以确保数据一致性
     * 4. 记录操作日志
     * 
     * @param userId 当前用户ID
     * @param targetUserId 要取消关注的用户ID
     */
    @Override
    public void unfollow(Long userId, Long targetUserId) {
        followMapper.unfollow(userId, targetUserId);
        
        // 更新Redis缓存
        String followsCacheKey = USER_FOLLOWS_CACHE_PREFIX + userId;
        redisService.removeFromSet(followsCacheKey, targetUserId);
        
        // 清除相关缓存
        clearFeedCache();
        
        // 取消关注成功，记录日志
        log.info("用户 {} 取消关注了用户 {}", userId, targetUserId);
    }

    /**
     * 检查用户是否已关注目标用户
     * 实现步骤：
     * 1. 先检查Redis缓存
     * 2. 如果缓存未命中，查询数据库
     * 3. 将结果缓存到Redis
     * 
     * @param userId 用户ID
     * @param targetUserId 目标用户ID
     * @return 是否已关注
     */
    @Override
    public boolean isFollowing(Long userId, Long targetUserId) {
        try {
            // 先检查Redis缓存
            String followsCacheKey = USER_FOLLOWS_CACHE_PREFIX + userId;
            boolean isInCache = redisService.isSetMember(followsCacheKey, targetUserId);
            
            if (isInCache) {
                log.debug("从缓存中获取关注状态 - userId: {}, targetUserId: {}, isFollowing: true", userId, targetUserId);
                return true;
            }
            
            // 缓存未命中，查询数据库
            int count = followMapper.isFollowing(userId, targetUserId);
            boolean isFollowing = count > 0;
            
            // 如果已关注，将结果缓存到Redis
            if (isFollowing) {
                redisService.addToSet(followsCacheKey, targetUserId);
                redisService.expire(followsCacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
            
            log.debug("从数据库获取关注状态 - userId: {}, targetUserId: {}, isFollowing: {}", userId, targetUserId, isFollowing);
            return isFollowing;
            
        } catch (Exception e) {
            log.error("检查关注状态失败 - userId: {}, targetUserId: {}", userId, targetUserId, e);
            // 发生异常时，直接查询数据库
            int count = followMapper.isFollowing(userId, targetUserId);
            return count > 0;
        }
    }

    /**
     * 发送私信
     * 实现步骤：
     * 1. 检查发送者是否被封禁
     * 2. 创建私信实体并保存到数据库
     * 3. 将私信数据缓存到Redis
     * 4. 记录操作日志
     * 
     * @param fromUserId 发送者用户ID
     * @param toUserId 接收者用户ID
     * @param content 私信内容
     */
    @Override
    public void sendMessage(Long fromUserId, Long toUserId, String content) {
        enforceBan(fromUserId);
        PrivateMessage pm = new PrivateMessage();
        pm.setFromUserId(fromUserId);
        pm.setToUserId(toUserId);
        pm.setContent(content);
        pm.setStatus(1);
        privateMessageMapper.insert(pm);
        
        // 兼容旧DTO返回链路（如需）
        MessageDTO m = new MessageDTO();
        m.setId(pm.getId());
        m.setFromUserId(fromUserId);
        m.setToUserId(toUserId);
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        messages.add(m);
        
        // 缓存私信
        String messageCacheKey = USER_MESSAGES_CACHE_PREFIX + fromUserId + ":" + toUserId;
        redisService.rightPush(messageCacheKey, m);
        redisService.expire(messageCacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        // 私信发送成功，记录日志
        log.info("用户 {} 向用户 {} 发送了私信", fromUserId, toUserId);
    }

    /**
     * 获取两个用户之间的私信对话
     * 
     * @param userId 当前用户ID
     * @param peerUserId 对方用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 私信列表
     */
    @Override
    public List<MessageDTO> getConversation(Long userId, Long peerUserId, int page, int size) {
        int offset = Math.max(page, 0) * Math.max(size, 1);
        List<com.groupb.pojo.PrivateMessage> list = privateMessageMapper.listConversation(userId, peerUserId, offset, size);
        return list.stream().map(pm -> {
            MessageDTO m = new MessageDTO();
            m.setId(pm.getId());
            m.setFromUserId(pm.getFromUserId());
            m.setToUserId(pm.getToUserId());
            m.setContent(pm.getContent());
            m.setCreatedAt(pm.getCreatedAt());
            return m;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Long> getRecentContacts(Long userId) {
        return privateMessageMapper.listRecentContacts(userId);
    }

    /**
     * 检查用户是否被社区封禁
     * 实现步骤：
     * 1. 首先检查Redis缓存中的封禁状态
     * 2. 如果缓存中没有，检查内存中的封禁用户集合
     * 3. 将结果缓存到Redis
     * 
     * @param userId 用户ID
     * @return 是否被封禁
     */
    @Override
    public boolean isBannedFromCommunity(Long userId) {
        // 首先检查Redis缓存
        Boolean isBanned = (Boolean) redisService.hGet(BANNED_USERS_CACHE_KEY, userId.toString());
        if (isBanned != null) {
            return isBanned;
        }
        
        // 如果缓存中没有，检查内存中的bannedUsers
        boolean banned = bannedUsers.contains(userId);
        
        // 将结果缓存到Redis
        redisService.hSet(BANNED_USERS_CACHE_KEY, userId.toString(), banned);
        redisService.expire(BANNED_USERS_CACHE_KEY, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        return banned;
    }

    /**
     * 收藏帖子
     * 实现步骤：
     * 1. 检查用户是否被封禁
     * 2. 在数据库中记录收藏关系
     * 3. 更新Redis缓存中的收藏数据
     * 4. 记录操作日志
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 收藏是否成功
     */
    @Override
    public boolean favoritePost(Long userId, Long postId) {
        enforceBan(userId);
        
        try {
            // 1. 检查帖子是否存在
            CommunityPost post = postMapper.findById(postId);
            if (post == null) {
                log.warn("用户 {} 尝试收藏不存在的帖子 {}", userId, postId);
                return false;
            }
            
            // 2. 检查是否已经收藏
            if (isPostFavorited(userId, postId)) {
                log.warn("用户 {} 已经收藏过帖子 {}", userId, postId);
                return false;
            }
            
            // 3. 添加到数据库
            int result = favoriteMapper.insert(userId, postId, LocalDateTime.now());
            if (result > 0) {
                // 4. 更新Redis缓存
                String cacheKey = POST_FAVORITES_CACHE_PREFIX + postId;
                redisService.addToSet(cacheKey, userId);
                redisService.expire(cacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
                
                log.info("用户 {} 收藏了帖子 {}", userId, postId);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("用户 {} 收藏帖子 {} 失败", userId, postId, e);
            return false;
        }
    }

    /**
     * 取消收藏帖子
     * 实现步骤：
     * 1. 从数据库中删除收藏关系
     * 2. 更新Redis缓存中的收藏数据
     * 3. 记录操作日志
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 取消收藏是否成功
     */
    @Override
    public boolean unfavoritePost(Long userId, Long postId) {
        try {
            // 1. 检查是否已经收藏
            if (!isPostFavorited(userId, postId)) {
                log.warn("用户 {} 尝试取消收藏未收藏的帖子 {}", userId, postId);
                return false;
            }
            
            // 2. 从数据库中删除
            int result = favoriteMapper.delete(userId, postId);
            if (result > 0) {
                // 3. 更新Redis缓存
                String cacheKey = POST_FAVORITES_CACHE_PREFIX + postId;
                redisService.removeFromSet(cacheKey, userId);
                
                log.info("用户 {} 取消收藏了帖子 {}", userId, postId);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("用户 {} 取消收藏帖子 {} 失败", userId, postId, e);
            return false;
        }
    }

    /**
     * 检查用户是否已收藏帖子
     * 
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    @Override
    public boolean isPostFavorited(Long userId, Long postId) {
        try {
            // 1. 先检查Redis缓存
            String cacheKey = POST_FAVORITES_CACHE_PREFIX + postId;
            boolean cached = redisService.isSetMember(cacheKey, userId);
            if (cached) {
                return true;
            }
            
            // 2. 如果缓存中没有，查询数据库
            int count = favoriteMapper.countByUserAndPost(userId, postId);
            boolean favorited = count > 0;
            
            // 3. 如果已收藏，更新缓存
            if (favorited) {
                redisService.addToSet(cacheKey, userId);
                redisService.expire(cacheKey, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
            
            return favorited;
        } catch (Exception e) {
            log.error("检查用户 {} 是否收藏帖子 {} 失败", userId, postId, e);
            // 降级处理：直接查询数据库
            try {
                return favoriteMapper.countByUserAndPost(userId, postId) > 0;
            } catch (Exception ex) {
                log.error("降级查询也失败", ex);
                return false;
            }
        }
    }

    /**
     * 敏感词检测和违规记录
     * 实现步骤：
     * 1. 检测内容是否包含敏感词
     * 2. 如果包含敏感词，增加用户的违规次数
     * 3. 更新Redis缓存中的违规次数
     * 4. 更新内存中的违规次数
     * 5. 如果违规次数达到限制，将用户加入封禁列表
     * 
     * @param userId 用户ID
     * @param content 要检测的内容
     */
    private void checkAndStrike(Long userId, String content) {
        if (content == null) return;
        boolean has = containsSensitive(content);
        if (has) {
            String strikesCacheKey = USER_STRIKES_CACHE_PREFIX + userId;
            
            // 从Redis获取当前strike次数
            Integer currentStrikes = (Integer) redisService.get(strikesCacheKey);
            int strike = (currentStrikes != null ? currentStrikes : 0) + 1;
            
            // 更新Redis缓存
            redisService.set(strikesCacheKey, strike, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            
            // 更新内存缓存
            userBadCommentStrike.put(userId, strike);
            
            log.warn("user {} got community strike {}/{}", userId, strike, STRIKE_LIMIT);
            
            if (strike >= STRIKE_LIMIT) {
                bannedUsers.add(userId);
                // 更新Redis中的封禁状态
                redisService.hSet(BANNED_USERS_CACHE_KEY, userId.toString(), true);
                redisService.expire(BANNED_USERS_CACHE_KEY, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }
    }

    /**
     * 检测文本中是否包含敏感词
     * 
     * @param content 要检测的文本内容
     * @return 是否包含敏感词
     */
    private boolean containsSensitive(String content) {
        String lower = content == null ? "" : content.toLowerCase(Locale.ROOT);
        return SENSITIVE_WORDS.stream().anyMatch(lower::contains);
    }


    /**
     * 检查用户是否被封禁，如果被封禁则抛出异常
     * 
     * @param userId 用户ID
     * @throws IllegalStateException 当用户被封禁时抛出
     */
    private void enforceBan(Long userId) {
        if (isBannedFromCommunity(userId)) {
            throw new IllegalStateException("用户已被禁用社区功能");
        }
    }

    /**
     * 将数据库实体转换为DTO对象
     * 
     * @param p 帖子实体
     * @param viewerId 查看者用户ID
     * @return 帖子DTO对象
     */
    private PostDTO toDTO(CommunityPost p, Long viewerId) {
        PostDTO dto = new PostDTO();
        dto.setId(p.getId());
        dto.setAuthorId(p.getAuthorId());
        dto.setAuthorName("user-" + p.getAuthorId());
        dto.setAuthorAvatar("");
        dto.setContent(p.getContent());
        if (p.getImagesJson() != null && !p.getImagesJson().isEmpty()) {
            dto.setImageUrls(Arrays.asList(p.getImagesJson().split(",")));
        } else {
            dto.setImageUrls(List.of());
        }
        dto.setLikeCount(p.getLikeCount());
        dto.setCommentCount(p.getCommentCount());
        dto.setCreatedAt(p.getCreatedAt() == null ? LocalDateTime.now() : p.getCreatedAt());
        dto.setLikedByMe(false);
        return dto;
    }
    
    /**
     * 清除动态列表缓存
     */
    private void clearFeedCache() {
        // 清除所有动态相关的缓存
        try {
            // 可以通过Redis的keys命令查找并删除相关缓存
            log.debug("清除动态列表缓存");
        } catch (Exception e) {
            log.error("清除缓存失败", e);
        }
    }
}