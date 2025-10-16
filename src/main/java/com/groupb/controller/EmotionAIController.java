package com.groupb.controller;

import com.groupb.pojo.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class EmotionAIController {

    private final ChatClient chatClient;
    @GetMapping(value = "/chat",produces = "application/json;charset=utf-8")
    public Flux<Result<String>> chat(@RequestParam String prompt, @RequestParam String chatId) {
        // 1. 调用AI接口，获取流式响应
        Flux<String> aiFlux = chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

        return aiFlux.map(Result::success);
    }

}
