package com.groupb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单测试控制器
 * 用于验证API基本功能
 */
@RestController
@RequestMapping("/api/test")
public class SimpleTestController {

    /**
     * 简单健康检查接口
     * GET /test/health
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDate.now());
        health.put("service", "EmotionApp API");
        return health;
    }

    /**
     * 获取服务器时间
     * GET /test/time
     */
    @GetMapping("/time")
    public Map<String, Object> getTime() {
        Map<String, Object> time = new HashMap<>();
        time.put("currentDate", LocalDate.now());
        time.put("timestamp", System.currentTimeMillis());
        return time;
    }
}


