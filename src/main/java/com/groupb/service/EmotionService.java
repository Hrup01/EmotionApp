package com.groupb.service;

import com.groupb.pojo.dto.EmotionDTO;
// import com.groupb.pojo.dto.WeeklyReportDTO; // 周报功能暂时注释

import java.time.LocalDate;
import java.util.List;

/**
 * 情绪日记服务接口
 */
public interface EmotionService {
    
    /**
     * 保存或更新情绪日记
     * @return true 表示创建了新的日记（可用于发放积分），false 表示对既有记录进行了更新
     */
    boolean saveDiary(Long userId, EmotionDTO dto);
    
    /**
     * 获取指定日期的情绪日记
     */
    EmotionDTO getDiaryByDate(Long userId, LocalDate date);
    
    /**
     * 获取用户最近的日记记录
     */
    List<EmotionDTO> getRecentDiaries(Long userId, Integer limit);
    
    /**
     * 删除指定ID的情绪日记
     */
    boolean deleteDiaryById(Long userId, Long diaryId);
    
    /**
     * 删除指定日期的情绪日记
     */
    boolean deleteDiaryByDate(Long userId, LocalDate date);
    
    /*
     * 周报功能 - 设计组构思中，暂时注释
     * 
     * 获取周报
     *
    WeeklyReportDTO getWeeklyReport(Long userId, LocalDate weekStart);
    */
    
    /**
     * 获取指定时间范围内的日记
     */
    List<EmotionDTO> getDiariesByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取用户当前连续打卡天数
     */
    Integer getCurrentCheckInCount(Long userId);
}