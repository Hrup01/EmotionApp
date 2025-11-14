package com.groupb.controller.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * mybatis-plus时间自动填充方法类
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 字段名常量，便于统一管理
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);//插入时二者更新
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);//更新时只更新update时间
    }
}