package com.groupb.controller;

import com.groupb.pojo.dto.EmotionDTO;
import com.groupb.service.EmotionService;
import com.groupb.util.EmotionColorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 情绪API控制器
 * 提供情绪选择相关的API接口
 */
@RestController
@RequestMapping("/api/emotion")
public class EmotionApiController {

    @Autowired
    private EmotionService emotionService;

    /**
     * 获取所有情绪类型列表
     * GET /api/emotion/types
     */
    @GetMapping("/types")
    public Map<String, Object> getEmotionTypes() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> emotionTypes = new ArrayList<>();
        
        // 12种情绪类型及其颜色
        String[][] emotions = {
            {"开心", "#FFB6C1"}, {"伤心", "#87CEEB"}, {"自责", "#90EE90"}, {"晕", "#98FB98"},
            {"邪恶", "#FF6B6B"}, {"生气", "#FF4500"}, {"困", "#9370DB"}, {"期待", "#FFD700"},
            {"无奈", "#4682B4"}, {"疑问", "#FFA500"}, {"满足", "#FF69B4"}, {"叹气", "#A0522D"}
        };
        
        for (String[] emotion : emotions) {
            Map<String, Object> emotionInfo = new HashMap<>();
            emotionInfo.put("name", emotion[0]);
            emotionInfo.put("color", emotion[1]);
            emotionTypes.add(emotionInfo);
        }
        
        response.put("code", 200);
        response.put("message", "获取情绪类型成功");
        response.put("data", emotionTypes);
        
        return response;
    }
    
    /**
     * 获取最近使用的情绪（需要认证）
     * GET /api/emotion/recent?limit=4&userId=1
     */
    @GetMapping("/recent")
    public Map<String, Object> getRecentEmotions(
            @RequestParam(defaultValue = "4") Integer limit,
            @RequestParam(required = false) Long userId) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> recentEmotions = new ArrayList<>();
        
        try {
            if (userId == null) {
                // 如果没有提供userId，返回空状态
                Map<String, Object> emptyState = new HashMap<>();
                emptyState.put("isEmpty", true);
                emptyState.put("message", "请先登录");
                emptyState.put("suggestion", "登录后查看最近的情绪记录");
                recentEmotions.add(emptyState);
            } else {
                // 从数据库查询用户的最近情绪记录
                List<EmotionDTO> recentDiaries = emotionService.getRecentDiaries(userId, 10);
                
                if (recentDiaries == null || recentDiaries.isEmpty()) {
                    // 当没有最近情绪时，返回空状态信息
                    Map<String, Object> emptyState = new HashMap<>();
                    emptyState.put("isEmpty", true);
                    emptyState.put("message", "最近没有选择情绪");
                    emptyState.put("suggestion", "选择一种情绪开始记录吧！");
                    recentEmotions.add(emptyState);
                } else {
                    // 提取最近使用的情绪类型
                    Map<String, Integer> emotionCount = new HashMap<>();
                    for (EmotionDTO diary : recentDiaries) {
                        String emotionType = diary.getEmotionType();
                        emotionCount.put(emotionType, emotionCount.getOrDefault(emotionType, 0) + 1);
                    }
                    
                    // 按使用频率排序，取前N个
                    recentEmotions = emotionCount.entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .limit(limit)
                            .map(entry -> {
                                Map<String, Object> emotionInfo = new HashMap<>();
                                emotionInfo.put("name", EmotionColorUtil.getEmotionName(entry.getKey()));
                                emotionInfo.put("color", EmotionColorUtil.getEmotionColor(entry.getKey()));
                                emotionInfo.put("count", entry.getValue());
                                return emotionInfo;
                            })
                            .collect(Collectors.toList());
                }
            }
            
            response.put("code", 200);
            response.put("message", "获取最近情绪成功");
            response.put("data", recentEmotions);
            response.put("hasRecentEmotions", !recentEmotions.isEmpty() && !recentEmotions.get(0).containsKey("isEmpty"));
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取最近情绪失败：" + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("hasRecentEmotions", false);
        }
        
        return response;
    }
}
