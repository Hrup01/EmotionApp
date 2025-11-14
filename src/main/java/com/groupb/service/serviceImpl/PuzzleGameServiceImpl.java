package com.groupb.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupb.mapper.PuzzleGameMapper;
import com.groupb.pojo.PuzzleGame;
import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.pojo.dto.PuzzleGameDTO;
import com.groupb.pojo.dto.PuzzleMoveDTO;
import com.groupb.pojo.dto.Result;
import com.groupb.service.PointsService;
import com.groupb.service.PuzzleGameService;
import com.groupb.util.SecurityContextUtil;
import com.groupb.pojo.PointsSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class PuzzleGameServiceImpl implements PuzzleGameService {

    private static final int DEFAULT_ROWS = 4;
    private static final int DEFAULT_COLS = 4;

    private final PuzzleGameMapper puzzleGameMapper;
    private final ObjectMapper objectMapper;
    private final PointsService pointsService;

    private static final int[][] TARGET_STATE_4X4 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    public PuzzleGameServiceImpl(PuzzleGameMapper puzzleGameMapper,
                                 ObjectMapper objectMapper,
                                 PointsService pointsService) {
        this.puzzleGameMapper = puzzleGameMapper;
        this.objectMapper = objectMapper;
        this.pointsService = pointsService;
    }

    @Override
    public Result<PuzzleGameDTO> createGame(String rank) {
        try {
            if (!isValidRank(rank)) {
                return Result.error("无效的关卡");
            }

            Long userId = SecurityContextUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }

            int[][] targetState = getTargetState(DEFAULT_ROWS, DEFAULT_COLS);
            int[][] initialState = generateSolvableState(DEFAULT_ROWS, DEFAULT_COLS);

            LocalDateTime now = LocalDateTime.now();
            PuzzleGame entity = new PuzzleGame();
            entity.setUserId(userId);
            entity.setGameId(UUID.randomUUID().toString());
            entity.setGameRank(rank);
            entity.setCurrentState(serializeState(initialState));
            entity.setTargetState(serializeState(targetState));
            entity.setMoves(0);
            entity.setTimeSpent(0);
            entity.setStatus("playing");
            entity.setStartTime(now);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            puzzleGameMapper.insert(entity);

            PuzzleGameDTO dto = toDto(entity, initialState, targetState);
            dto.setMessage("游戏创建成功");
            return Result.success(dto, "游戏创建成功");
        } catch (Exception e) {
            log.error("创建拼图游戏失败", e);
            return Result.error("创建游戏失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PuzzleGameDTO> getGame(String gameId) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            return Result.success(toDto(entity), "获取游戏状态成功");
        } catch (Exception e) {
            log.error("获取游戏状态失败", e);
            return Result.error("获取游戏状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PuzzleMoveDTO> makeMove(String gameId, String direction) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            if (!"playing".equalsIgnoreCase(entity.getStatus())) {
                return Result.error("游戏已结束或暂停");
            }

            int[][] currentState = deserializeState(entity.getCurrentState());
            int[][] newState = applyMove(currentState, direction);
            if (newState == null) {
                return Result.error("无效的移动操作");
            }

            entity.setMoves(entity.getMoves() + 1);
            entity.setTimeSpent(calculateTimeSpent(entity.getStartTime()));
            entity.setCurrentState(serializeState(newState));
            entity.setUpdateTime(LocalDateTime.now());

            boolean completed = isCompleted(newState);
            if (completed) {
                entity.setStatus("completed");
                entity.setEndTime(LocalDateTime.now());
            }

            puzzleGameMapper.updateById(entity);

            PuzzleMoveDTO moveDTO = new PuzzleMoveDTO();
            moveDTO.setGameId(gameId);
            moveDTO.setDirection(direction);
            moveDTO.setNewState(newState);
            moveDTO.setMoves(entity.getMoves());
            moveDTO.setTimeSpent(entity.getTimeSpent());
            return Result.success(moveDTO, completed ? "拼图完成" : "移动成功");
        } catch (Exception e) {
            log.error("执行拼图移动失败", e);
            return Result.error("移动失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> pauseGame(String gameId) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            entity.setStatus("paused");
            entity.setUpdateTime(LocalDateTime.now());
            puzzleGameMapper.updateById(entity);
            return Result.success("游戏已暂停", "暂停成功");
        } catch (Exception e) {
            log.error("暂停拼图游戏失败", e);
            return Result.error("暂停失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PuzzleGameDTO> resumeGame(String gameId) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            entity.setStatus("playing");
            entity.setUpdateTime(LocalDateTime.now());
            puzzleGameMapper.updateById(entity);
            return Result.success(toDto(entity), "游戏已恢复");
        } catch (Exception e) {
            log.error("恢复拼图游戏失败", e);
            return Result.error("恢复失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PuzzleGameDTO> completeGame(String gameId) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            int[][] currentState = deserializeState(entity.getCurrentState());
            if (!isCompleted(currentState)) {
                return Result.error("拼图尚未完成，无法结算");
            }
            entity.setStatus("completed");
            entity.setEndTime(LocalDateTime.now());
            entity.setTimeSpent(calculateTimeSpent(entity.getStartTime()));
            entity.setUpdateTime(LocalDateTime.now());
            puzzleGameMapper.updateById(entity);
            grantCompletionPoints(entity);
            return Result.success(toDto(entity, currentState, deserializeState(entity.getTargetState())), "游戏完成");
        } catch (Exception e) {
            log.error("完成拼图游戏失败", e);
            return Result.error("完成失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PuzzleGameDTO> debugSolveGame(String gameId) {
        try {
            PuzzleGame entity = puzzleGameMapper.findByGameId(gameId);
            if (entity == null) {
                return Result.error("游戏不存在");
            }
            int[][] targetState = deserializeState(entity.getTargetState());
            entity.setCurrentState(entity.getTargetState());
            entity.setStatus("completed");
            entity.setEndTime(LocalDateTime.now());
            entity.setTimeSpent(calculateTimeSpent(entity.getStartTime()));
            entity.setUpdateTime(LocalDateTime.now());
            puzzleGameMapper.updateById(entity);
            grantCompletionPoints(entity);
            PuzzleGameDTO dto = toDto(entity, targetState, targetState);
            dto.setMessage("已强制完成（调试接口）");
            return Result.success(dto, "已强制完成");
        } catch (Exception e) {
            log.error("调试强制完成拼图失败", e);
            return Result.error("调试完成失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<PuzzleGameDTO>> getGameHistory(Long userId) {
        try {
            Long resolvedUserId = userId != null ? userId : SecurityContextUtil.getCurrentUserId();
            if (resolvedUserId == null) {
                return Result.error("用户未登录");
            }
            List<PuzzleGame> games = puzzleGameMapper.findLatestByUser(resolvedUserId, 20);
            List<PuzzleGameDTO> dtos = new ArrayList<>();
            for (PuzzleGame game : games) {
                dtos.add(toDto(game));
            }
            return Result.success(dtos, "获取游戏历史成功");
        } catch (Exception e) {
            log.error("获取游戏历史失败", e);
            return Result.error("获取游戏历史失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<PuzzleGameDTO>> getLeaderboard(String theme, String difficulty) {
        return Result.success(new ArrayList<>(), "排行榜功能待实现");
    }

    private boolean isValidRank(String rank) {
        return "rank1".equalsIgnoreCase(rank);
    }

    private int[][] generateSolvableState(int rows, int cols) {
        int[][] state = generateRandomState(rows, cols);
        if (isSolvable(state)) {
            return state;
        }
        adjustToSolvable(state);
        if (!isSolvable(state)) {
            return generateSolvableState(rows, cols);
        }
        return state;
    }

    private int[][] generateRandomState(int rows, int cols) {
        int totalPieces = rows * cols;
        int[] tmp = new int[totalPieces];
        for (int i = 0; i < totalPieces; i++) {
            tmp[i] = i;
        }
        Random random = new Random();
        for (int i = 0; i < tmp.length; i++) {
            int randomIndex = random.nextInt(tmp.length);
            int swap = tmp[i];
            tmp[i] = tmp[randomIndex];
            tmp[randomIndex] = swap;
        }
        int[][] state = new int[rows][cols];
        for (int i = 0; i < tmp.length; i++) {
            state[i / cols][i % cols] = tmp[i];
        }
        return state;
    }

    private void adjustToSolvable(int[][] state) {
        int rows = state.length;
        int cols = state[0].length;
        int[] first = null;
        int[] second = null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (state[i][j] == 0) {
                    continue;
                }
                if (first == null) {
                    first = new int[]{i, j};
                } else {
                    second = new int[]{i, j};
                    break;
                }
            }
            if (second != null) {
                break;
            }
        }
        if (first != null && second != null) {
            swap(state, first[0], first[1], second[0], second[1]);
        }
    }

    private boolean isSolvable(int[][] state) {
        int rows = state.length;
        int cols = state[0].length;
        int total = rows * cols;
        int[] flattened = new int[total - 1];
        int idx = 0;
        int blankRow = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = state[i][j];
                if (value == 0) {
                    blankRow = i;
                    continue;
                }
                flattened[idx++] = value;
            }
        }
        int inversions = 0;
        for (int i = 0; i < flattened.length; i++) {
            for (int j = i + 1; j < flattened.length; j++) {
                if (flattened[i] > flattened[j]) {
                    inversions++;
                }
            }
        }
        if (cols % 2 == 1) {
            return inversions % 2 == 0;
        }
        int blankFromBottom = rows - blankRow;
        if (blankFromBottom % 2 == 0) {
            return inversions % 2 == 1;
        }
        return inversions % 2 == 0;
    }

    private int[][] getTargetState(int rows, int cols) {
        if (rows == DEFAULT_ROWS && cols == DEFAULT_COLS) {
            return copyState(TARGET_STATE_4X4);
        }
        int[][] target = new int[rows][cols];
        int num = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == rows - 1 && j == cols - 1) {
                    target[i][j] = 0;
                } else {
                    target[i][j] = num++;
                }
            }
        }
        return target;
    }

    private int[][] applyMove(int[][] state, String direction) {
        int[][] newState = copyState(state);
        int rows = state.length;
        int cols = state[0].length;
        int blankRow = -1;
        int blankCol = -1;
        outer:
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (newState[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outer;
                }
            }
        }
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
            default:
                return null;
        }
        return null;
    }

    private void swap(int[][] state, int row1, int col1, int row2, int col2) {
        int temp = state[row1][col1];
        state[row1][col1] = state[row2][col2];
        state[row2][col2] = temp;
    }

    private int[][] copyState(int[][] state) {
        int rows = state.length;
        int cols = state[0].length;
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(state[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    private boolean isCompleted(int[][] state) {
        int[][] target = getTargetState(state.length, state[0].length);
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] != target[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int calculateTimeSpent(LocalDateTime startTime) {
        if (startTime == null) {
            return 0;
        }
        return (int) Duration.between(startTime, LocalDateTime.now()).getSeconds();
    }

    private String serializeState(int[][] state) throws JsonProcessingException {
        return objectMapper.writeValueAsString(state);
    }

    private int[][] deserializeState(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, int[][].class);
    }

    private PuzzleGameDTO toDto(PuzzleGame entity) throws JsonProcessingException {
        int[][] current = deserializeState(entity.getCurrentState());
        int[][] target = deserializeState(entity.getTargetState());
        return toDto(entity, current, target);
    }

    private PuzzleGameDTO toDto(PuzzleGame entity, int[][] currentState, int[][] targetState) {
        PuzzleGameDTO dto = new PuzzleGameDTO();
        dto.setGameId(entity.getGameId());
        dto.setGameRank(entity.getGameRank());
        dto.setRows(DEFAULT_ROWS);
        dto.setCols(DEFAULT_COLS);
        dto.setCurrentState(currentState);
        dto.setTargetState(targetState);
        dto.setMoves(entity.getMoves());
        dto.setTimeSpent(entity.getTimeSpent());
        dto.setStatus(entity.getStatus());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setCompleted("completed".equalsIgnoreCase(entity.getStatus()));
        return dto;
    }

    private PointsChangeRequest buildPointsRequest(PuzzleGame entity) {
        PointsChangeRequest request = new PointsChangeRequest();
        request.setUserId(entity.getUserId());
        request.setSourceType(PointsSourceType.GAME_COMPLETE.name());
        request.setRequestedPoints(10);
        request.setBusinessId("PUZZLE-" + entity.getGameId());
        request.setRemark("拼图游戏完成奖励");
        request.setAllowClientOverride(false);
        return request;
    }

    private void grantCompletionPoints(PuzzleGame entity) {
        try {
            pointsService.changePoints(buildPointsRequest(entity));
            log.info("拼图完成积分发放成功: gameId={}, userId={}", entity.getGameId(), entity.getUserId());
        } catch (IllegalStateException ex) {
            log.warn("拼图完成积分已发放过: gameId={}, userId={}", entity.getGameId(), entity.getUserId());
        } catch (Exception ex) {
            log.error("拼图完成积分发放失败: gameId={}, userId={}", entity.getGameId(), entity.getUserId(), ex);
            throw ex;
        }
    }
}
