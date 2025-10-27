package com.groupb.pojo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 拼图游戏实体类
 */
@Data
public class PuzzleGame {
    private Long id;
    private Long userId;           // 用户ID
    private String gameId;         // 游戏唯一标识
    private String theme;          // 游戏主题 (girl, animal, sport)
    private String difficulty;     // 难度等级 (easy, medium, hard)
    private String aspectRatio;    // 图片比例 (1:1, 3:4, 4:3)
    private Integer rows;          // 拼图行数
    private Integer cols;          // 拼图列数
    private String currentState;   // 当前游戏状态 (JSON格式的4x4数组)
    private String targetState;    // 目标状态 (JSON格式的4x4数组)
    private Integer moves;         // 移动次数
    private Integer timeSpent;     // 游戏用时(秒)
    private String status;         // 游戏状态 (playing, completed, paused)
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
