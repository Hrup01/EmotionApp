package com.groupb.service;

import com.groupb.pojo.dto.PuzzleGameDTO;
import com.groupb.pojo.dto.PuzzleMoveDTO;
import com.groupb.pojo.dto.Result;

import java.util.List;

/**
 * 拼图游戏服务接口
 */
public interface PuzzleGameService {
    
    /**
     * 创建新游戏
     */
    Result<PuzzleGameDTO> createGame(String theme, String difficulty);
    
    /**
     * 创建新游戏（支持自定义比例）
     */
    Result<PuzzleGameDTO> createGame(String theme, String difficulty, String aspectRatio, int rows, int cols);
    
    /**
     * 获取游戏状态
     */
    Result<PuzzleGameDTO> getGame(String gameId);
    
    /**
     * 执行移动操作
     */
    Result<PuzzleMoveDTO> makeMove(String gameId, String direction);
    
    /**
     * 暂停游戏
     */
    Result<String> pauseGame(String gameId);
    
    /**
     * 恢复游戏
     */
    Result<PuzzleGameDTO> resumeGame(String gameId);
    
    /**
     * 完成游戏
     */
    Result<PuzzleGameDTO> completeGame(String gameId);
    
    /**
     * 获取用户游戏历史
     */
    Result<List<PuzzleGameDTO>> getGameHistory(Long userId);
    
    /**
     * 获取游戏排行榜
     */
    Result<List<PuzzleGameDTO>> getLeaderboard(String theme, String difficulty);
}
