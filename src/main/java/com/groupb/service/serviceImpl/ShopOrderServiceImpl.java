package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.groupb.mapper.ShopOrderMapper;
import com.groupb.pojo.PointsSourceType;
import com.groupb.pojo.ShopOrder;
import com.groupb.pojo.dto.PointsChangeRequest;
import com.groupb.pojo.dto.PointsChangeResponse;
import com.groupb.service.PointsService;
import com.groupb.service.ShopOrderService;
import jakarta.el.LambdaExpression;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShopOrderServiceImpl implements ShopOrderService {


    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private PointsService pointsService;

    //创建订单
    @Override
    public ShopOrder createShopOrder(ShopOrder shopOrder) {
        log.info("创建订单");
        int insert = shopOrderMapper.insert(shopOrder);//插入数据
        if(insert>0){
            log.info("创建成功");
            return shopOrder;
        }else{
            log.error("创建失败返回null");
            return null;
        }
    }

    //获取用户订单
    @Override
    public List<ShopOrder> getShopOrderByUserId(Long userId) {
        log.info("根据用户获取订单");
        if (userId==null) {
            log.error("用户不存在");
            return null;
        }
        LambdaQueryWrapper<ShopOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopOrder::getUserId, userId);
        return shopOrderMapper.selectList(wrapper);
    }

    //更新订单
    @Override
    public ShopOrder updateShopOrder(ShopOrder shopOrder) {
        log.info("更新订单");
        LambdaUpdateWrapper<ShopOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShopOrder::getId, shopOrder.getId());//唯一标识
        int update = shopOrderMapper.update(shopOrder, wrapper);
        if(update>0){
            log.info("更新成功");
            return shopOrder;
        }else{
            log.error("更新失败返回null");
            return null;
        }
    }

    //进行积分变更并且生成积分流水
    @Override
    public PointsChangeResponse changePointByOrder(ShopOrder shopOrder) {
        log.info("积分变更");
        return pointsService.changePoints(buildRequestByOrder(shopOrder));
    }

    //根据订单创建积分请求体
    public PointsChangeRequest buildRequestByOrder(ShopOrder shopOrder){
        log.info("根据订单创建积分请求体");
        PointsChangeRequest request = new PointsChangeRequest();
        request.setUserId(shopOrder.getUserId());
        request.setSourceType(PointsSourceType.COMPENSATION.name());//商城类型
        request.setRequestedPoints(-shopOrder.getPayPrice());
        request.setBusinessId(shopOrder.getId());
        request.setRemark("购买商品扣除积分");
        return request;
    }

}
