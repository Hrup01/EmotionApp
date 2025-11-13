package com.groupb.controller;

import com.groupb.pojo.dto.PuzzleGameDTO;
import com.groupb.pojo.dto.PuzzleMoveDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.PuzzleGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拼图游戏控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/puzzle")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class PuzzleGameController {
    
    @Autowired
    private PuzzleGameService puzzleGameService;
    
    /**
     * 创建新游戏
     * POST /api/puzzle/create
     * 只能使用静态资源中的图片，不能自定义切割
     * rank参数：rank1, rank2等，对应静态资源中的Rank1, Rank2目录
     */
    @PostMapping("/create")
    public Result<PuzzleGameDTO> createGame(
            @RequestParam(defaultValue = "rank1") String rank) {
        log.info("创建拼图游戏: rank={}", rank);
        return puzzleGameService.createGame(rank);
    }
    
    /**
     * 获取游戏状态
     * GET /api/puzzle/game/{gameId}
     */
    @GetMapping("/game/{gameId}")
    public Result<PuzzleGameDTO> getGame(@PathVariable String gameId) {
        log.info("获取拼图游戏状态: gameId={}", gameId);
        return puzzleGameService.getGame(gameId);
    }
    
    /**
     * 执行移动操作
     * POST /api/puzzle/move
     */
    @PostMapping("/move")
    public Result<PuzzleMoveDTO> makeMove(
            @RequestParam String gameId,
            @RequestParam String direction) {
        log.info("执行拼图移动: gameId={}, direction={}", gameId, direction);
        return puzzleGameService.makeMove(gameId, direction);
    }
    
    /**
     * 暂停游戏
     * POST /api/puzzle/pause
     */
    @PostMapping("/pause")
    public Result<String> pauseGame(@RequestParam String gameId) {
        log.info("暂停拼图游戏: gameId={}", gameId);
        return puzzleGameService.pauseGame(gameId);
    }
    
    /**
     * 恢复游戏
     * POST /api/puzzle/resume
     */
    @PostMapping("/resume")
    public Result<PuzzleGameDTO> resumeGame(@RequestParam String gameId) {
        log.info("恢复拼图游戏: gameId={}", gameId);
        return puzzleGameService.resumeGame(gameId);
    }
    
    /**
     * 完成游戏
     * POST /api/puzzle/complete
     */
    @PostMapping("/complete")
    public Result<PuzzleGameDTO> completeGame(@RequestParam String gameId) {
        log.info("完成拼图游戏: gameId={}", gameId);
        return puzzleGameService.completeGame(gameId);
    }
    
    /**
     * 获取游戏历史
     * GET /api/puzzle/history
     */
    @GetMapping("/history")
    public Result<List<PuzzleGameDTO>> getGameHistory() {
        log.info("获取拼图游戏历史");
        return puzzleGameService.getGameHistory(null); // 从SecurityContext获取用户ID
    }
    
    /**
     * 获取排行榜
     * GET /api/puzzle/leaderboard
     */
    @GetMapping("/leaderboard")
    public Result<List<PuzzleGameDTO>> getLeaderboard(
            @RequestParam(defaultValue = "girl") String theme,
            @RequestParam(defaultValue = "medium") String difficulty) {
        log.info("获取拼图游戏排行榜: theme={}, difficulty={}", theme, difficulty);
        return puzzleGameService.getLeaderboard(theme, difficulty);
    }
    
    /**
     * 获取可用的难度等级（rank列表）
     * GET /api/puzzle/ranks
     * 返回resources/image目录下可用的难度等级
     * 建议直接使用 /api/puzzle/image/ranks 接口获取
     */
    @GetMapping("/ranks")
    public Result<List<String>> getRanks() {
        // 目前只支持rank1
        List<String> ranks = List.of("rank1");
        return Result.success(ranks, "获取难度等级列表成功");
    }

    /**
     * 调试：强制完成游戏（仅测试使用）
     */
    @PostMapping("/debug/solve")
    public Result<PuzzleGameDTO> debugSolveGame(@RequestParam String gameId) {
        log.warn("[DEBUG] 强制完成拼图: gameId={}", gameId);
        return puzzleGameService.debugSolveGame(gameId);
    }
}
