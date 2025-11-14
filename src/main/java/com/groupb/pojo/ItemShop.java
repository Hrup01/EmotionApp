package com.groupb.pojo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("item_shop")
public class ItemShop {

    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //商品名称
    @TableField("shop_name")
    private String shopName;
    //商品价格
    private Long price;
    //商品类型
    private String type;
    //商品状态，1--上架，0--下架
    private Integer status=1;//默认为1
    //商品图片
    @TableField("image_url")
    private String imageUrl;
    //创建时间
    @TableField(value="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
    @TableField(value="update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
