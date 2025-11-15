package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.groupb.mapper.DayEmotionRecordMapper;
import com.groupb.mapper.WeeklyReportMapper;
import com.groupb.pojo.DayEmotionRecord;
import com.groupb.pojo.WeeklyReport;
import com.groupb.service.DayEmotionRecordService;
import com.groupb.service.WeeklyReportService;
import com.groupb.util.DayForWeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DayEmotionRecordServiceImpl implements DayEmotionRecordService {

    @Autowired
    private WeeklyReportMapper weeklyReportMapper;

    @Autowired
    private DayEmotionRecordMapper dayEmotionRecordMapper;

    @Autowired
    private DayForWeekUtil dayForWeekUtil;

    /**
     * 创建每日记录
     * @param userId 用户Id
     * @param emotion 当日心情
     * @return 当日完整心情
     */
    @Override
    public DayEmotionRecord createRecord(Long userId,String emotion) {
        log.info("创建每日记录");
        LocalDate today = LocalDate.now();//获取当日的YYYY:MM:DD形式
        //获取当前是周几
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int dayValue = dayOfWeek.getValue(); //1--周一，2--周二....
        //解析当日属于周报的主键
        long weeklyReportId;
        Optional<WeeklyReport>weeklyReportOptional=Optional.ofNullable(dayForWeekUtil.getCurrentWeeklyReportId(today,userId));
        //如果为空，先新建一条周报记录，再获取周报主键--利用mybatis-plus的主键回显
        WeeklyReport weeklyReport=weeklyReportOptional.orElseGet(()->{
            WeeklyReport emptyWeeklyReport = new WeeklyReport();
            emptyWeeklyReport.setUserId(userId);
            emptyWeeklyReport.setCreateTime(LocalDateTime.now());
            weeklyReportMapper.insert(emptyWeeklyReport);//先插入
            return emptyWeeklyReport;
        });
        weeklyReportId=weeklyReport.getId();//获取主键
        //插入当日记录
        DayEmotionRecord dayEmotionRecord = new DayEmotionRecord();
        dayEmotionRecord.setWeeklyReportId(weeklyReportId);
        dayEmotionRecord.setDay(dayValue);
        dayEmotionRecord.setEmotion(emotion);
        dayEmotionRecordMapper.insert(dayEmotionRecord);
        return dayEmotionRecord;
    }


    /**
     * 查询上周每日记录
     * @param weeklyReportId 周报主键
     * @return 上周每日记录(空记录为null)
     */
    @Override
    public Map<Integer,String> getRecordsByWeeklyReportId(Long weeklyReportId) {
        log.info("查询本周每日记录");
        LambdaQueryWrapper<DayEmotionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DayEmotionRecord::getWeeklyReportId,weeklyReportId);
        List<DayEmotionRecord> dayEmotionRecords = dayEmotionRecordMapper.selectList(queryWrapper);
        log.info("对查询到的每日记录进行处理，空记录需要补充为null");
        Map<Integer,String>dayEmotionRecordMap=new HashMap<>();
        dayEmotionRecords.forEach(dayEmotionRecord->{
            dayEmotionRecordMap.put(dayEmotionRecord.getDay(),dayEmotionRecord.getEmotion());//全部映射到map里
        });
        log.info("有{}条每日记录数据",dayEmotionRecordMap.size());
        for (int i = 1; i <=7 ; i++) {
            dayEmotionRecordMap.putIfAbsent(i,"未知");//把空缺补齐
        }
        log.info("补齐后有{}条每日记录数据",dayEmotionRecordMap.size());
        return dayEmotionRecordMap;
    }

}
