package com.groupb.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.groupb.mapper.UserMapper;
import com.groupb.pojo.ShopOrder;
import com.groupb.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 检测积分工具类
 */
@Component
@Slf4j
public class CheckPointsUtil {

    @Autowired
    private UserMapper userMapper;

    public boolean checkPoints(ShopOrder shopOrder) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, shopOrder.getUserId())
                .select(User::getPoints);//查询对应用户的积分
        //执行查询
        User user = userMapper.selectOne(wrapper);
        if (user==null) {
            log.error("用户不存在");
            return false;
        }
        //获取积分
        Integer points=user.getPoints();
        if (points==null) {
            log.error("用户积分不存在");
            return false;
        }
        return points > shopOrder.getPayPrice();
    }

}
