package com.groupb.pojo.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 情绪日记传输对象
 * 封装情绪日记内容以及记录日期信息
 */
@Data
public class EmotionDTO {
    private Long id;
    private Long userId;//关联的用户ID
    private LocalDate diaryDate;//记录日期
    private String emotionType; //情绪类型
    private String content;//日记内容
    private String backgroundMusic;//背景音乐
    private String moodColor;//心情颜色
    private String location;//地点
    private Integer checkInCount;//连续打卡天数
}




