package com.groupb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 情绪日记实体类
 * 对应emotion_diaries表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionDiary {
    private Long id;
    private Long userId;//关联的用户id
    private LocalDate diaryDate;//日记日期
    private String emotionType; // 情绪类型：开心, 伤心, 自责, 晕, 邪恶, 生气, 困, 期待, 无奈, 疑问, 满足, 叹气
    private String content;// 内容
    private String backgroundMusic;//背景音乐路径
    private String moodColor;//情绪颜色
    private String location;//地点
    private Integer checkInCount;//连续打卡天数
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}
