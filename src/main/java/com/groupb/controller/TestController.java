package com.groupb.controller;

import com.groupb.pojo.dto.Result;
import com.groupb.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试JWT token解析功能
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class TestController {

    /**
     * 测试JWT token解析功能
     * GET /api/test/jwt-info
     */
    @GetMapping("/jwt-info")
    public Result<Map<String, Object>> testJwtInfo() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取用户ID
            Long userId = SecurityContextUtil.getCurrentUserId();
            result.put("userId", userId);
            
            // 获取用户名
            String username = SecurityContextUtil.getCurrentUsername();
            result.put("username", username);
            
            // 检查认证状态
            boolean isAuthenticated = SecurityContextUtil.isAuthenticated();
            result.put("isAuthenticated", isAuthenticated);
            
            // 获取详细认证信息（用于调试）
            String authInfo = SecurityContextUtil.getAuthInfo();
            result.put("authInfo", authInfo);
            
            // 判断解析方式
            String parseMethod = userId != null ? "JWT Token解析" : "用户名查询";
            result.put("parseMethod", parseMethod);
            
            log.info("JWT测试 - 用户ID: {}, 用户名: {}, 认证状态: {}, 解析方式: {}", 
                    userId, username, isAuthenticated, parseMethod);
            
            return Result.success(result, "JWT token解析测试成功");
            
        } catch (Exception e) {
            log.error("JWT token解析测试失败", e);
            return Result.error("JWT token解析测试失败：" + e.getMessage());
        }
    }

    /**
     * 测试用户ID获取功能
     * GET /api/test/user-id
     */
    @GetMapping("/user-id")
    public Result<Map<String, Object>> testUserId() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            Long userId = SecurityContextUtil.getCurrentUserId();
            result.put("userId", userId);
            result.put("success", userId != null);
            result.put("message", userId != null ? "成功获取用户ID" : "无法获取用户ID");
            
            return Result.success(result, "用户ID获取测试完成");
            
        } catch (Exception e) {
            log.error("用户ID获取测试失败", e);
            return Result.error("用户ID获取测试失败：" + e.getMessage());
        }
    }
}
