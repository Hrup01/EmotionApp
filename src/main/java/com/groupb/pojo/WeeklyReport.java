package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 周报实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("weekly_report")
public class WeeklyReport {

    //主键
    @TableId(type= IdType.AUTO)
    private long id;
    //外键用户Id
    @TableField("user_id")
    private long userId;
    //最大心情
    @TableField("more_emotion")
    private String moreEmotion;
    //本周打卡次数
    private Long count;
    //情绪建议
    @TableField("emotion_advice")
    private String emotionAdvice;
    //创建时间
    @TableField(value="create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
