package com.groupb.controller;

import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.pojo.dto.PointsChangeResponse;
import com.groupb.pojo.dto.Result;
import com.groupb.service.PointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分对外接口控制器，提供统一的积分增减入口。
 * 适用场景营手动补偿、人工扣除异常积分；
 * 数据修正（比如处理异常订单、作弊回滚）；
 * 开发/测试阶段快速验证积分逻辑。
 */
@Slf4j
@RestController
@RequestMapping("/api/points")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class PointsController {

    private final PointsService pointsService;

    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    /**
     * 积分变更接口。
     *
     * <p>前端传入积分来源、建议积分值等信息，后端统一校验后进行积分扣减或增加。</p>
     *
     * @param request 积分变更请求体
     * @return 积分变更结果（包含实际变更值与最新积分余额）
     */
    @PostMapping("/change")
    public Result<PointsChangeResponse> changePoints(@Validated @RequestBody PointsChangeRequest request) {
        try {
            PointsChangeResponse response = pointsService.changePoints(request);
            return Result.success(response, "积分变更成功");
        } catch (IllegalArgumentException ex) {
            log.warn("积分变更参数错误: {}", ex.getMessage());
            return Result.error(400, ex.getMessage());
        } catch (IllegalStateException ex) {
            log.warn("积分变更业务状态异常: {}", ex.getMessage());
            return Result.error(409, ex.getMessage());
        } catch (Exception ex) {
            log.error("积分变更失败", ex);
            return Result.error("积分变更失败，请稍后再试");
        }
    }
}


