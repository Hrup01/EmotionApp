package com.groupb.controller;


import com.groupb.pojo.ItemShop;
import com.groupb.pojo.dto.Result;
import com.groupb.service.ItemShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("itemShop")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"}, maxAge = 3600)
public class ItemShopController {

    @Autowired
    private ItemShopService itemShopService;

    /**
     * 获取全部商品数据
     */
    @GetMapping
    public Result<List<ItemShop>> getAllItemShop() {
        List<ItemShop> allItemShop = itemShopService.getAllItemShop();
        if (allItemShop==null) {
            log.error("查询失败，商品为空");
            return Result.error("商品列表为空");
        }
        return Result.success(allItemShop);
    }

    //创建商品
    @PostMapping
    public Result<ItemShop> createItemShop(@RequestBody ItemShop itemShop) {
        ItemShop createItemShop = itemShopService.createItemShop(itemShop);
        if (createItemShop==null) {
            log.error("创建失败");
            return Result.error("创建失败");
        }
        return Result.success(createItemShop);
    }

    //更新商品
    @PostMapping("update")
    public Result<ItemShop> updateItemShop(@RequestBody ItemShop itemShop) {
        ItemShop updateItemShop = itemShopService.updateItemShop(itemShop);
        if (updateItemShop==null) {
            log.error("更新失败");
            return Result.error("更新失败");
        }
        return Result.success(updateItemShop);
    }


    /**
     * 获取单个商品数据
     */
    @GetMapping("{id}")
    public Result<ItemShop> getItemShop(@PathVariable Long id) {
        ItemShop itemShop = itemShopService.getItemShopById(id);
        if (itemShop==null) {
            log.error("查询不到该商品");
            return Result.error("查询不到该商品");
        }
        return Result.success(itemShop);
    }

}
