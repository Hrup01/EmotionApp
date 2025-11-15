package com.groupb.service;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.groupb.pojo.DayEmotionRecord;

import java.util.List;
import java.util.Map;

/**
 * 每日心情服务接口
 */
public interface DayEmotionRecordService {

    /**
     * 创建每日记录
     * @param userId 用户Id
     * @param emotion 当日心情
     * @return 当日完整心情
     */
    DayEmotionRecord createRecord(Long userId, String emotion);

    /**
     * 查询本周每日记录
     * @param weeklyReportId 周报主键
     * @return 本周每日记录
     */
    Map<Integer,String> getRecordsByWeeklyReportId(Long weeklyReportId);



}
