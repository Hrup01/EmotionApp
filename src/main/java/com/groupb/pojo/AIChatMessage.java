package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 将Message转换为该类，用于传输给前端
 */
@Data
@NoArgsConstructor
@TableName("ai_chat_message")
public class AIChatMessage {

    @TableId(type = IdType.AUTO)
    @TableField("message_id")
    private long messageId;//主键自增长
    private String chatId;//会话Id
    private String role;//角色
    private String content;//内容

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AIChatMessage that = (AIChatMessage) o;
        return Objects.equals(chatId, that.chatId) && Objects.equals(role, that.role) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, role, content);
    }

    public AIChatMessage(Message message, String chatId) {

        this.chatId=chatId;
        switch (message.getMessageType()) {
            case USER -> role="user";
            case ASSISTANT -> role="assistant";
            default -> role="";
        }
        content= message.getText();
    }


}
