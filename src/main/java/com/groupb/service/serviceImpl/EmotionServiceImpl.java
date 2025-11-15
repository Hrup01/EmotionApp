package com.groupb.service.serviceImpl;

import com.groupb.mapper.EmotionDiaryMapper;
import com.groupb.pojo.EmotionDiary;
import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.dto.EmotionDTO;
import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.service.EmotionService;
import com.groupb.service.PointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmotionServiceImpl implements EmotionService {

    private final EmotionDiaryMapper emotionDiaryMapper;
    private final PointsService pointsService;

    public EmotionServiceImpl(EmotionDiaryMapper emotionDiaryMapper, PointsService pointsService) {
        this.emotionDiaryMapper = emotionDiaryMapper;
        this.pointsService = pointsService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDiary(Long userId, EmotionDTO dto) {
        EmotionDiary diary = new EmotionDiary();
        BeanUtils.copyProperties(dto, diary);
        diary.setUserId(userId);
        
        // 如果日期为空，设置为今天
        if (diary.getDiaryDate() == null) {
            diary.setDiaryDate(LocalDate.now());
        }
        
        // 检查是否已存在该日期的日记
        EmotionDiary existingDiary = emotionDiaryMapper.findByUserAndDate(userId, diary.getDiaryDate());
        boolean created = false;
        
        if (existingDiary != null) {
            // 更新现有日记，保持原有的打卡天数
            diary.setId(existingDiary.getId());
            diary.setCheckInCount(existingDiary.getCheckInCount());
            emotionDiaryMapper.update(diary);
        } else {
            // 插入新日记，计算打卡天数
            int checkInCount = calculateCheckInCount(userId, diary.getDiaryDate());
            diary.setCheckInCount(checkInCount);
            emotionDiaryMapper.insert(diary);
            created = true;
            grantDiaryRewards(userId, diary.getDiaryDate(), checkInCount);
        }

        return created;
    }

    @Override
    public EmotionDTO getDiaryByDate(Long userId, LocalDate date) {
        EmotionDiary diary = emotionDiaryMapper.findByUserAndDate(userId, date);
        if (diary == null) {
            return null;
        }
        
        EmotionDTO dto = new EmotionDTO();
        BeanUtils.copyProperties(diary, dto);
        return dto;
    }

    @Override
    public List<EmotionDTO> getRecentDiaries(Long userId, Integer limit) {
        List<EmotionDiary> diaries = emotionDiaryMapper.findRecentByUser(userId, limit);
        return diaries.stream()
                .map(diary -> {
                    EmotionDTO dto = new EmotionDTO();
                    BeanUtils.copyProperties(diary, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteDiaryById(Long userId, Long diaryId) {
        // 先检查日记是否存在且属于该用户
        EmotionDiary diary = emotionDiaryMapper.findById(diaryId);
        if (diary == null || !diary.getUserId().equals(userId)) {
            return false;
        }
        
        emotionDiaryMapper.deleteById(diaryId, userId);
        return true;
    }
    
    @Override
    public boolean deleteDiaryByDate(Long userId, LocalDate date) {
        log.info("尝试删除用户 {} 在日期 {} 的日记", userId, date);
        
        // 先检查该日期是否有日记
        EmotionDiary diary = emotionDiaryMapper.findByUserAndDate(userId, date);
        if (diary == null) {
            log.warn("用户 {} 在日期 {} 没有找到日记", userId, date);
            return false;
        }
        
        log.info("找到日记 ID: {}, 用户ID: {}, 日期: {}", diary.getId(), diary.getUserId(), diary.getDiaryDate());
        
        emotionDiaryMapper.deleteById(diary.getId(), userId);
        return true;
    }

    /*
     * 周报功能 - 设计组构思中，暂时注释
     * 
    @Override
    public WeeklyReportDTO getWeeklyReport(Long userId, LocalDate weekStart) {
        // 周报功能实现代码已注释
        return null;
    }
    */


    @Override
    public List<EmotionDTO> getDiariesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<EmotionDiary> diaries = emotionDiaryMapper.findByUserBetween(userId, startDate, endDate);
        return diaries.stream()
                .map(diary -> {
                    EmotionDTO dto = new EmotionDTO();
                    BeanUtils.copyProperties(diary, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCurrentCheckInCount(Long userId) {
        Integer lastCheckInCount = emotionDiaryMapper.getLastCheckInCount(userId);
        return lastCheckInCount != null ? lastCheckInCount : 0;
    }

    /**
     * 计算打卡天数
     * 如果中间有断档，则从1开始重新计算
     */
    private int calculateCheckInCount(Long userId, LocalDate currentDate) {
        // 获取最后一次打卡的日期
        LocalDate lastCheckInDate = emotionDiaryMapper.getLastCheckInDate(userId);
        
        if (lastCheckInDate == null) {
            // 第一次打卡
            return 1;
        }
        
        // 计算日期差
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastCheckInDate, currentDate);
        
        if (daysBetween == 1) {
            // 连续打卡，获取上次的打卡天数并加1
            Integer lastCount = emotionDiaryMapper.getLastCheckInCount(userId);
            return (lastCount != null ? lastCount : 0) + 1;
        } else {
            // 断档了，重新从1开始
            return 1;
        }
    }

    private void grantDiaryRewards(Long userId, LocalDate diaryDate, int checkInCount) {
        PointsChangeRequest diaryReward = buildPointsRequest(
                userId,
                PointsSourceType.EMOTION_DIARY,
                20,
                "DIARY-" + diaryDate,
                "情绪日记打卡奖励",
                false
        );
        pointsService.changePoints(diaryReward);

        int dayInCycle = ((checkInCount - 1) % 7) + 1;
        PointsChangeRequest dailyCheckInReward = buildPointsRequest(
                userId,
                PointsSourceType.DAILY_CHECK_IN,
                dayInCycle,
                "CHECKIN-DAILY-" + diaryDate,
                "每日打卡奖励",
                true
        );
        pointsService.changePoints(dailyCheckInReward);
    }

    private PointsChangeRequest buildPointsRequest(Long userId,
                                                   PointsSourceType sourceType,
                                                   int requestedPoints,
                                                   String businessId,
                                                   String remark,
                                                   boolean allowOverride) {
        PointsChangeRequest request = new PointsChangeRequest();
        request.setUserId(userId);
        request.setSourceType(sourceType.name());
        request.setRequestedPoints(requestedPoints);
        request.setBusinessId(businessId);
        request.setRemark(remark);
        request.setAllowClientOverride(allowOverride);
        return request;
    }
}