package com.groupb.pojo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 情绪日记实体类
 * 对应emotion_diaries表
 */
@Data
public class EmotionDiary {
    private Long id;
    private Long userId;//关联的用户id
    private LocalDate diaryDate;//日记日期
    private String emotionType; // happy, sad, angry, anxious, calm, excited, tired, confused
    private String content;// 内容
    private String backgroundMusic;//背景音乐路径
    private String moodColor;//情绪颜色
    private String location;//地点
    private Integer checkInCount;//连续打卡天数
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}
