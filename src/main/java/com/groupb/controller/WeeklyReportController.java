package com.groupb.controller;

import com.groupb.pojo.WeeklyReport;
import com.groupb.pojo.dto.Result;
import com.groupb.service.WeeklyReportService;
import com.groupb.util.UserInformationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("weeklyReport")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class WeeklyReportController {

    @Autowired
    private WeeklyReportService weeklyReportService;

    @Autowired
    private UserInformationUtil userInformationUtil;

    /**
     * 获取周报
     * @param auth 认证
     * @return 周报
     */
    @GetMapping
    public Result<WeeklyReport> getWeeklyReport(Authentication auth) {
        Long userId = userInformationUtil.getCurrentUserId(auth);//获取用户Id
        if(userId == null){
            log.error("未获取用户信息");
            return Result.error("未获取用户信息");
        }
        //获取周报
        WeeklyReport weeklyReport = weeklyReportService.getWeeklyReport(userId, LocalDate.now());
        if (weeklyReport==null) {
            log.error("获取周报失败");
            return Result.error("获取周报失败");
        }
        return Result.success(weeklyReport);
    }
}
