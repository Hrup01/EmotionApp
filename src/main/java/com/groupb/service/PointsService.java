package com.groupb.service;

import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.pojo.dto.PointsChangeResponse;

public interface PointsService {

    /**
     * 统一的积分变更入口。
     *
     * @param request 积分变更请求
     * @return 积分变更结果
     */
    PointsChangeResponse changePoints(PointsChangeRequest request);
}


