package com.groupb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupb.pojo.PuzzleGame;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PuzzleGameMapper extends BaseMapper<PuzzleGame> {

    @Select("SELECT id, user_id AS userId, game_id AS gameId, game_rank AS gameRank, current_state AS currentState, " +
            "target_state AS targetState, moves, time_spent AS timeSpent, status, start_time AS startTime, " +
            "end_time AS endTime, create_time AS createTime, update_time AS updateTime " +
            "FROM puzzle_game WHERE game_id = #{gameId} LIMIT 1")
    PuzzleGame findByGameId(@Param("gameId") String gameId);

    @Select("SELECT id, user_id AS userId, game_id AS gameId, game_rank AS gameRank, current_state AS currentState, " +
            "target_state AS targetState, moves, time_spent AS timeSpent, status, start_time AS startTime, " +
            "end_time AS endTime, create_time AS createTime, update_time AS updateTime " +
            "FROM puzzle_game WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<PuzzleGame> findLatestByUser(@Param("userId") Long userId, @Param("limit") int limit);
}


