package com.groupb.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ai_chat_history")
public class AIChatHistory {


    //外键用户ID
    @TableField(value="user_id")
    private long userId;
    //会话ID
    @TableId(type= IdType.INPUT)
    @TableField(value="chat_id")
    private String chatId;
    //业务类型(目前仅有chat类型)
    private String type;


}
