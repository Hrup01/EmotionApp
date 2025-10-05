package com.groupb.mapper;

import com.groupb.pojo.PrivateMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrivateMessageMapper {

    @Insert("INSERT INTO private_message(from_user_id, to_user_id, content, status) VALUES(#{fromUserId}, #{toUserId}, #{content}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PrivateMessage message);

    @Select("SELECT id, from_user_id as fromUserId, to_user_id as toUserId, content, status, created_at as createdAt " +
            "FROM private_message WHERE ((from_user_id=#{u1} AND to_user_id=#{u2}) OR (from_user_id=#{u2} AND to_user_id=#{u1})) " +
            "AND status=1 ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<PrivateMessage> listConversation(@Param("u1") Long u1, @Param("u2") Long u2, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT to_user_id FROM private_message WHERE from_user_id=#{userId} GROUP BY to_user_id ORDER BY MAX(created_at) DESC")
    List<Long> listRecentContacts(Long userId);
}


