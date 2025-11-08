package com.groupb.service;

import com.groupb.pojo.AIChatMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * 对AIChatMessage的扩展功能
 */
public interface AIChatMessageService {

//    /**
//     * 添加一个会话
//     * @param chatId
//     * @return
//     */
//    String addConversation(String chatId);


    List<AIChatMessage> find(String conversationId);


}
