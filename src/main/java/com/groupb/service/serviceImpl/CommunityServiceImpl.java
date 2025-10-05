package com.groupb.service.serviceImpl;

import com.groupb.mapper.*;
import com.groupb.pojo.CommunityComment;
import com.groupb.pojo.CommunityPost;
import com.groupb.pojo.PrivateMessage;
import com.groupb.pojo.UserCommunityBan;
import com.groupb.pojo.dto.CommentDTO;
import com.groupb.pojo.dto.MessageDTO;
import com.groupb.pojo.dto.PostDTO;
import com.groupb.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityPostMapper postMapper;
    private final CommunityCommentMapper commentMapper;
    private final CommunityLikeMapper likeMapper;
    private final UserFollowMapper followMapper;
    private final PrivateMessageMapper privateMessageMapper;
    private final UserCommunityBanMapper userCommunityBanMapper;

    private final List<MessageDTO> messages = Collections.synchronizedList(new ArrayList<>());//存储私信
    private final Map<Long, Integer> userBadCommentStrike = new HashMap<>();//用户敏感词触发次数（内存缓存，可选）
    private final Set<Long> bannedUsers = new HashSet<>();//封禁用户（启动后懒加载缓存，可选）

    private static final Set<String> SENSITIVE_WORDS = Set.of("傻", "垃圾", "fuck", "shit");//敏感词管理
    private static final int STRIKE_LIMIT = 3;//敏感词触发限制

    @Override
    public PostDTO createPost(Long userId, String content, List<String> imageUrls) {
        //1. 检查用户是否被封禁
        enforceBan(userId);
        //2.初始化帖子信息
        CommunityPost entity = new CommunityPost();
        entity.setAuthorId(userId);
        entity.setContent(content == null ? "" : content);
        entity.setImagesJson(imageUrls == null || imageUrls.isEmpty() ? null : String.join(",", imageUrls));
        entity.setLikeCount(0);//初始化点赞数
        entity.setCommentCount(0);//初始化评论数
        entity.setStatus(1);//帖子状态默认正常
        postMapper.insert(entity);
        PostDTO p = toDTO(entity, userId);
        p.setLikedByMe(false);
        return p;
    }

    //软删除（保证数据的一致性）
    @Override
    public boolean deletePost(Long userId, Long postId) {
        return postMapper.softDelete(postId, userId) > 0;
    }

    //
    @Override
    public List<PostDTO> getFeed(Long userId, String type, int page, int size) {
        //参数验证和默认值设置
        if (type == null || type.trim().isEmpty()){
            type = "recommended";
        }
        if (page < 0) page = 0;
        if (size < 0) size = 10;
        int offset = Math.max(page, 0) * Math.max(size, 1);
        List<CommunityPost> list;
        //1. 获取关注列表
        if ("following".equalsIgnoreCase(type)) {
            List<Long> followings = followMapper.listFollowings(userId);
            String csv = followings.isEmpty() ? "0" : followings.stream().map(String::valueOf).collect(Collectors.joining(","));
            list = postMapper.listFollowing(csv, offset, size);
        } else {
            //2. 获取推荐列表
            list = postMapper.listRecommend(offset, size);
        }
        //3. 封装为DTO返回到前端
        return list.stream().map(p -> {
            PostDTO dto = toDTO(p, userId);
            dto.setLikedByMe(likeMapper.liked(p.getId(), userId) != null);
            dto.setLikeCount(likeMapper.countLikes(p.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean likePost(Long userId, Long postId) {
        //1.检查用户状态
        enforceBan(userId);
        //2.调用Mapper接口操作点赞列表完成点赞操作
        likeMapper.like(postId, userId);
        postMapper.incrLikeCount(postId, 1);
        return true;
    }

    @Override
    public boolean unlikePost(Long userId, Long postId) {
        int affected = likeMapper.unlike(postId, userId);
        if (affected > 0) {
            // 仅当确实删除了一条点赞记录时再安全自减
            postMapper.decrLikeCountSafe(postId);
        }
        return affected > 0;
    }


    @Override
    public CommentDTO addComment(Long userId, Long postId, String content, Long replyToCommentId) {
        //1.检查用户状态并检查内容是否包含敏感词
        enforceBan(userId);
        checkAndStrike(userId, content);
        //2.把传输的数据封装为实体对象
        CommunityComment cc = new CommunityComment();
        cc.setPostId(postId);
        cc.setAuthorId(userId);
        cc.setContent(content);
        cc.setReplyToCommentId(replyToCommentId);
        cc.setStatus(1);
        //3.调用Mapper接口完成插入操作
        commentMapper.insert(cc);
        postMapper.incrCommentCount(postId, 1);
        //4.封装为DTO返回给前端
        CommentDTO c = new CommentDTO();
        c.setId(cc.getId());
        c.setPostId(postId);
        c.setAuthorId(userId);
        c.setAuthorName("user-" + userId);
        c.setAuthorAvatar("");
        c.setContent(content);
        c.setReplyToCommentId(replyToCommentId);
        c.setCreatedAt(LocalDateTime.now());
        return c;
    }

    @Override
    public List<CommentDTO> getComments(Long postId, int page, int size) {
        int offset = Math.max(page, 0) * Math.max(size, 1);
        List<CommunityComment> list = commentMapper.listByPost(postId, offset, size);
        return list.stream().map(cc -> {
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
    }

    @Override
    public void follow(Long userId, Long targetUserId) {
        followMapper.follow(userId, targetUserId);
    }

    @Override
    public void unfollow(Long userId, Long targetUserId) {
        followMapper.unfollow(userId, targetUserId);
    }

    @Override
    public void sendMessage(Long fromUserId, Long toUserId, String content) {
        enforceBan(fromUserId);
        PrivateMessage pm = new com.groupb.pojo.PrivateMessage();
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
    }

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

    @Override
    public boolean isBannedFromCommunity(Long userId) {
        // 先查内存缓存，未命中则查库
        if (bannedUsers.contains(userId)) return true;
        UserCommunityBan record = userCommunityBanMapper.findByUserId(userId);
        if (record == null) return false;
        boolean banned = record.getStrikeCount() != null && record.getStrikeCount() >= STRIKE_LIMIT;
        if (banned) {
            bannedUsers.add(userId);
        }
        return banned;
    }

    /**
     * 检查内容是否包含敏感词
     */
    private void checkAndStrike(Long userId, String content) {
        if (content == null) return;
        String lower = content.toLowerCase(Locale.ROOT);
        boolean has = SENSITIVE_WORDS.stream().anyMatch(lower::contains);
        if (has) {
            // 1) 数据库累加
            userCommunityBanMapper.upsertAndIncrementStrike(userId, "敏感词触发");
            UserCommunityBan record = userCommunityBanMapper.findByUserId(userId);
            int strike = record == null || record.getStrikeCount() == null ? 0 : record.getStrikeCount();
            // 2) 内存缓存可选维护
            userBadCommentStrike.put(userId, strike);
            log.warn("user {} got community strike {}/{}", userId, strike, STRIKE_LIMIT);
            if (strike >= STRIKE_LIMIT) {
                bannedUsers.add(userId);
            }
        }
    }


    private void enforceBan(Long userId) {
        if (isBannedFromCommunity(userId)) {
            throw new IllegalStateException("用户已被禁用社区功能");
        }
    }


    /**
     * 转换为DTO
     */
    private PostDTO toDTO(CommunityPost p, Long viewerId) {
        //把帖子数据封装到Dto返回到前端
        PostDTO dto = new PostDTO();
        dto.setId(p.getId());//帖子id
        dto.setAuthorId(p.getAuthorId());//初始化用户id
        dto.setAuthorName("user-" + p.getAuthorId());//初始化用户名称
        dto.setAuthorAvatar("");//初始化用户头像
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
}


