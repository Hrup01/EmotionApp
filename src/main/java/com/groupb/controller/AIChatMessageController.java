package com.groupb.controller;


import com.groupb.pojo.AIChatMessage;
import com.groupb.pojo.dto.Result;
import com.groupb.service.AIChatMessageService;
import com.groupb.service.serviceImpl.AIChatMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/ai/message")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class AIChatMessageController {

    @Autowired
    private AIChatMessageServiceImpl chatMessageServiceImpl;

    @GetMapping("{chatId}")
    public Result<List<AIChatMessage>> getMessages(@PathVariable String chatId) {
        if (chatId==null) {
            log.error("chatId为空");
            return Result.error("chatId为空");
        }
        List<AIChatMessage> aiChatMessageList = chatMessageServiceImpl.find(chatId);
        //转化类型
        return Result.success(aiChatMessageList);
    }

}
