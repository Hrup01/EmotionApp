package com.groupb.service;

import com.groupb.pojo.WeeklyReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 周报生成服务接口
 */
public interface WeeklyReportService {

    /**
     * 根据用户Id获取周报
     * @param userId 用户Id
     * @param localDate 当前时间
     * @return 周报
     */
    WeeklyReport getWeeklyReport(Long userId, LocalDate localDate);


    /**
     * 根据用户Id创建空周报
     * @param userId 用户Id
     * @return 空周报
     */
    WeeklyReport createEmptyWeeklyReport(Long userId);


    /**
     * 保存完整周报
     * @param userId 用户Id
     * @param weeklyReportId 周报主键
     * @param weeklyReport 周报
     * @return 完整周报
     */
    WeeklyReport updateWeeklyReport(Long userId,Long weeklyReportId, WeeklyReport weeklyReport);

    /**
     * 生成周报
     * @param weeklyReport 空周报
     * @return 完整周报
     */
    WeeklyReport createWeeklyReport(WeeklyReport weeklyReport,LocalDate localDate);

}
