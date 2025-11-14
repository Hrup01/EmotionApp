package com.groupb.service;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.groupb.pojo.ItemShop;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务接口
 */
public interface ItemShopService {


    /**
     * 创建商品
     * @param itemShop 商品
     * @return 商品本身
     */
    ItemShop createItemShop(ItemShop itemShop);

    /**
     * 更新商品
     * @param itemShop 更新的商品
     * @return 商品本身
     */
    ItemShop updateItemShop(ItemShop itemShop);

    /**
     * 下架商品
     * @param id 商品主键
     * @return 布尔类型，true--下架成功，false--下架失败
     */
    Boolean downItemShop(Long id);

    /**
     * 上架商品
     * @param id 商品主键
     * @return 布尔类型，true--上架成功，false--上架失败
     */
    Boolean upItemShop(Long id);

    /**
     * 获取全部商品
     * @return 全部商品的顺序表
     */
    List<ItemShop> getAllItemShop();

    /**
     * 获取单个商品
     * @param id 商品Id
     * @return 单个商品
     */
    ItemShop getItemShopById(Long id);

}
