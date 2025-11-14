package com.groupb.service.serviceImpl;

import com.groupb.pojo.PointsSourceType;
import com.groupb.mapper.PointsRecordMapper;
import com.groupb.mapper.UserMapper;
import com.groupb.pojo.PointsRecord;
import com.groupb.pojo.User;
import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.pojo.dto.PointsChangeResponse;
import com.groupb.service.PointsService;
import com.groupb.service.policy.PointsPolicyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class PointsServiceImpl implements PointsService {

    private final UserMapper userMapper;
    private final PointsRecordMapper pointsRecordMapper;
    private final PointsPolicyResolver pointsPolicyResolver;

    public PointsServiceImpl(UserMapper userMapper,
                             PointsRecordMapper pointsRecordMapper,
                             PointsPolicyResolver pointsPolicyResolver) {
        this.userMapper = userMapper;
        this.pointsRecordMapper = pointsRecordMapper;
        this.pointsPolicyResolver = pointsPolicyResolver;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointsChangeResponse changePoints(PointsChangeRequest request) {
        validateRequest(request);

        PointsSourceType sourceType = PointsSourceType.safeValueOf(request.getSourceType());
        if (sourceType == null) {
            throw new IllegalArgumentException("无法识别的积分来源类型：" + request.getSourceType());
        }

        if (request.getBusinessId() != null && pointsRecordMapper.countByBusiness(
                request.getUserId(), sourceType.name(), request.getBusinessId()) > 0) {
            throw new IllegalStateException("积分业务已处理，请勿重复提交");
        }

        User user = userMapper.findById(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在，userId=" + request.getUserId());
        }

        int delta = pointsPolicyResolver.resolve(sourceType, request);
        if (delta == 0) {
            log.info("积分变更为0，忽略处理。userId={}, source={}", request.getUserId(), sourceType);
            return buildResponse(user.getId(), sourceType, 0, user.getPoints(), null, request);
        }

        int newBalance = computeNewBalance(user.getPoints(), delta);

        LocalDateTime now = LocalDateTime.now();
        int updated = userMapper.updatePoints(user.getId(), newBalance, now);
        if (updated <= 0) {
            throw new IllegalStateException("更新用户积分失败，userId=" + user.getId());
        }

        PointsRecord record = null;
        if (!request.isSkipRecord()) {
            record = buildRecord(user.getId(), delta, newBalance, sourceType, request, now);
            pointsRecordMapper.insert(record);
        }

        return buildResponse(user.getId(), sourceType, delta, newBalance, record, request);
    }

    private void validateRequest(PointsChangeRequest request) {
        Objects.requireNonNull(request, "请求体不能为空");
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId不能为空");
        }
        if (request.getRequestedPoints() == null) {
            throw new IllegalArgumentException("requestedPoints不能为空");
        }
    }

    private int computeNewBalance(Integer currentPoints, int delta) {
        int current = currentPoints != null ? currentPoints : 0;
        long candidate = (long) current + delta;
        if (candidate < 0) {
            throw new IllegalArgumentException("积分不足，无法扣减。当前积分：" + current);
        }
        if (candidate > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("积分超过上限");
        }
        return (int) candidate;
    }

    private PointsRecord buildRecord(Long userId,
                                     int delta,
                                     int newBalance,
                                     PointsSourceType sourceType,
                                     PointsChangeRequest request,
                                     LocalDateTime now) {
        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setDelta(delta);
        record.setBalanceAfter(newBalance);
        record.setSourceType(sourceType.name());
        record.setBusinessId(request.getBusinessId());
        record.setRemark(request.getRemark());
        record.setCreatedAt(now);
        return record;
    }

    private PointsChangeResponse buildResponse(Long userId,
                                               PointsSourceType sourceType,
                                               int delta,
                                               int balance,
                                               PointsRecord record,
                                               PointsChangeRequest request) {
        PointsChangeResponse response = new PointsChangeResponse();
        response.setUserId(userId);
        response.setDelta(delta);
        response.setBalance(balance);
        response.setSourceType(sourceType.name());
        response.setBusinessId(request.getBusinessId());
        if (record != null) {
            response.setRecordId(record.getId());
        }
        return response;
    }
}


