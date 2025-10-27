package com.groupb.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * SecurityContext工具类
 * 用于从SecurityContext中获取用户信息
 */
@Slf4j
public class SecurityContextUtil {

    /**
     * 从SecurityContext中获取当前用户ID
     * 优先从JWT token中解析，如果失败则通过用户名查询数据库
     * 
     * @return 用户ID，如果获取失败返回null
     */
    public static Long getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                log.debug("SecurityContext中没有认证信息");
                return null;
            }

            // 尝试从认证对象的details中获取用户ID（JWT token解析）
            if (auth.getDetails() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> details = (Map<String, Object>) auth.getDetails();
                Object userIdObj = details.get("userId");
                if (userIdObj instanceof Long) {
                    Long userId = (Long) userIdObj;
                    log.debug("从JWT token中获取到用户ID: {}", userId);
                    return userId;
                } else if (userIdObj instanceof Number) {
                    Long userId = ((Number) userIdObj).longValue();
                    log.debug("从JWT token中获取到用户ID: {}", userId);
                    return userId;
                }
            }

            // 如果无法从details中获取，记录警告并返回null
            log.warn("无法从认证对象details中获取用户ID，details类型: {}", 
                    auth.getDetails() != null ? auth.getDetails().getClass().getSimpleName() : "null");
            return null;

        } catch (Exception e) {
            log.error("从SecurityContext获取用户ID失败", e);
            return null;
        }
    }

    /**
     * 从SecurityContext中获取当前用户名
     * 
     * @return 用户名，如果获取失败返回null
     */
    public static String getCurrentUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getPrincipal() == null) {
                log.debug("SecurityContext中没有认证信息");
                return null;
            }

            String username = auth.getPrincipal().toString();
            log.debug("从SecurityContext中获取到用户名: {}", username);
            return username;

        } catch (Exception e) {
            log.error("从SecurityContext获取用户名失败", e);
            return null;
        }
    }

    /**
     * 检查当前用户是否已认证
     * 
     * @return 是否已认证
     */
    public static boolean isAuthenticated() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null && auth.isAuthenticated() && auth.getPrincipal() != null;
        } catch (Exception e) {
            log.error("检查用户认证状态失败", e);
            return false;
        }
    }

    /**
     * 获取当前认证信息（用于调试）
     * 
     * @return 认证信息字符串
     */
    public static String getAuthInfo() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return "No authentication";
            }
            
            StringBuilder info = new StringBuilder();
            info.append("Principal: ").append(auth.getPrincipal());
            info.append(", Authenticated: ").append(auth.isAuthenticated());
            info.append(", Details: ").append(auth.getDetails());
            
            return info.toString();
        } catch (Exception e) {
            return "Error getting auth info: " + e.getMessage();
        }
    }
}
