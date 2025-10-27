package com.groupb.pojo.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 拼图游戏DTO
 */
@Data
public class PuzzleGameDTO {
    private String gameId;         // 游戏唯一标识
    private String theme;          // 游戏主题
    private String difficulty;     // 难度等级
    private String aspectRatio;    // 图片比例
    private int rows;              // 拼图行数
    private int cols;              // 拼图列数
    private int[][] currentState;  // 当前游戏状态
    private int[][] targetState;   // 目标状态
    private int moves;             // 移动次数
    private int timeSpent;         // 游戏用时(秒)
    private String status;         // 游戏状态
    private LocalDateTime startTime;
    private boolean isCompleted;   // 是否完成
    private String message;        // 提示信息
}
