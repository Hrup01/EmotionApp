package com.groupb.service.serviceImpl;

import com.groupb.mapper.HandAccountMapper;
import com.groupb.pojo.HandAccount;
import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.service.HandAccountService;
import com.groupb.service.PointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandAccountServiceImpl implements HandAccountService {

    private final HandAccountMapper handAccountMapper;
    private final PointsService pointsService;

    @Override
    public HandAccount createHandAccount(Long userId, String imageUrl, String title, String remark) {
        HandAccount entity = new HandAccount();
        entity.setUserId(userId);
        entity.setImageUrl(imageUrl);
        entity.setTitle(title);
        entity.setRemark(remark);
        entity.setStatus(1);
        handAccountMapper.insertHandAccount(entity);

        grantHandAccountPoints(entity);

        log.info("用户 {} 上传手账图片成功, id={}, url={}", userId, entity.getId(), imageUrl);
        return entity;
    }

    private void grantHandAccountPoints(HandAccount entity) {
        try {
            PointsChangeRequest request = buildPointsRequest(entity);
            pointsService.changePoints(request);
            log.info("手账完成积分发放成功: handAccountId={}, userId={}", entity.getId(), entity.getUserId());
        } catch (IllegalStateException ex) {
            log.warn("手账完成积分已发放过: handAccountId={}, userId={}", entity.getId(), entity.getUserId());
        } catch (Exception ex) {
            log.error("手账完成积分发放失败: handAccountId={}, userId={}", entity.getId(), entity.getUserId(), ex);
            throw ex;
        }
    }

    private PointsChangeRequest buildPointsRequest(HandAccount entity) {
        PointsChangeRequest request = new PointsChangeRequest();
        request.setUserId(entity.getUserId());
        request.setSourceType(PointsSourceType.HAND_ACCOUNT.name());
        request.setRequestedPoints(10);
        request.setBusinessId("HAND-" + entity.getId());
        request.setRemark("完成一次手账奖励");
        request.setAllowClientOverride(false);
        return request;
    }
}
