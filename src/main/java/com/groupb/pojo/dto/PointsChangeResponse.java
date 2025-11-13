package com.groupb.pojo.dto;

import lombok.Data;

/**
 * 积分变更响应结果，返回给调用方展示。
 */
@Data
public class PointsChangeResponse {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 实际变更值，可能与请求值不同（经过策略校准）
     */
    private Integer delta;
    /**
     * 变更后的积分余额
     */
    private Integer balance;
    /**
     * 积分来源类型
     */
    private String sourceType;
    /**
     * 积分流水记录ID
     */
    private Long recordId;
    /**
     * 业务标识
     */
    private String businessId;
}


