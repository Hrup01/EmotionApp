package com.groupb.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.groupb.mapper.ItemShopMapper;
import com.groupb.pojo.ItemShop;
import com.groupb.service.ItemShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemShopServiceImpl implements ItemShopService {

    @Autowired
    private ItemShopMapper itemShopMapper;


    @Override
    public ItemShop createItemShop(ItemShop itemShop) {
        log.info("创建商品");
        int insert = itemShopMapper.insert(itemShop);
        if(insert>0){
            log.info("创建成功");
            return itemShop;
        }else{
            log.error("创建失败返回null");
            return null;
        }
    }

    @Override
    public ItemShop updateItemShop(ItemShop itemShop) {
        log.info("更新商品");
        LambdaUpdateWrapper<ItemShop>updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ItemShop::getId, itemShop.getId());//主键唯一标识
        int update = itemShopMapper.update(itemShop, updateWrapper);//更新
        if(update>0){
            log.info("更新成功");
            return itemShop;
        }else{
            log.error("更新失败返回null");
            return null;
        }
    }

    @Override
    public Boolean downItemShop(Long id) {
        log.info("下架商品");
        if (id==null) {
            log.error("下架失败,商品名或商品主键为空");
            return false;
        }
        LambdaUpdateWrapper<ItemShop>updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ItemShop::getId, id)
                .set(ItemShop::getStatus, 0);//状态置为0
        int update = itemShopMapper.update(null, updateWrapper);
        if(update>0){
            log.info("下架成功");
            return true;
        }else{
            log.error("下架失败");
            return false;
        }
    }

    @Override
    public Boolean upItemShop(Long id) {
        log.info("上架商品");
        if (id==null) {
            log.error("上架失败,商品名或商品主键为空");
            return false;
        }
        LambdaUpdateWrapper<ItemShop>updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ItemShop::getId, id)
                .set(ItemShop::getStatus, 1);//状态置为1
        int update = itemShopMapper.update(null, updateWrapper);
        if(update>0){
            log.info("上架成功");
            return true;
        }else{
            log.error("上架失败");
            return false;
        }
    }

    @Override
    public List<ItemShop> getAllItemShop() {
        log.info("获取全部商品信息");
        return itemShopMapper.selectList(null);
    }

    @Override
    public ItemShop getItemShopById(Long id) {
        log.info("获取单个商品信息");
        if (id==null) {
            log.error("该商品不存在");
        }
        LambdaQueryWrapper<ItemShop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ItemShop::getId, id);//查询
        return itemShopMapper.selectOne(queryWrapper);
    }

}
