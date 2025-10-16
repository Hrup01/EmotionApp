package com.groupb.service.serviceImpl;

import com.groupb.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务降级实现 - 当Redis不可用时使用
 */
@Service
@Slf4j
@ConditionalOnMissingBean(RedisTemplate.class)
public class NoOpRedisServiceImpl implements RedisService {
    
    @Override
    public void set(String key, Object value) {
        log.debug("Redis不可用，跳过set操作: key={}", key);
    }
    
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        log.debug("Redis不可用，跳过set操作: key={}", key);
    }
    
    @Override
    public Object get(String key) {
        log.debug("Redis不可用，返回null: key={}", key);
        return null;
    }
    
    @Override
    public void delete(String key) {
        log.debug("Redis不可用，跳过delete操作: key={}", key);
    }
    
    @Override
    public boolean hasKey(String key) {
        log.debug("Redis不可用，返回false: key={}", key);
        return false;
    }
    
    @Override
    public void leftPush(String key, Object value) {
        log.debug("Redis不可用，跳过leftPush操作: key={}", key);
    }
    
    @Override
    public void rightPush(String key, Object value) {
        log.debug("Redis不可用，跳过rightPush操作: key={}", key);
    }
    
    @Override
    public List<Object> range(String key, long start, long end) {
        log.debug("Redis不可用，返回空列表: key={}", key);
        return List.of();
    }
    
    @Override
    public Object leftPop(String key) {
        log.debug("Redis不可用，返回null: key={}", key);
        return null;
    }
    
    @Override
    public Object rightPop(String key) {
        log.debug("Redis不可用，返回null: key={}", key);
        return null;
    }
    
    @Override
    public long listSize(String key) {
        log.debug("Redis不可用，返回0: key={}", key);
        return 0;
    }
    
    @Override
    public void addToSet(String key, Object value) {
        log.debug("Redis不可用，跳过addToSet操作: key={}", key);
    }
    
    @Override
    public Set<Object> getSet(String key) {
        log.debug("Redis不可用，返回空集合: key={}", key);
        return Set.of();
    }
    
    @Override
    public boolean isSetMember(String key, Object value) {
        log.debug("Redis不可用，返回false: key={}", key);
        return false;
    }
    
    @Override
    public void removeFromSet(String key, Object value) {
        log.debug("Redis不可用，跳过removeFromSet操作: key={}", key);
    }
    
    @Override
    public void hSet(String key, String field, Object value) {
        log.debug("Redis不可用，跳过hSet操作: key={}, field={}", key, field);
    }
    
    @Override
    public Object hGet(String key, String field) {
        log.debug("Redis不可用，返回null: key={}, field={}", key, field);
        return null;
    }
    
    @Override
    public void hDelete(String key, String field) {
        log.debug("Redis不可用，跳过hDelete操作: key={}, field={}", key, field);
    }
    
    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        log.debug("Redis不可用，跳过expire操作: key={}", key);
    }
    
    @Override
    public long getExpire(String key) {
        log.debug("Redis不可用，返回-1: key={}", key);
        return -1;
    }
}
