package com.groupb.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 情绪颜色映射工具类
 * 根据UI设计提供情绪类型对应的颜色和中文名称
 */
public class EmotionColorUtil {
    
    private static final Map<String, EmotionInfo> EMOTION_MAP = new HashMap<>();
    
    static {
        // 根据前端UI设计中的12种情绪类型（使用中文作为键）
        EMOTION_MAP.put("开心", new EmotionInfo("开心", "#FFB6C1","保持好心情~")); // 粉色 - 开心
        EMOTION_MAP.put("伤心", new EmotionInfo("伤心", "#87CEEB","眼泪过后是晴天")); // 天蓝色 - 伤心
        EMOTION_MAP.put("自责", new EmotionInfo("自责", "#90EE90","你已经尽力啦")); // 浅绿色 - 自责
        EMOTION_MAP.put("晕", new EmotionInfo("晕", "#98FB98","停下来歇歇脚")); // 浅绿色 - 晕
        EMOTION_MAP.put("邪恶", new EmotionInfo("邪恶", "#FF6B6B","阳光就在心底")); // 红色 - 邪恶
        EMOTION_MAP.put("生气", new EmotionInfo("生气", "#FF4500","让风吹散怒气")); // 橙红色 - 生气
        EMOTION_MAP.put("困", new EmotionInfo("困", "#9370DB","身体需要充电")); // 紫色 - 困
        EMOTION_MAP.put("期待", new EmotionInfo("期待", "#FFD700","前方会有好事")); // 金色 - 期待
        EMOTION_MAP.put("无奈", new EmotionInfo("无奈", "#4682B4","调整方向再出发")); // 钢蓝色 - 无奈
        EMOTION_MAP.put("疑问", new EmotionInfo("疑问", "#FFA500","答案总会到来")); // 橙色 - 疑问
        EMOTION_MAP.put("满足", new EmotionInfo("满足", "#FF69B4","快乐如此简单")); // 热粉色 - 满足
        EMOTION_MAP.put("叹气", new EmotionInfo("叹气", "#A0522D","呼出烦恼就好")); // 赭色 - 叹气
    }
    
    /**
     * 获取情绪信息
     */
    public static EmotionInfo getEmotionInfo(String emotionType) {
        return EMOTION_MAP.getOrDefault(emotionType, new EmotionInfo("未知", "#808080",""));
    }
    
    /**
     * 获取情绪中文名称
     */
    public static String getEmotionName(String emotionType) {
        EmotionInfo info = getEmotionInfo(emotionType);
        return info != null ? info.getName() : "未知";
    }
    
    /**
     * 获取情绪颜色
     */
    public static String getEmotionColor(String emotionType) {
        EmotionInfo info = getEmotionInfo(emotionType);
        return info != null ? info.getColor() : "#808080";
    }
    
    /**
     * 情绪信息内部类
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class EmotionInfo {
        private String name;
        private String color;
        private String message;//不同心情代表的话
    }
}