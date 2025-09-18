package com.groupb.mapper;

import com.groupb.pojo.EmotionDiary;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmotionDiaryMapper {

    @Insert("INSERT INTO emotion_diaries (user_id, diary_date, emotion_type, content, background_music, mood_color, location, check_in_count, created_at, updated_at) " +
            "VALUES (#{userId}, #{diaryDate}, #{emotionType}, #{content}, #{backgroundMusic}, #{moodColor}, #{location}, #{checkInCount}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EmotionDiary diary);

    @Update("UPDATE emotion_diaries SET emotion_type=#{emotionType}, content=#{content}, " +
            "background_music=#{backgroundMusic}, mood_color=#{moodColor}, location=#{location}, check_in_count=#{checkInCount}, updated_at=NOW() " +
            "WHERE id=#{id} AND user_id=#{userId}")
    int update(EmotionDiary diary);

    @Delete("DELETE FROM emotion_diaries WHERE id=#{id} AND user_id=#{userId}")
    int deleteById(Long id, Long userId);

    @Select("SELECT id, user_id as userId, diary_date as diaryDate, emotion_type as emotionType, " +
            "content, background_music as backgroundMusic, tags, mood_color as moodColor, location, check_in_count as checkInCount, created_at as createdAt, updated_at as updatedAt " +
            "FROM emotion_diaries WHERE user_id=#{userId} AND diary_date=#{date}")
    EmotionDiary findByUserAndDate(Long userId, LocalDate date);

    @Select("SELECT id, user_id as userId, diary_date as diaryDate, emotion_type as emotionType, " +
            "content, background_music as backgroundMusic, tags, mood_color as moodColor, location, check_in_count as checkInCount, created_at as createdAt, updated_at as updatedAt " +
            "FROM emotion_diaries WHERE user_id=#{userId} AND diary_date BETWEEN #{start} AND #{end} ORDER BY diary_date DESC")
    List<EmotionDiary> findByUserBetween(Long userId, LocalDate start, LocalDate end);

    @Select("SELECT id, user_id as userId, diary_date as diaryDate, emotion_type as emotionType, " +
            "content, background_music as backgroundMusic, tags, mood_color as moodColor, location, check_in_count as checkInCount, created_at as createdAt, updated_at as updatedAt " +
            "FROM emotion_diaries WHERE user_id=#{userId} ORDER BY diary_date DESC LIMIT #{limit}")
    List<EmotionDiary> findRecentByUser(Long userId, Integer limit);

    @Select("SELECT COUNT(*) FROM emotion_diaries WHERE user_id=#{userId} AND diary_date BETWEEN #{start} AND #{end}")
    long countByUserBetween(Long userId, LocalDate start, LocalDate end);

    @Select("SELECT id, user_id as userId, diary_date as diaryDate, emotion_type as emotionType, " +
            "content, background_music as backgroundMusic, tags, mood_color as moodColor, location, check_in_count as checkInCount, created_at as createdAt, updated_at as updatedAt " +
            "FROM emotion_diaries WHERE id=#{id}")
    EmotionDiary findById(Long id);

    @Select("SELECT emotion_type, COUNT(*) as count FROM emotion_diaries WHERE user_id=#{userId} AND diary_date BETWEEN #{start} AND #{end} GROUP BY emotion_type")
    List<Map<String, Object>> getEmotionTypeStats(Long userId, LocalDate start, LocalDate end);

    // 获取用户最后一次打卡的日期
    @Select("SELECT MAX(diary_date) FROM emotion_diaries WHERE user_id=#{userId}")
    LocalDate getLastCheckInDate(Long userId);

    // 获取用户连续打卡天数
    @Select("SELECT check_in_count FROM emotion_diaries WHERE user_id=#{userId} ORDER BY diary_date DESC LIMIT 1")
    Integer getLastCheckInCount(Long userId);
}




