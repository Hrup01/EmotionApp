package com.groupb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.groupb.pojo.PointsRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PointsRecordMapper extends BaseMapper<PointsRecord> {

    @Insert("INSERT INTO points_records (user_id, delta, balance_after, source_type, business_id, remark, created_at) " +
            "VALUES (#{userId}, #{delta}, #{balanceAfter}, #{sourceType}, #{businessId}, #{remark}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PointsRecord record);

    @Select("SELECT id, user_id AS userId, delta, balance_after AS balanceAfter, source_type AS sourceType, " +
            "business_id AS businessId, remark, created_at AS createdAt " +
            "FROM points_records WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<PointsRecord> findLatestByUser(@Param("userId") long userId, @Param("limit") int limit);

    @Select("SELECT COUNT(1) FROM points_records WHERE user_id = #{userId} AND source_type = #{sourceType} " +
            "AND business_id = #{businessId}")
    int countByBusiness(@Param("userId") long userId,
                        @Param("sourceType") String sourceType,
                        @Param("businessId") String businessId);

    @Select("SELECT created_at FROM points_records WHERE user_id = #{userId} AND source_type = #{sourceType} " +
            "ORDER BY created_at DESC LIMIT 1")
    LocalDateTime findLastChangeTime(@Param("userId") long userId, @Param("sourceType") String sourceType);
}


