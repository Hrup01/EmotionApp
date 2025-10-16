package com.groupb.controller;

import com.groupb.pojo.dto.EmotionDTO;
import com.groupb.pojo.dto.Result;
// import com.groupb.pojo.dto.WeeklyReportDTO; // 周报功能暂时注释
import com.groupb.pojo.User;
import com.groupb.service.EmotionService;
import com.groupb.service.UserService;
import com.groupb.util.EmotionColorUtil;
import com.groupb.util.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 情绪日记控制器
 * 为Android客户端提供RESTful API
 */
@Slf4j
@RestController
@RequestMapping("/api/emotion")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class EmotionController {

    @Autowired
    private EmotionService emotionService;
    
    @Autowired
    private UserService userService;

    /**
     * 从SecurityContext获取当前用户ID
     * 从JWT token中解析用户信息
     */
    private Long getCurrentUserId(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        
        try {
            // 从SecurityContext中获取用户名
            String username = auth.getPrincipal().toString();
            
            // 从JWT token中解析用户ID
            // 这里需要从请求头中获取token并解析用户ID
            // 暂时通过用户名查询用户ID，实际项目中应该从JWT token中解析
            log.debug("当前用户: {}", username);
            
            // 通过用户名查询用户ID
            User user = userService.findByUsername(username);
            if (user != null) {
                return user.getId();
            }
            
            return null;
        } catch (Exception e) {
            log.error("解析用户ID失败", e);
            return null;
        }
    }

    /**
     * 保存或更新情绪日记
     * POST /api/emotion/diary
     */
    @PostMapping("/diary")
    public Result<EmotionDTO> saveDiary(@RequestBody EmotionDTO dto, Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            // 验证必填字段
            if (dto.getEmotionType() == null || dto.getEmotionType().trim().isEmpty()) {
                return Result.error("情绪类型不能为空");
            }
            
            // 设置用户ID和默认日期
            dto.setUserId(userId);
            if (dto.getDiaryDate() == null) {
                dto.setDiaryDate(LocalDate.now());
            }
            
            emotionService.saveDiary(userId, dto);
            
            // 获取更新后的打卡天数
            Integer checkInCount = emotionService.getCurrentCheckInCount(userId);
            dto.setCheckInCount(checkInCount);
            
            log.info("用户 {} 保存情绪日记成功，日期：{}，情绪：{}，连续打卡：{}天", userId, dto.getDiaryDate(), dto.getEmotionType(), checkInCount);
            return Result.success(dto, "情绪日记保存成功，连续打卡 " + checkInCount + " 天");
        } catch (Exception e) {
            log.error("保存情绪日记失败", e);
            return Result.error("保存情绪日记失败：" + e.getMessage());
        }
    }

    /**
     * 获取指定日期的情绪日记
     * GET /api/emotion/diary?date=2024-01-01
     */
    @GetMapping("/diary")
    public Result<EmotionDTO> getDiaryByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            EmotionDTO diary = emotionService.getDiaryByDate(userId, date);
            if (diary == null) {
                return Result.error("该日期没有情绪日记记录");
            }
            return Result.success(diary, "获取情绪日记成功");
        } catch (Exception e) {
            log.error("获取情绪日记失败", e);
            return Result.error("获取情绪日记失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户最近的日记记录
     * GET /api/emotion/diaries/recent?limit=10
     */
    @GetMapping("/diaries/recent")
    public Result<List<EmotionDTO>> getRecentDiaries(
            @RequestParam(defaultValue = "10") Integer limit,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            List<EmotionDTO> diaries = emotionService.getRecentDiaries(userId, limit);
            return Result.success(diaries, "获取最近日记成功");
        } catch (Exception e) {
            log.error("获取最近日记失败", e);
            return Result.error("获取最近日记失败：" + e.getMessage());
        }
    }

    /**
     * 获取指定时间范围内的日记
     * GET /api/emotion/diaries/range?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/diaries/range")
    public Result<List<EmotionDTO>> getDiariesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            List<EmotionDTO> diaries = emotionService.getDiariesByDateRange(userId, startDate, endDate);
            return Result.success(diaries, "获取时间范围内日记成功");
        } catch (Exception e) {
            log.error("获取时间范围内日记失败", e);
            return Result.error("获取时间范围内日记失败：" + e.getMessage());
        }
    }

    /**
     * 删除指定ID的情绪日记
     * DELETE /api/emotion/diary/{id}
     */
    @DeleteMapping("/diary/{id}")
    public Result<Void> deleteDiaryById(@PathVariable Long id, Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            boolean deleted = emotionService.deleteDiaryById(userId, id);
            if (!deleted) {
                return Result.error("日记不存在或无权限删除");
            }
            
            log.info("用户 {} 删除情绪日记成功，ID：{}", userId, id);
            return Result.success(null, "删除情绪日记成功");
        } catch (Exception e) {
            log.error("删除情绪日记失败", e);
            return Result.error("删除情绪日记失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除指定日期的情绪日记
     * DELETE /api/emotion/diary/date?date=2024-01-01
     */
    @DeleteMapping("/diary/date")
    public Result<Void> deleteDiaryByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            boolean deleted = emotionService.deleteDiaryByDate(userId, date);
            if (!deleted) {
                return Result.error("该日期没有情绪日记记录");
            }
            
            log.info("用户 {} 删除日期 {} 的情绪日记成功", userId, date);
            return Result.success(null, "删除情绪日记成功");
        } catch (Exception e) {
            log.error("删除情绪日记失败", e);
            return Result.error("删除情绪日记失败：" + e.getMessage());
        }
    }

    /*
     * 周报功能 - 设计组构思中，暂时注释
     * 
     * 获取周报
     * GET /api/emotion/weekly-report?weekStart=2024-01-01
     *
    @GetMapping("/weekly-report")
    public Result<WeeklyReportDTO> getWeeklyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            WeeklyReportDTO report = emotionService.getWeeklyReport(userId, weekStart);
            return Result.success(report, "获取周报成功");
        } catch (Exception e) {
            log.error("获取周报失败", e);
            return Result.error("获取周报失败：" + e.getMessage());
        }
    }
    */

    /**
     * 获取用户当前连续打卡天数
     * GET /api/emotion/check-in-count
     */
    @GetMapping("/check-in-count")
    public Result<Map<String, Object>> getCurrentCheckInCount(Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            Integer checkInCount = emotionService.getCurrentCheckInCount(userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("checkInCount", checkInCount);
            result.put("message", checkInCount > 0 ? "已连续打卡 " + checkInCount + " 天" : "还没有打卡记录");
            
            return Result.success(result, "获取打卡天数成功");
        } catch (Exception e) {
            log.error("获取打卡天数失败", e);
            return Result.error("获取打卡天数失败：" + e.getMessage());
        }
    }

    /**
     * 获取可用的情绪类型列表
     * GET /api/emotion/emotion-types
     */
    @GetMapping("/emotion-types")
    public String getEmotionTypes() {
        return "{\"code\":200,\"message\":\"获取情绪类型成功\",\"data\":[]}";
    }

    /**
     * 获取可用的背景音乐列表
     * GET /api/emotion/background-music
     */
    @GetMapping("/background-music")
    public Result<List<String>> getBackgroundMusicList() {
        List<String> musicList = List.of(
            "Beach_64.m4a", "Cafe 1_64.m4a", "Female conversation (English speech)_64.m4a",
            "Forest 1_64.m4a", "splashing-rainfall160.mp3", "Thunder 2_64.m4a"
        );
        return Result.success(musicList, "获取背景音乐列表成功");
    }

    /**
     * 获取用户最近使用的情绪类型
     * GET /api/emotion/recent-emotions?limit=3
     */
    @GetMapping("/recent-emotions")
    public Result<List<Map<String, Object>>> getRecentEmotions(
            @RequestParam(defaultValue = "3") Integer limit,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            // 获取用户最近的日记记录
            List<EmotionDTO> recentDiaries = emotionService.getRecentDiaries(userId, 10);
            
            List<Map<String, Object>> recentEmotions = new ArrayList<>();
            
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
            
            return Result.success(recentEmotions, "获取最近情绪成功");
        } catch (Exception e) {
            log.error("获取最近情绪失败", e);
            return Result.error("获取最近情绪失败：" + e.getMessage());
        }
    }

    /**
     * 记录情绪选择
     * POST /api/emotion/mood-selection
     */
    @PostMapping("/mood-selection")
    public Result<Map<String, Object>> recordMoodSelection(
            @RequestBody Map<String, Object> moodData,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            String emotionType = (String) moodData.get("emotionType");
            if (emotionType == null || emotionType.trim().isEmpty()) {
                return Result.error("情绪类型不能为空");
            }
            
            // 记录情绪选择（可以用于统计用户偏好）
            Map<String, Object> result = new HashMap<>();
            result.put("emotionType", emotionType);
            result.put("emotionName", EmotionColorUtil.getEmotionName(emotionType));
            result.put("color", EmotionColorUtil.getEmotionColor(emotionType));
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("用户 {} 选择了情绪: {}", userId, emotionType);
            
            return Result.success(result, "情绪选择记录成功");
        } catch (Exception e) {
            log.error("记录情绪选择失败", e);
            return Result.error("记录情绪选择失败：" + e.getMessage());
        }
    }

    /**
     * 快速记录情绪日记（简化版）
     * POST /api/emotion/quick-diary
     */
    @PostMapping("/quick-diary")
    public Result<Map<String, Object>> quickDiary(
            @RequestBody Map<String, Object> diaryData,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            if (userId == null) {
                return Result.unauthorized("用户未登录");
            }
            
            // 提取数据
            String emotionType = (String) diaryData.get("emotionType");
            String content = (String) diaryData.get("content");
            
            // 验证必填字段
            if (emotionType == null || emotionType.trim().isEmpty()) {
                return Result.error("情绪类型不能为空");
            }
            
            // 设置默认值
            if (content == null) {
                content = ""; // 允许空内容
            }
            
            // 创建EmotionDTO
            EmotionDTO dto = new EmotionDTO();
            dto.setUserId(userId);
            dto.setDiaryDate(LocalDate.now());
            dto.setEmotionType(emotionType);
            dto.setContent(content);
            dto.setLocation(""); // 可以后续添加位置信息
            
            // 保存日记
            emotionService.saveDiary(userId, dto);
            
            // 获取更新后的打卡天数
            Integer checkInCount = emotionService.getCurrentCheckInCount(userId);
            
            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("id", dto.getId());
            result.put("emotionType", emotionType);
            result.put("emotionName", EmotionColorUtil.getEmotionName(emotionType));
            result.put("color", EmotionColorUtil.getEmotionColor(emotionType));
            result.put("content", content);
            result.put("diaryDate", dto.getDiaryDate());
            result.put("checkInCount", checkInCount);
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("用户 {} 快速记录情绪日记成功，情绪：{}，连续打卡：{}天", userId, emotionType, checkInCount);
            
            return Result.success(result, "快速记录成功，连续打卡 " + checkInCount + " 天");
        } catch (Exception e) {
            log.error("快速记录情绪日记失败", e);
            return Result.error("快速记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取日历数据
     * GET /api/emotion/calendar?year=2024&month=9
     */
    @GetMapping("/calendar")
    public Result<Map<String, Object>> getCalendarData(
            @RequestParam Integer year,
            @RequestParam Integer month,
            Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            
            // 计算月份的开始和结束日期
            LocalDate monthStart = LocalDate.of(year, month, 1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            
            // 获取该月的所有日记
            List<EmotionDTO> monthDiaries = emotionService.getDiariesByDateRange(userId, monthStart, monthEnd);
            
            // 构建日历数据
            Map<LocalDate, EmotionDTO> diaryMap = monthDiaries.stream()
                    .collect(Collectors.toMap(EmotionDTO::getDiaryDate, diary -> diary));
            
            List<Map<String, Object>> calendarDays = new ArrayList<>();
            LocalDate currentDate = monthStart;
            while (!currentDate.isAfter(monthEnd)) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", currentDate);
                dayData.put("dayOfMonth", currentDate.getDayOfMonth());
                dayData.put("dayOfWeek", currentDate.getDayOfWeek().getValue());
                
                EmotionDTO diary = diaryMap.get(currentDate);
                if (diary != null) {
                    dayData.put("hasEntry", true);
                    dayData.put("emotionType", diary.getEmotionType());
                    dayData.put("emotionName", EmotionColorUtil.getEmotionName(diary.getEmotionType()));
                    dayData.put("color", EmotionColorUtil.getEmotionColor(diary.getEmotionType()));
                } else {
                    dayData.put("hasEntry", false);
                    dayData.put("emotionType", null);
                    dayData.put("emotionName", null);
                    dayData.put("color", "#E0E0E0");
                }
                
                calendarDays.add(dayData);
                currentDate = currentDate.plusDays(1);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("year", year);
            result.put("month", month);
            result.put("monthName", getMonthName(month));
            result.put("days", calendarDays);
            result.put("totalDays", monthStart.lengthOfMonth());
            
            return Result.success(result, "获取日历数据成功");
        } catch (Exception e) {
            log.error("获取日历数据失败", e);
            return Result.error("获取日历数据失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取月份名称
     */
    private String getMonthName(int month) {
        String[] monthNames = {"", "一月", "二月", "三月", "四月", "五月", "六月",
                "七月", "八月", "九月", "十月", "十一月", "十二月"};
        return monthNames[month];
    }
}




