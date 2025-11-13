package com.groupb.pojo.dto;

import lombok.Data;

/**
 * 拼图游戏移动操作DTO
 */
@Data
public class PuzzleMoveDTO {
    private String gameId;     // 游戏ID
    private String direction;  // 移动方向 (up, down, left, right)
    private int[][] newState;  // 移动后的新状态
    private int moves;         // 当前移动次数
    private int timeSpent;     // 当前用时
}










