package com.groupb.service.policy;

import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.dto.PointsChangeRequest;

/**
 * 积分策略解析器，用于统一决定最终的积分变更值。
 */
public interface PointsPolicyResolver {

    /**
     * 基于来源和请求信息返回最终的积分变更值。
     *
     * @param sourceType 来源类型
     * @param request    请求信息
     * @return 校验后的积分变更值
     */
    int resolve(PointsSourceType sourceType, PointsChangeRequest request);
}


