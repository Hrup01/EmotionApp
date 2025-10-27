-- 拼图游戏表
CREATE TABLE IF NOT EXISTS puzzle_game (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    game_id VARCHAR(64) NOT NULL UNIQUE COMMENT '游戏唯一标识',
    theme VARCHAR(20) NOT NULL DEFAULT 'girl' COMMENT '游戏主题',
    difficulty VARCHAR(20) NOT NULL DEFAULT 'medium' COMMENT '难度等级',
    aspect_ratio VARCHAR(10) NOT NULL DEFAULT '1:1' COMMENT '图片比例',
    rows INT NOT NULL DEFAULT 4 COMMENT '拼图行数',
    cols INT NOT NULL DEFAULT 4 COMMENT '拼图列数',
    current_state TEXT NOT NULL COMMENT '当前游戏状态(JSON格式)',
    target_state TEXT NOT NULL COMMENT '目标状态(JSON格式)',
    moves INT NOT NULL DEFAULT 0 COMMENT '移动次数',
    time_spent INT NOT NULL DEFAULT 0 COMMENT '游戏用时(秒)',
    status VARCHAR(20) NOT NULL DEFAULT 'playing' COMMENT '游戏状态',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NULL COMMENT '结束时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_game_id (game_id),
    INDEX idx_theme_difficulty (theme, difficulty),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼图游戏表';

-- 拼图游戏排行榜表（用于缓存排行榜数据）
CREATE TABLE IF NOT EXISTS puzzle_leaderboard (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    theme VARCHAR(20) NOT NULL COMMENT '游戏主题',
    difficulty VARCHAR(20) NOT NULL COMMENT '难度等级',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    game_id VARCHAR(64) NOT NULL COMMENT '游戏ID',
    moves INT NOT NULL COMMENT '移动次数',
    time_spent INT NOT NULL COMMENT '游戏用时(秒)',
    rank_position INT NOT NULL COMMENT '排名位置',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_theme_difficulty (theme, difficulty),
    INDEX idx_rank_position (rank_position),
    UNIQUE KEY uk_theme_difficulty_user (theme, difficulty, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼图游戏排行榜表';
