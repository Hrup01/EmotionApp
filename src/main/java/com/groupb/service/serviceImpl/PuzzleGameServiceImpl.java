package com.groupb.service.serviceImpl;

import com.groupb.pojo.dto.PuzzleGameDTO;
import com.groupb.pojo.dto.PuzzleMoveDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.PuzzleGameService;
import com.groupb.util.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 拼图游戏服务实现类
 */
@Slf4j
@Service
public class PuzzleGameServiceImpl implements PuzzleGameService {
    
    // 内存中存储游戏状态（生产环境建议使用Redis）
    private Map<String, PuzzleGameDTO> gameCache = new HashMap<>();
    
    // 目标状态（4x4拼图）
    private final int[][] TARGET_STATE_4X4 = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12},
        {13, 14, 15, 0}
    };
    
    // 目标状态（3x4拼图）
    private final int[][] TARGET_STATE_3X4 = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 0}
    };
    
    // 目标状态（4x3拼图）
    private final int[][] TARGET_STATE_4X3 = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9},
        {10, 11, 0}
    };
    
    @Override
    public Result<PuzzleGameDTO> createGame(String theme, String difficulty) {
        return createGame(theme, difficulty, "1:1", 4, 4);
    }
    
    /**
     * 创建新游戏（支持自定义比例）
     */
    public Result<PuzzleGameDTO> createGame(String theme, String difficulty, String aspectRatio, int rows, int cols) {
        try {
            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            
            String gameId = UUID.randomUUID().toString();
            int[][] initialState = generateRandomState(rows, cols);
            int[][] targetState = getTargetState(rows, cols);
            
            PuzzleGameDTO game = new PuzzleGameDTO();
            game.setGameId(gameId);
            game.setTheme(theme);
            game.setDifficulty(difficulty);
            game.setAspectRatio(aspectRatio);
            game.setRows(rows);
            game.setCols(cols);
            game.setCurrentState(initialState);
            game.setTargetState(targetState);
            game.setMoves(0);
            game.setTimeSpent(0);
            game.setStatus("playing");
            game.setStartTime(LocalDateTime.now());
            game.setCompleted(false);
            
            // 存储到缓存
            gameCache.put(gameId, game);
            
            log.info("创建新拼图游戏: gameId={}, userId={}, theme={}, difficulty={}", 
                    gameId, userId, theme, difficulty);
            
            return Result.success(game, "游戏创建成功");
            
        } catch (Exception e) {
            log.error("创建拼图游戏失败", e);
            return Result.error("创建游戏失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PuzzleGameDTO> getGame(String gameId) {
        try {
            PuzzleGameDTO game = gameCache.get(gameId);
            if (game == null) {
                return Result.error("游戏不存在");
            }
            
            return Result.success(game, "获取游戏状态成功");
            
        } catch (Exception e) {
            log.error("获取游戏状态失败", e);
            return Result.error("获取游戏状态失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PuzzleMoveDTO> makeMove(String gameId, String direction) {
        try {
            PuzzleGameDTO game = gameCache.get(gameId);
            if (game == null) {
                return Result.error("游戏不存在");
            }
            
            if (!"playing".equals(game.getStatus())) {
                return Result.error("游戏已结束或暂停");
            }
            
            int[][] currentState = game.getCurrentState();
            int[][] newState = makeMove(currentState, direction);
            
            if (newState == null) {
                return Result.error("无效的移动操作");
            }
            
            // 更新游戏状态
            game.setCurrentState(newState);
            game.setMoves(game.getMoves() + 1);
            game.setTimeSpent(calculateTimeSpent(game.getStartTime()));
            
            // 检查是否完成
            if (isCompleted(newState)) {
                game.setStatus("completed");
                game.setCompleted(true);
                log.info("拼图游戏完成: gameId={}, moves={}, timeSpent={}", 
                        gameId, game.getMoves(), game.getTimeSpent());
            }
            
            // 更新缓存
            gameCache.put(gameId, game);
            
            PuzzleMoveDTO moveResult = new PuzzleMoveDTO();
            moveResult.setGameId(gameId);
            moveResult.setDirection(direction);
            moveResult.setNewState(newState);
            moveResult.setMoves(game.getMoves());
            moveResult.setTimeSpent(game.getTimeSpent());
            
            return Result.success(moveResult, "移动成功");
            
        } catch (Exception e) {
            log.error("执行移动操作失败", e);
            return Result.error("移动失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<String> pauseGame(String gameId) {
        try {
            PuzzleGameDTO game = gameCache.get(gameId);
            if (game == null) {
                return Result.error("游戏不存在");
            }
            
            game.setStatus("paused");
            gameCache.put(gameId, game);
            
            return Result.success("游戏已暂停", "暂停成功");
            
        } catch (Exception e) {
            log.error("暂停游戏失败", e);
            return Result.error("暂停失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PuzzleGameDTO> resumeGame(String gameId) {
        try {
            PuzzleGameDTO game = gameCache.get(gameId);
            if (game == null) {
                return Result.error("游戏不存在");
            }
            
            game.setStatus("playing");
            gameCache.put(gameId, game);
            
            return Result.success(game, "游戏已恢复");
            
        } catch (Exception e) {
            log.error("恢复游戏失败", e);
            return Result.error("恢复失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PuzzleGameDTO> completeGame(String gameId) {
        try {
            PuzzleGameDTO game = gameCache.get(gameId);
            if (game == null) {
                return Result.error("游戏不存在");
            }
            
            game.setStatus("completed");
            game.setCompleted(true);
            gameCache.put(gameId, game);
            
            return Result.success(game, "游戏完成");
            
        } catch (Exception e) {
            log.error("完成游戏失败", e);
            return Result.error("完成失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<List<PuzzleGameDTO>> getGameHistory(Long userId) {
        // TODO: 实现从数据库获取游戏历史
        return Result.success(new ArrayList<>(), "获取游戏历史成功");
    }
    
    @Override
    public Result<List<PuzzleGameDTO>> getLeaderboard(String theme, String difficulty) {
        // TODO: 实现排行榜功能
        return Result.success(new ArrayList<>(), "获取排行榜成功");
    }
    
    /**
     * 生成随机初始状态
     */
    private int[][] generateRandomState(int rows, int cols) {
        int totalPieces = rows * cols;
        int[] tempArr = new int[totalPieces];
        
        // 初始化数组
        for (int i = 0; i < totalPieces; i++) {
            tempArr[i] = i;
        }
        
        Random random = new Random();
        
        // 打乱数组
        for (int i = 0; i < tempArr.length; i++) {
            int randomIndex = random.nextInt(tempArr.length);
            int temp = tempArr[randomIndex];
            tempArr[randomIndex] = tempArr[i];
            tempArr[i] = temp;
        }
        
        // 转换为二维数组
        int[][] state = new int[rows][cols];
        for (int i = 0; i < tempArr.length; i++) {
            state[i / cols][i % cols] = tempArr[i];
        }
        
        return state;
    }
    
    /**
     * 获取目标状态
     */
    private int[][] getTargetState(int rows, int cols) {
        if (rows == 4 && cols == 4) {
            return copyState(TARGET_STATE_4X4);
        } else if (rows == 3 && cols == 4) {
            return copyState(TARGET_STATE_3X4);
        } else if (rows == 4 && cols == 3) {
            return copyState(TARGET_STATE_4X3);
        } else {
            // 动态生成目标状态
            int[][] target = new int[rows][cols];
            int num = 1;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (i == rows - 1 && j == cols - 1) {
                        target[i][j] = 0; // 空白块在右下角
                    } else {
                        target[i][j] = num++;
                    }
                }
            }
            return target;
        }
    }
    
    /**
     * 执行移动操作
     */
    private int[][] makeMove(int[][] state, String direction) {
        int[][] newState = copyState(state);
        int rows = state.length;
        int cols = state[0].length;
        
        // 找到空白块位置
        int blankRow = -1, blankCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (newState[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }
            }
        }
        
        // 根据方向移动
        switch (direction.toLowerCase()) {
            case "up":
                if (blankRow < rows - 1) {
                    swap(newState, blankRow, blankCol, blankRow + 1, blankCol);
                    return newState;
                }
                break;
            case "down":
                if (blankRow > 0) {
                    swap(newState, blankRow, blankCol, blankRow - 1, blankCol);
                    return newState;
                }
                break;
            case "left":
                if (blankCol < cols - 1) {
                    swap(newState, blankRow, blankCol, blankRow, blankCol + 1);
                    return newState;
                }
                break;
            case "right":
                if (blankCol > 0) {
                    swap(newState, blankRow, blankCol, blankRow, blankCol - 1);
                    return newState;
                }
                break;
        }
        
        return null; // 无效移动
    }
    
    /**
     * 交换两个位置的值
     */
    private void swap(int[][] state, int row1, int col1, int row2, int col2) {
        int temp = state[row1][col1];
        state[row1][col1] = state[row2][col2];
        state[row2][col2] = temp;
    }
    
    /**
     * 复制状态数组
     */
    private int[][] copyState(int[][] state) {
        int rows = state.length;
        int cols = state[0].length;
        int[][] newState = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, cols);
        }
        return newState;
    }
    
    /**
     * 检查是否完成
     */
    private boolean isCompleted(int[][] state) {
        int rows = state.length;
        int cols = state[0].length;
        
        // 动态生成目标状态进行比较
        int[][] targetState = getTargetState(rows, cols);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (state[i][j] != targetState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 计算游戏用时
     */
    private int calculateTimeSpent(LocalDateTime startTime) {
        return (int) java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
    }
}
