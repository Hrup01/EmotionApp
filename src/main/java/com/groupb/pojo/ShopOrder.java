package com.groupb.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shop_order")
public class ShopOrder {

    //主键
    @TableId(type = IdType.ASSIGN_UUID)//UUID生成
    private String id;
    //用户Id
    @TableField("user_id")
    private Long userId;
    //商品Id
    @TableField("shop_id")
    private Long shopId;
    //商品名称
    @TableField("shop_name")
    private String shopName;
    //购买情况
    @TableField("buy_status")
    private Integer buyStatus;//1--购买,0--未购买
    //花销金额
    @TableField("pay_price")
    private Integer payPrice;
    //创建时间
    @TableField(value="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(value="update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
