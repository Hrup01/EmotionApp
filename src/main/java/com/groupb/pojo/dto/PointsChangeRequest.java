package com.groupb.pojo.dto;

import com.groupb.pojo.PointsSourceType;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 积分变更请求入参。
 * 前端可传入建议的积分值，但后端仍会根据策略进行校验与兜底。
 */
@Data
public class PointsChangeRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 积分来源类型，使用 {@link PointsSourceType} 枚举
     */
    @NotNull(message = "sourceType不能为空")
    private String sourceType;

    /**
     * 前端建议的积分增量，可为负表示扣减
     */
    @NotNull(message = "requestedPoints不能为空")
    private Integer requestedPoints;

    /**
     * 业务方可选的外部业务编号，例如签到记录ID、游戏局ID等
     */
    private String businessId;

    /**
     * 备注信息，将进入积分流水记录
     */
    private String remark;

    /**
     * 是否允许前端建议值直接生效。
     * 若为false则由后端策略决定最终积分，默认为false。
     */
    private boolean allowClientOverride = false;

    /**
     * 是否忽略产生积分流水记录，默认false。
     * 一般不建议跳过流水，仅在特殊批处理场景使用。
     */
    private boolean skipRecord = false;
}


