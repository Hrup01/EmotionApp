-- 打卡功能数据库更新脚本
-- 执行此脚本前请先备份数据库

USE emotionApp;

-- 1. 移除weather和tags字段，添加check_in_count字段
ALTER TABLE emotion_diaries 
DROP COLUMN IF EXISTS weather,
DROP COLUMN IF EXISTS tags,
ADD COLUMN check_in_count INT DEFAULT 0 COMMENT '连续打卡天数';

-- 2. 为现有数据计算打卡天数
-- 这里提供一个简单的示例，实际使用时可能需要更复杂的逻辑

-- 创建临时表来存储用户的打卡天数计算
CREATE TEMPORARY TABLE temp_check_in AS
SELECT 
    user_id,
    diary_date,
    ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY diary_date) as check_in_count
FROM emotion_diaries
ORDER BY user_id, diary_date;

-- 更新现有数据的打卡天数
UPDATE emotion_diaries ed
JOIN temp_check_in tci ON ed.user_id = tci.user_id AND ed.diary_date = tci.diary_date
SET ed.check_in_count = tci.check_in_count;

-- 清理临时表
DROP TEMPORARY TABLE temp_check_in;

-- 3. 验证更新结果
SELECT 
    user_id,
    diary_date,
    emotion_type,
    check_in_count,
    created_at
FROM emotion_diaries 
ORDER BY user_id, diary_date
LIMIT 10;

-- 4. 显示每个用户的打卡统计
SELECT 
    user_id,
    COUNT(*) as total_diaries,
    MAX(check_in_count) as max_check_in_count,
    MIN(diary_date) as first_diary_date,
    MAX(diary_date) as last_diary_date
FROM emotion_diaries 
GROUP BY user_id
ORDER BY user_id;
