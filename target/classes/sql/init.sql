-- 情绪日记应用数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS emotionApp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE emotionApp;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    phone VARCHAR(20) COMMENT '手机号（可选）',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    nickname VARCHAR(50) COMMENT '昵称',
    gender VARCHAR(10) COMMENT '性别（MALE/FEMALE/OTHER）',
    birthday DATE COMMENT '生日',
    points INT DEFAULT 0 COMMENT '积分',
    status ENUM('ONLINE', 'OFFLINE', 'DISABLED', 'DELETED') DEFAULT 'ONLINE' COMMENT '用户状态',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) COMMENT '用户表';

-- 为已存在的用户表添加新字段
-- 注意：如果字段已存在，执行以下语句会报错，需要手动判断或使用存储过程
-- 使用方法：如果表已存在，请先检查字段是否存在，然后再执行相应的ALTER TABLE语句
-- ALTER TABLE users ADD COLUMN nickname VARCHAR(50) COMMENT '昵称' AFTER avatar_url;
-- ALTER TABLE users ADD COLUMN gender VARCHAR(10) COMMENT '性别（MALE/FEMALE/OTHER）' AFTER nickname;
-- ALTER TABLE users ADD COLUMN birthday DATE COMMENT '生日' AFTER gender;
-- ALTER TABLE users ADD COLUMN points INT DEFAULT 0 COMMENT '积分' AFTER birthday;

-- 创建情绪日记表
CREATE TABLE IF NOT EXISTS emotion_diaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID（逻辑外键）',
    diary_date DATE NOT NULL COMMENT '日记日期',
    emotion_type VARCHAR(20) NOT NULL COMMENT '情绪类型：开心, 伤心, 自责, 晕, 邪恶, 生气, 困, 期待, 无奈, 疑问, 满足, 叹气',
    content TEXT COMMENT '日记内容',
    background_music VARCHAR(50) COMMENT '背景音乐/白噪音类型',
    mood_color VARCHAR(10) COMMENT '心情颜色',
    location VARCHAR(100) COMMENT '位置',
    check_in_count INT DEFAULT 0 COMMENT '连续打卡天数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_date (user_id, diary_date),
    INDEX idx_user_emotion (user_id, emotion_type),
    INDEX idx_diary_date (diary_date)
    -- 注意：这里使用逻辑外键，不设置物理外键约束
) COMMENT '情绪日记表';

-- 插入测试用户数据（密码为123456的BCrypt加密结果）
INSERT INTO users (username, password, phone, status, create_time, update_time) VALUES
('testuser1', '$2a$12$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '13800138000', 'ONLINE', NOW(), NOW()),
('testuser2', '$2a$12$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '13800138001', 'ONLINE', NOW(), NOW()),
('admin', '$2a$12$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '13800138002', 'ONLINE', NOW(), NOW())
ON DUPLICATE KEY UPDATE username=username;

-- 插入测试情绪日记数据（使用新的12种情绪类型，包含打卡数据）
INSERT INTO emotion_diaries (user_id, diary_date, emotion_type, content, background_music, mood_color, location, check_in_count) VALUES
(1, '2024-01-15', '开心', '今天心情很好！', 'Beach_64.m4a', '#FFB6C1', '北京', 1),
(1, '2024-01-16', '伤心', '今天有些忧郁...', 'splashing-rainfall160.mp3', '#87CEEB', '北京', 2),
(1, '2024-01-17', '满足', '今天比较平静，对现状很满足。', 'Forest 1_64.m4a', '#FF69B4', '北京', 3),
(1, '2024-01-18', '期待', '明天有重要的事情，很期待！', 'Cafe 1_64.m4a', '#FFD700', '北京', 4),
(1, '2024-01-19', '困', '工作了一天，感觉很困。', 'Thunder 2_64.m4a', '#9370DB', '北京', 5),
(1, '2024-01-20', '开心', '今天很开心！', 'Female conversation (English speech)_64.m4a', '#FFB6C1', '北京', 6),
(1, '2024-01-21', '疑问', '对某些事情有些疑问，需要思考。', 'Beach_64.m4a', '#FFA500', '北京', 7),
(1, '2024-01-22', '无奈', '遇到了一些困难，感到无奈。', 'splashing-rainfall160.mp3', '#4682B4', '北京', 8),
(1, '2024-01-23', '生气', '今天遇到了一些让人生气的事情。', 'Thunder 2_64.m4a', '#FF4500', '北京', 9),
(1, '2024-01-24', '自责', '觉得自己做得不够好，有些自责。', 'Forest 1_64.m4a', '#90EE90', '北京', 10)
ON DUPLICATE KEY UPDATE emotion_type=emotion_type;

-- 创建周报统计视图（可选）
CREATE OR REPLACE VIEW weekly_report_view AS
SELECT
    user_id,
    YEARWEEK(diary_date) as week_year,
    MIN(diary_date) as week_start,
    MAX(diary_date) as week_end,
    COUNT(*) as total_entries,
    GROUP_CONCAT(DISTINCT emotion_type) as emotion_types
FROM emotion_diaries
GROUP BY user_id, YEARWEEK(diary_date)
ORDER BY user_id, week_year DESC;