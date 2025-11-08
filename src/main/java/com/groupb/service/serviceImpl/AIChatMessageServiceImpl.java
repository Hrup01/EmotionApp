package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.groupb.mapper.AIChatHistoryMapper;
import com.groupb.mapper.AIChatMessageMapper;
import com.groupb.pojo.AIChatHistory;
import com.groupb.pojo.AIChatMessage;
import com.groupb.pojo.User;
import com.groupb.service.AIChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AIChatMessageServiceImpl implements AIChatMessageService, ChatMemoryRepository {

    @Autowired
    private AIChatMessageMapper aiChatMessageMapper;

    @Autowired
    private AIChatHistoryMapper aiChatHistoryMapper;



    private static int inCount=0;//计数，奇数保存用户信息，偶数保存助手信息

    //提供JDK21-的api(removeLast,getLast)
    public void removeLast(List<Message>messages){
        messages.remove(messages.size()-1);
    }

    public Message getLast(List<Message> messages){
        return messages.get(messages.size()-1);
    }

    /**
     * 获取全部会话Id(获取所有的chatId，并无区分用户，最好别用)
     * @return chatId集合
     */
    @Override
    public List<String> findConversationIds() {
        LambdaQueryWrapper<AIChatHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AIChatHistory::getChatId);
        List<Object> objectList = aiChatHistoryMapper.selectObjs(lambdaQueryWrapper);
        List<String> conversationIds = new ArrayList<>();
        objectList.forEach(o -> {
            conversationIds.add(o.toString());
        });
        log.info("获取全部chatId成功");
        return conversationIds;
    }

    /**
     * 根据会话Id获取会话信息
     * @param conversationId 会话Id
     * @return 会话信息集合
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        LambdaQueryWrapper<AIChatMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AIChatMessage::getChatId, conversationId);
        List<AIChatMessage> aiChatMessageList = aiChatMessageMapper.selectList(lambdaQueryWrapper);
        Collections.reverse(aiChatMessageList);//反转列表
        List<Message> messages = new ArrayList<>();
        aiChatMessageList.forEach(a -> {
            String type=a.getRole();
            switch (type) {
                case "user" -> messages.add(new UserMessage(a.getContent()));
                case "assistant" -> messages.add(new AssistantMessage(a.getContent()));
                default -> throw new IllegalArgumentException("Unknown message type: " + type);
            }
        });
        return messages;
    }

    /**
     * 根据会话Id保存全部的会话信息
     * @param conversationId 会话Id
     * @param messages 会话信息
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {

        Assert.notNull(conversationId, "保存会话内容时，会话Id不能为空");
        inCount++;//开始计数

        int count=0;//有效添加数--每次添加一条数据，在增强中一次交互触发俩次saveAll
        List<AIChatMessage> toInsert=new ArrayList<>();
        //有效添加一条后退出
        while(count<1&&!messages.isEmpty()) {
            AIChatMessage aiChatMessage = new AIChatMessage();
            //Message message = messages.getLast();
            Message message = getLast(messages);
            //messages.removeLast();//拿出最后一条信息并且移除（最后一条不一定是要保存的那条）
            removeLast(messages);
            aiChatMessage.setChatId(conversationId);
            aiChatMessage.setContent(message.getText());
            if(inCount%2==0) {//偶数添加助手信息
                switch(message.getMessageType()){
                    case ASSISTANT: aiChatMessage.setRole("assistant");count++;toInsert.add(aiChatMessage);break;
                    default:
                }
            }
            else if(inCount%2==1){//奇数添加用户信息
                switch(message.getMessageType()){
                    case USER: aiChatMessage.setRole("user");count++;toInsert.add(aiChatMessage);break;
                    default:
                }
            }
        }
        //保存信息
        if (!toInsert.isEmpty()) {
            toInsert.forEach(i->{
                aiChatMessageMapper.insert(i);
            });
        }
    }

    /**
     * 根据会话Id删除会话信息
     * @param conversationId 会话Id
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        LambdaUpdateWrapper<AIChatMessage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AIChatMessage::getChatId, conversationId);
        aiChatMessageMapper.delete(lambdaUpdateWrapper);
        log.info("会话删除成功");
    }

    /**
     * 减少Message-AIChatMessage之间的转化
     * @param conversationId 会话Id
     * @return AIChatMessage
     */
    @Override
    public List<AIChatMessage> find(String conversationId) {
        LambdaQueryWrapper<AIChatMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AIChatMessage::getChatId, conversationId);
        List<AIChatMessage> aiChatMessageList = aiChatMessageMapper.selectList(lambdaQueryWrapper);
        Collections.reverse(aiChatMessageList);//反转列表
        return aiChatMessageList;
    }
}

