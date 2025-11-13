package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.groupb.mapper.AIChatHistoryMapper;
import com.groupb.pojo.AIChatHistory;
import com.groupb.service.AIChatHistoryService;
import com.groupb.util.AI.AIEmotionHistorySummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AIChatHistoryServiceImpl implements AIChatHistoryService {

    @Autowired
    private AIChatHistoryMapper aiChatHistoryMapper;

    @Autowired
    private AIEmotionHistorySummary aiEmotionHistorySummary;

    @Override
    public AIChatHistory saveChatHistory(Long userId,String chatId,String type) {
        AIChatHistory aiChatHistory = new AIChatHistory(userId, chatId, type,null);
        aiChatHistoryMapper.insert(aiChatHistory);
        log.info("会话历史保存成功");
        return aiChatHistory;
    }

    @Override
    public List<AIChatHistory> getChatHistory(Long userId, String type) {

        LambdaQueryWrapper<AIChatHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //条件查询userId和type都相同时查询所有记录
        lambdaQueryWrapper.eq(AIChatHistory::getUserId, userId).eq(AIChatHistory::getType, type);
        List<AIChatHistory> historyList=aiChatHistoryMapper.selectList(lambdaQueryWrapper);
        List<String> summary = aiEmotionHistorySummary.getSummary(userId);
        System.out.println("123");
        System.out.println(summary.size());

        int summaryIndex=0;//记录summary的下标
        log.info("添加总结");
        for(AIChatHistory history:historyList){
            if (history.getSummary()==null||history.getSummary().equals("空对话")) {
                history.setSummary(summary.get(summaryIndex));
                summaryIndex++;//下标前移
                //持久化
                aiChatHistoryMapper.updateById(history);
            }
        }
        log.info("获取会话历史成功");
        return historyList;
    }

    @Override
    public boolean deleteChatHistory(String type, String chatId) {
        LambdaUpdateWrapper<AIChatHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //非逻辑删除（直接删除）
        if(type==null||chatId==null){
            log.error("业务类型或chatId为空");
            return false;
        }
        lambdaUpdateWrapper.eq(AIChatHistory::getType, type).eq(AIChatHistory::getChatId, chatId);
        aiChatHistoryMapper.delete(lambdaUpdateWrapper);
        log.info("删除会话历史成功");
        return true;
    }

}
