package com.groupb.util.AI;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.groupb.mapper.DayEmotionRecordMapper;
import com.groupb.pojo.DayEmotionRecord;
import com.groupb.pojo.WeeklyReport;
import com.groupb.util.DayForWeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AI情绪建议
 */
@Component
@Slf4j
public class AIEmotionAdvice {

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private NoMemoryAIHelper noMemoryAIHelper;

    @Autowired
    private DayForWeekUtil dayForWeekUtil;

    @Autowired
    private DayEmotionRecordMapper dayEmotionRecordMapper;

    /**
     * 获取建议
     * @param userId 用户Id
     * @return 保存建议的链表
     */
    public List<String> getAdvice(Long userId,LocalDate localDate) {
        log.info("获取情绪建议");
        List<String> advice = new ArrayList<>();
        Map<String, String> aiDefaultPromprMap = NoMemoryAIHelper.getAIDefaultPromprMap();
        String s=aiDefaultPromprMap.get("情绪建议");//获取系统默认词
        ChatClient chatClient = noMemoryAIHelper.defaultChatClient(ollamaChatModel, s);
        WeeklyReport weeklyReport = dayForWeekUtil.getCurrentWeeklyReport(localDate, userId);//查询周报
        if (weeklyReport==null) {
            log.error("获取情绪建议失败，本周无周报");
            return null;
        }
        StringBuffer sb=new StringBuffer();//需要拼接心情
        //有周报则查询心情记录
        LambdaQueryWrapper<DayEmotionRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DayEmotionRecord::getWeeklyReportId,weeklyReport.getId());
        List<DayEmotionRecord> dayEmotionRecords = dayEmotionRecordMapper.selectList(queryWrapper);
        dayEmotionRecords.forEach(x->{//拼接好字符串
            sb.append(x.getEmotion());
            sb.append(" ");
        });
        for (int i = 0; i < 4; i++) {
            advice.add(noMemoryAIHelper.AIHelper(sb.toString(), chatClient));//总结心情
        }
        return advice;//返回建议
    }
}
