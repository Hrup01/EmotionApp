package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.groupb.mapper.WeeklyReportMapper;
import com.groupb.pojo.WeeklyReport;
import com.groupb.service.DayEmotionRecordService;
import com.groupb.service.WeeklyReportService;
import com.groupb.util.AI.AIEmotionAdvice;
import com.groupb.util.DayForWeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
@Slf4j
public class WeeklyReportServiceImpl implements WeeklyReportService {

    @Autowired
    private WeeklyReportMapper weeklyReportMapper;

    @Autowired
    private DayForWeekUtil dayForWeekUtil;

    @Autowired
    private DayEmotionRecordService dayEmotionRecordService;

    @Autowired
    private AIEmotionAdvice aiEmotionAdvice;

    /**
     * 根据用户Id获取周报
     * @param userId 用户Id
     * @param localDate 当前时间
     * @return 周报
     */
    @Override
    public WeeklyReport getWeeklyReport(Long userId, LocalDate localDate) {
        log.info("获取周报");
        //基于不插入数据就不会生成下一个周报
        WeeklyReport currentWeeklyReport = dayForWeekUtil.getCurrentWeeklyReport(localDate, userId);
        if(currentWeeklyReport == null) {
            log.info("周报为空");
            return null;//没有周报时返回空
        }
        //生成周报
        if (currentWeeklyReport.getCount()==null) {//周报无数据需要生成
            return createWeeklyReport(currentWeeklyReport,localDate);
        }else{
            return currentWeeklyReport;//周报有数据无需生成
        }
    }

    /**
     * 创建空周报
     * @param userId 用户Id
     * @return 空周报
     */
    @Override
    public WeeklyReport createEmptyWeeklyReport(Long userId) {
        log.info("创建空周报");
        WeeklyReport weeklyReport = new WeeklyReport();
        weeklyReport.setUserId(userId);
        weeklyReport.setCreateTime(LocalDateTime.now());
        weeklyReportMapper.insert(weeklyReport);
        return weeklyReport;
    }

    /**
     * 保存完整周报
     * @param userId 用户Id
     * @param weeklyReportId 周报主键
     * @param weeklyReport 周报
     * @return 周报
     */
    @Override
    public WeeklyReport updateWeeklyReport(Long userId, Long weeklyReportId, WeeklyReport weeklyReport) {
        log.info("保存完整周报");
        LambdaUpdateWrapper<WeeklyReport> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WeeklyReport::getUserId, userId)
                .eq(WeeklyReport::getId, weeklyReportId);
        weeklyReportMapper.update(weeklyReport, updateWrapper);
        return weeklyReport;
    }

    //生成周报
    @Override
    public WeeklyReport createWeeklyReport(WeeklyReport currentWeeklyReport,LocalDate localDate) {
        log.info("生成周报");
        //查询出这个空周报
        Map<Integer, String> records= dayEmotionRecordService.getRecordsByWeeklyReportId(currentWeeklyReport.getId());
        //解析最大心情和打卡次数
        Map<String,Integer> map=new HashMap<>();
        AtomicLong count = new AtomicLong(0);//线程安全的原子Long
        records.forEach((k,v)->{
            if(!"未知".equals(v)) {
                count.incrementAndGet();//原子自增
                map.put(v,map.getOrDefault(v,0)+1);//添加进map
            }
        });
        Optional<Map.Entry<String, Integer>> maxEntry = map.entrySet()//获取全部的键值对
                .stream()
                .max(Map.Entry.comparingByValue());//获取值最大的一个键值对
        if (maxEntry.isPresent()) {
            Map.Entry<String, Integer> entry = maxEntry.get();//获取到这个键值对
            currentWeeklyReport.setMoreEmotion(entry.getKey());//封装进去
        } else {
            log.info("本周没有记录");
            currentWeeklyReport.setCount(-1L);//设置为-1，防止获取周报时重复生成周报
            updateWeeklyReport(currentWeeklyReport.getUserId(), currentWeeklyReport.getId(),currentWeeklyReport);//需要保存
            return null;
        }
        currentWeeklyReport.setCount(count.get());//封装打卡次数
        List<String> advice = aiEmotionAdvice.getAdvice(currentWeeklyReport.getUserId(),localDate);//就行情绪建议
        if (advice==null) {
            log.info("情绪建议为空");
            return null;
        }
        StringBuffer sb = new StringBuffer();
        advice.forEach(s->{
            sb.append(s);//拼接建议
            sb.append("#");//分隔符
        });
        currentWeeklyReport.setEmotionAdvice(sb.toString());
        updateWeeklyReport(currentWeeklyReport.getUserId(), currentWeeklyReport.getId(),currentWeeklyReport);//需要保存
        return currentWeeklyReport;
    }
}
