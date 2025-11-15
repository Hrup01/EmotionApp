package com.groupb.service.policy;

import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.dto.PointsChangeRequest;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * 默认的积分策略实现，集中处理前端传入的积分值与后端配置的融合。
 */
@Component
public class DefaultPointsPolicyResolver implements PointsPolicyResolver {

    private static final int ABSOLUTE_MAX_DELTA = 1000;
    private static final int ABSOLUTE_MIN_DELTA = -1000;

    private final Map<PointsSourceType, Integer> defaultRewardTable = new EnumMap<>(PointsSourceType.class);

    public DefaultPointsPolicyResolver() {
        defaultRewardTable.put(PointsSourceType.GAME_COMPLETE, 10);
        defaultRewardTable.put(PointsSourceType.EMOTION_DIARY, 20);
        defaultRewardTable.put(PointsSourceType.COMMUNITY_POST, 20);
        defaultRewardTable.put(PointsSourceType.CONTINUOUS_CHECK_IN, 30);
        defaultRewardTable.put(PointsSourceType.DAILY_CHECK_IN, 10);
        defaultRewardTable.put(PointsSourceType.HAND_ACCOUNT, 10);
    }

    @Override
    public int resolve(PointsSourceType sourceType, PointsChangeRequest request) {
        Objects.requireNonNull(sourceType, "积分来源类型不能为空");
        Objects.requireNonNull(request, "请求体不能为空");

        int requested = safeRequestedPoints(request);

        Integer defaultReward = defaultRewardTable.get(sourceType);
        if (defaultReward != null && !request.isAllowClientOverride()) {
            return defaultReward;
        }

        if (request.isAllowClientOverride()) {
            return clamp(requested);
        }

        switch (sourceType) {
            case OPERATION_EVENT:
                return clamp(requested, -200, 200);
            case COMPENSATION:
                return clamp(requested, -5000, 5000);
            case CUSTOM:
                // 对于完全自定义的来源，必须显式允许客户端覆盖
                throw new IllegalArgumentException("CUSTOM 类型需要开启 allowClientOverride 以明确积分值");
            default:
                return clamp(requested);
        }
    }

    private int safeRequestedPoints(PointsChangeRequest request) {
        Integer requestedPoints = request.getRequestedPoints();
        if (requestedPoints == null) {
            throw new IllegalArgumentException("requestedPoints不能为空");
        }
        return requestedPoints;
    }

    private int clamp(int value) {
        return clamp(value, ABSOLUTE_MIN_DELTA, ABSOLUTE_MAX_DELTA);
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}


