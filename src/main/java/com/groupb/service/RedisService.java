package com.groupb.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务接口
 */
public interface RedisService {
    
    // 字符串操作
    void set(String key, Object value);
    void set(String key, Object value, long timeout, TimeUnit unit);
    Object get(String key);
    void delete(String key);
    boolean hasKey(String key);
    
    // 列表操作
    void leftPush(String key, Object value);
    void rightPush(String key, Object value);
    List<Object> range(String key, long start, long end);
    Object leftPop(String key);
    Object rightPop(String key);
    long listSize(String key);
    
    // 集合操作
    void addToSet(String key, Object value);
    Set<Object> getSet(String key);
    boolean isSetMember(String key, Object value);
    void removeFromSet(String key, Object value);
    
    // 哈希操作
    void hSet(String key, String field, Object value);
    Object hGet(String key, String field);
    void hDelete(String key, String field);
    
    // 过期时间
    void expire(String key, long timeout, TimeUnit unit);
    long getExpire(String key);
}
