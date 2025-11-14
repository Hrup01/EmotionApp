package com.groupb.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.ShopOrder;
import com.groupb.pojo.dto.PointsChangeResponse;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务接口
 */
public interface ShopOrderService {


    /**
     * 创建订单
     * @param shopOrder 订单
     * @return 订单
     */
    ShopOrder createShopOrder(ShopOrder shopOrder);

    /**
     * 根据用户Id查询他的订单
     * @param userId 用户Id
     * @return 用户所有订单
     */
    List<ShopOrder> getShopOrderByUserId(Long userId);

    /**
     * 修改订单
     * @param shopOrder 订单
     * @return 订单
     */
    ShopOrder updateShopOrder(ShopOrder shopOrder);


    /**
     * 商城的积分变更（通过订单信息）
     * @param shopOrder 订单
     * @return 积分变更返回体
     */
    PointsChangeResponse changePointByOrder(ShopOrder shopOrder);


}
