package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 拼图游戏实体类
 */
@Data
@TableName("puzzle_game")
public class PuzzleGame {
    @TableId
    private Long id;
    private Long userId;           // 用户ID
    private String gameId;         // 游戏唯一标识
    private String gameRank;       // 游戏关卡
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
