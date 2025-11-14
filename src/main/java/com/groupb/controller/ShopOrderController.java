package com.groupb.controller;

import com.groupb.pojo.ShopOrder;
import com.groupb.pojo.dto.PointsChangeResponse;
import com.groupb.pojo.dto.Result;
import com.groupb.service.PointsService;
import com.groupb.service.ShopOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("shopOrder")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class ShopOrderController {

    @Autowired
    private ShopOrderService shopOrderService;

    /**
     * 创建订单--购买
     * @param shopOrder 订单
     * @return 订单
     */
    @PostMapping
    public Result<ShopOrder> createShopOrder(@RequestBody ShopOrder shopOrder) {
        PointsChangeResponse pointsChangeResponse = shopOrderService.changePointByOrder(shopOrder);//积分变更，创建流水
        if (pointsChangeResponse==null) {
            log.error("购买失败");
            return Result.error("购买失败");
        }
        ShopOrder createShopOrder = shopOrderService.createShopOrder(shopOrder);//创建订单
        if (createShopOrder==null) {
            log.error("购买失败");
            return Result.error("购买失败");
        }else{
            return Result.success(createShopOrder);
        }
    }

    /**
     * 获取用户全部订单
     * @param userId 用户Id
     * @return 全部订单
     */
    @GetMapping("{userId}")
    public Result<List<ShopOrder>> getUserShopOrder(@PathVariable Long userId) {
        List<ShopOrder> shopOrderByUserId = shopOrderService.getShopOrderByUserId(userId);
        if (shopOrderByUserId==null) {
            log.error("用户订单获取失败");
            return Result.error("用户订单获取失败");
        }else{
            return Result.success(shopOrderByUserId);
        }
    }

    /**
     * 更新订单--流水表不会变，但是可以根据唯一标识定位UUID
     * @param shopOrder 订单
     * @return 订单
     */
    @PostMapping("update")
    public Result<ShopOrder> updateShopOrder(@RequestBody ShopOrder shopOrder){
        ShopOrder shopOrder1 = shopOrderService.updateShopOrder(shopOrder);
        if (shopOrder1==null) {
            log.error("更新订单失败");
            return Result.error("更新订单失败");
        }else{
            return Result.success(shopOrder1);
        }
    }
}
