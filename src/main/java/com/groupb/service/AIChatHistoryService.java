package com.groupb.service;

import com.groupb.pojo.AIChatHistory;

import java.util.List;

public interface AIChatHistoryService {


    /**
     * 保存会话历史
     * @param userId 用户ID
     * @param type 业务类型
     * @param chatId 会话ID
     */
    AIChatHistory saveChatHistory(Long userId,String type,String chatId);


    /**
     * 获取会话历史列表
     * @param userId 用户ID
     * @param type 业务类型
     * @return 会话历史列表(String(会话ID))
     */
    List<AIChatHistory> getChatHistory(Long userId, String type);

    /**
     * 删除会话历史
     * @param type 业务类型
     * @param chatId 会话ID
     */
    boolean deleteChatHistory(String type,String chatId);



}
