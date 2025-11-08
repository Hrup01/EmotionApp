package com.groupb.controller;


import com.groupb.pojo.AIChatHistory;
import com.groupb.pojo.dto.Result;
import com.groupb.service.AIChatHistoryService;
import com.groupb.service.AIChatMessageService;
import com.groupb.service.serviceImpl.AIChatMessageServiceImpl;
import com.groupb.util.AI.AIEmotionHistorySummary;
import com.groupb.util.UserInformationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/ai/chatHistory")
@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class AIChatHistoryController {

    @Autowired
    private AIChatHistoryService chatHistoryService;

    @Autowired
    private AIChatMessageServiceImpl chatMessageServiceImpl;

    @Autowired
    private UserInformationUtil userInformationUtil;


    //业务类型为chat(目前只有chat，且无需前端发送)
    private final String typeName = "chat";

    /**
     * 添加历史会话
     * @param auth 用户信息
     * @param chatId 会话Id
     * @return 保存结果响应
     */
    @PostMapping("{chatId}")
    public Result<AIChatHistory> saveChatHistory(Authentication auth,@PathVariable String chatId) {
        //获取userId
        Long userId = userInformationUtil.getCurrentUserId(auth);
        if(userId == null){
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        AIChatHistory aiChatHistory = chatHistoryService.saveChatHistory(userId, chatId, typeName);
        return Result.success(aiChatHistory);

    }


    /**
     * 获取会话历史列表
     * @param auth 用户信息
     * @return 结果响应
     */
    @GetMapping
    public Result<List<AIChatHistory>> getChatHistory(Authentication auth) {
        Long userId = userInformationUtil.getCurrentUserId(auth);
        if(userId == null){
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        List<AIChatHistory> chatHistory = chatHistoryService.getChatHistory(userId, typeName);
        return Result.success(chatHistory);
    }

    /**
     * 根据chatId删除会话历史
     * @param auth 用户信息
     * @param chatId 会话Id
     * @return 响应
     */
    @DeleteMapping("{chatId}")
    public Result<Void> deleteChatHistory(Authentication auth,@PathVariable String chatId) {
        if (auth==null) {
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        boolean isDelete = chatHistoryService.deleteChatHistory(typeName, chatId);
        chatMessageServiceImpl.deleteByConversationId(chatId);//同时删除会话内容
        if(!isDelete){
            return Result.error("删除失败");
        }
        return Result.success();
    }


}
