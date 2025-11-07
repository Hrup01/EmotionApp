package com.groupb.util.AI;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.groupb.mapper.AIChatHistoryMapper;
import com.groupb.mapper.AIChatMessageMapper;
import com.groupb.pojo.AIChatHistory;
import com.groupb.pojo.AIChatMessage;
import com.groupb.service.serviceImpl.AIChatMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 历史会话简单总结
 */
@Slf4j
@Component
public class AIEmotionHistorySummary {

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private NoMemoryAIHelper noMemoryAIHelper;

    @Autowired
    private AIChatHistoryMapper aiChatHistoryMapper;

    @Autowired
    private AIChatMessageMapper aiChatMessageMapper;

    public List<String> getSummary(Long userId) {
        //查询需要总结的会话历史
        LambdaQueryWrapper<AIChatHistory> queryWrapperHistory = new LambdaQueryWrapper<>();
        queryWrapperHistory.eq(AIChatHistory::getUserId, userId)
                .and(wrapper -> wrapper.isNull(AIChatHistory::getSummary)//空对话需要查出来
                        .or()
                        .eq(AIChatHistory::getSummary, "空对话"));//为“空对话”也需要查出来
        List<AIChatHistory> aiChatHistoryList = aiChatHistoryMapper.selectList(queryWrapperHistory);
        log.info("查询需要总结的历史会话，有{}条", aiChatHistoryList.size());
        //查询每个会话的第一条聊天数据
        List<AIChatMessage> aiChatMessageList = new ArrayList<>();
        aiChatHistoryList.forEach(x -> {
            LambdaQueryWrapper<AIChatMessage> queryWrapperMessages = new LambdaQueryWrapper<>();
            queryWrapperMessages.last("limit 1");
            queryWrapperMessages.eq(AIChatMessage::getChatId,x.getChatId());
            aiChatMessageList.add(aiChatMessageMapper.selectOne(queryWrapperMessages));
            log.info("查询历史会话中第一条历史信息");
        });
        //保存总结
        List<String> summary = new ArrayList<>();
        //获取到系统默认词的Map
        Map<String, String> aiDefaultPromprMap = NoMemoryAIHelper.getAIDefaultPromprMap();
        String key = aiDefaultPromprMap.get("会话总结历史记录");
        //输入系统默认词
        ChatClient chatClient = noMemoryAIHelper.defaultChatClient(ollamaChatModel, key);
        log.info("开始总结{}个历史记录", aiChatMessageList.size());
        aiChatMessageList.forEach(x -> {
            if (x==null||x.equals("")) {
                summary.add("空对话");
            }
            else{
                summary.add(noMemoryAIHelper.AIHelper(x.getContent(), chatClient));
            }
        });
        return summary;
    }

}
