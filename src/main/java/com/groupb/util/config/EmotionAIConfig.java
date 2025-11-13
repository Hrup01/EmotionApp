package com.groupb.util.config;

import com.groupb.service.serviceImpl.AIChatMessageServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 简易AI配置类
 */
@Configuration
public class EmotionAIConfig {


    @Autowired
    private AIChatMessageServiceImpl aiChatMessageServiceImpl;

    @Bean
    public ChatMemory chatMemory(){
        //注入自定义的chatMemoryRepository(MySql/Redis)版本(内置是用Map内存储存)
        return MessageWindowChatMemory.builder().chatMemoryRepository(aiChatMessageServiceImpl).build();
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel model,ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("你记住你现在的身份是一个情绪教练叫做小栈，专门为大家提供放松心情的建议，请以小栈的身份回答我的问题。")
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build())//增强
                .build();
    }

}
