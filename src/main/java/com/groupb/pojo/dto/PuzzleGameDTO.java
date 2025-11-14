package com.groupb.pojo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 拼图游戏DTO
 */
@Data
public class PuzzleGameDTO {
    private String gameId;         // 游戏唯一标识
    private String gameRank;       // 游戏关卡
    private int rows;              // 拼图行数
    private int cols;              // 拼图列数
    private int[][] currentState;  // 当前游戏状态
    private int[][] targetState;   // 目标状态
    private int moves;             // 移动次数
    private int timeSpent;         // 游戏用时(秒)
    private String status;         // 游戏状态
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;     // 是否完成
    private String message;// 提示信息


    // 新增：拼图图片块列表
    private List<PuzzlePiece> originalPieces;

    @Data
    public static class PuzzlePiece {
        private int originalIndex;
        private String url;
    }
}
