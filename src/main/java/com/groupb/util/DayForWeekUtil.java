package com.groupb.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.groupb.mapper.WeeklyReportMapper;
import com.groupb.pojo.WeeklyReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * 解析当日与周报关系工具类
 */
@Component
@Slf4j
public class DayForWeekUtil {


    @Autowired
    private WeeklyReportMapper weeklyReportMapper;

    /**
     * 解析用户当日是否属于当前周报
     * @param localDate 当日时间YYYY:MM:DD
     * @return 周报,是--当前周报，否--null
     */
    public WeeklyReport getCurrentWeeklyReportId(LocalDate localDate,Long userId) {
        log.info("解析当日与周报关系");
        LambdaQueryWrapper<WeeklyReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(WeeklyReport::getCreateTime)
                .eq(WeeklyReport::getUserId, userId)
                .last("limit 1");//获取当前对应用户的周报

        Optional<WeeklyReport>weeklyReportOptional=Optional.ofNullable(weeklyReportMapper.selectOne(queryWrapper));
        //查询数据为空时直接走orElseGet方法，不为空则进行filter过滤判断，为空--orElseGet，不为空返回过滤值
        return weeklyReportOptional
                .filter(weeklyReport ->
                        dayInWeek(weeklyReport.getCreateTime().toLocalDate(),localDate))
                .orElseGet(()->{
                   log.info("数据库没有当日周报");
                   return null;
                });
    }

    /**
     * 解析当前周报
     * @param localDate 当前时间YYYY:MM:DD
     * @param userId 用户Id
     * @return 周报,有--周报，没有--null
     */
    public WeeklyReport getCurrentWeeklyReport(LocalDate localDate,Long userId){
        log.info("解析周报");
        LambdaQueryWrapper<WeeklyReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(WeeklyReport::getCreateTime)
                .eq(WeeklyReport::getUserId, userId)
                .last("limit 2");//获取当前对应用户的周报
        List<WeeklyReport> weeklyReports = weeklyReportMapper.selectList(queryWrapper);//查询出来俩条数据
        if (weeklyReports==null|| weeklyReports.isEmpty()) {
            log.error("周报不存在或为空");
            return null;
        }
        WeeklyReport weeklyReport = weeklyReports.get(0);
        if (weeklyReport==null) {
            log.error("周报不存在或为空");
            return null;
        }
        boolean b = dayInWeek(weeklyReport.getCreateTime().toLocalDate(), localDate);
        if (weeklyReports.size()>1) {
            if (b) {//是当前周报
                WeeklyReport weeklyReport1 = weeklyReports.get(1);
                if (weeklyReport1==null) {
                    log.error("周报不存在或为空");
                    return null;
                }
                return weeklyReport1;//周报存在
            }else{//不是当前周报
                return weeklyReport;
            }
        }else{
            log.info("没有第二条周报");
            return null;
        }

    }


    /**
     * 判断俩个时间段是否属于同一周 YYYY:MM:DD
     * @param localDate1 时间1
     * @param localDate2 时间2
     * @return Boolean值
     */
    public boolean dayInWeek(LocalDate localDate1,LocalDate localDate2){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        //获取当前周所属年份以及当前周为该年第几周进行判断
        return localDate1.get(weekFields.weekBasedYear()) == localDate2.get(weekFields.weekBasedYear())
                && localDate1.get(weekFields.weekOfWeekBasedYear()) == localDate2.get(weekFields.weekOfWeekBasedYear());
    }

}
