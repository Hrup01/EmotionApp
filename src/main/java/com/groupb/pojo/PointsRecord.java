package com.groupb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户积分流水记录。
 */
@Data
@TableName("points_records")
public class PointsRecord {
    @TableId(type = IdType.AUTO)
    private Long id;// 流水ID
    @TableField("user_id")
    private Long userId;// 用户ID
    private Integer delta;// 积分变更值
    @TableField("balance_after")
    private Integer balanceAfter;// 变更后积分余额
    @TableField("source_type")
    private String sourceType;// 积分来源
    @TableField("business_id")
    private String businessId;// 业务ID
    private String remark;// 备注
    @TableField("create_at")
    private LocalDateTime createdAt;// 创建时间
}


