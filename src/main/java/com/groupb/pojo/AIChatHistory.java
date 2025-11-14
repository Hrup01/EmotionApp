package com.groupb.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @TableField("summary")//会话总结
    private String summary;

    @TableField(value="create_time",fill= FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间


}
