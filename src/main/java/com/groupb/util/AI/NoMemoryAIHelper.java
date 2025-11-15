package com.groupb.util.AI;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AI辅助类(无记忆)
 */
@Slf4j
@Data
@Component
public class NoMemoryAIHelper {

    //存放prompt
    @Getter
    private static final Map<String,String> AIDefaultPromprMap=new HashMap<>();
    //在static内部放提示词
    static{
        AIDefaultPromprMap.put("会话总结历史记录","你是一个专业的语句总结员，请你总结一下以下这句话，控制在10个字以内");
        AIDefaultPromprMap.put("情绪建议","你是一个专业的情绪建议大师，我将给出我这周的心情，请你给出30个字的心情建议");
    }

    /**
     * 自定义系统提示词的ChatClient
     * @param model 模型
     * @param defaultPrompt 系统提示词
     * @return ChatClient对象
     */
    public ChatClient defaultChatClient(OllamaChatModel model,String defaultPrompt){
        log.info("返回默认值的ChatClient");
        return ChatClient
                .builder(model)
                .defaultSystem(defaultPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    /**
     * 自定义系统提示词下的AI辅助
     * @param prompt 输入词
     * @return AI解答
     */
    public String AIHelper(String prompt,ChatClient chatClient){
        log.info("系统默认值的AIHelper");
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

}
