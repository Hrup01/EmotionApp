package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私信实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("private_message")
public class PrivateMessage {
    @TableId(type = IdType.AUTO)
    private Long id;//主键
    @TableField("from_user_name")
    private String fromUserName;//发送信息用户名
    @TableField("to_user_name")
    private String toUserName;//接收信息用户名
    private String content;//内容
    @TableField("is_read")
    private Boolean isRead=false;//是否已读
    @TableField(value="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;//发送时间
}


