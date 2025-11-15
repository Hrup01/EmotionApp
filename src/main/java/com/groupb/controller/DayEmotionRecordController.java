package com.groupb.controller;

import com.groupb.pojo.DayEmotionRecord;
import com.groupb.pojo.WeeklyReport;
import com.groupb.pojo.dto.Result;
import com.groupb.service.DayEmotionRecordService;
import com.groupb.util.DayForWeekUtil;
import com.groupb.util.EmotionColorUtil;
import com.groupb.util.UserInformationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("dayEmotionRecord")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class DayEmotionRecordController {

    @Autowired
    private DayEmotionRecordService dayEmotionRecordService;

    @Autowired
    private UserInformationUtil userInformationUtil;

    @Autowired
    private DayForWeekUtil dayForWeekUtil;

    /**
     * 记录每日心情
     * @param emotion 心情
     * @return 完整的心情
     */
    @PostMapping
    public Result<DayEmotionRecord> recordEmotion(Authentication auth,String emotion) {
        Long userId = userInformationUtil.getCurrentUserId(auth);//获取用户Id
        if(userId == null){
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        if (emotion==null|| emotion.isEmpty()) {
            log.error("心情不能为空");
            return Result.error("心情为空");
        }
        DayEmotionRecord record = dayEmotionRecordService.createRecord(userId, emotion);
        return Result.success(record);
    }

    /**
     * 获取上周记录的心情
     * @param auth 认证
     * @return 上周心情
     */
    @GetMapping
    public Result<Map<Integer, EmotionColorUtil.EmotionInfo>> getRecordEmotionList(Authentication auth) {
        Long userId = userInformationUtil.getCurrentUserId(auth);//获取用户Id
        if(userId == null){
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        Map<Integer, EmotionColorUtil.EmotionInfo>resultMap=new HashMap<>();//存放结果
        //获取周报
        WeeklyReport currentWeeklyReport = dayForWeekUtil.getCurrentWeeklyReport(LocalDate.now(), userId);
        if (currentWeeklyReport==null) {
            log.error("本周没有周报");
            return Result.error("本周没有周报");
        }
        //获取本周心情
        Map<Integer, String> records = dayEmotionRecordService.getRecordsByWeeklyReportId(currentWeeklyReport.getId());
        records.forEach((k,v)->{
            resultMap.put(k,EmotionColorUtil.getEmotionInfo(v));//获取结果集
        });
        return Result.success(resultMap);
    }
}
