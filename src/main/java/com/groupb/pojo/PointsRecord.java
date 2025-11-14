package com.groupb.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户积分流水记录。
 */
@Data
public class PointsRecord {
    private Long id;// 流水ID
    private Long userId;// 用户ID
    private Integer delta;// 积分变更值
    private Integer balanceAfter;// 变更后积分余额
    private String sourceType;// 积分来源
    private String businessId;// 业务ID
    private String remark;// 备注
    private LocalDateTime createdAt;// 创建时间
}


