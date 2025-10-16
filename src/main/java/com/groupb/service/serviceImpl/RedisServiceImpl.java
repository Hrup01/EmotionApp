package com.groupb.service.serviceImpl;

import com.groupb.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis set操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis set操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get操作失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }
    
    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis hasKey操作失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }
    
    @Override
    public void leftPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("Redis leftPush操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public void rightPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("Redis rightPush操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public List<Object> range(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis range操作失败: key={}, error={}", key, e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public Object leftPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("Redis leftPop操作失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }
    
    @Override
    public Object rightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("Redis rightPop操作失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }
    
    @Override
    public long listSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Redis listSize操作失败: key={}, error={}", key, e.getMessage());
            return 0;
        }
    }
    
    @Override
    public void addToSet(String key, Object value) {
        try {
            redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            log.error("Redis addToSet操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis getSet操作失败: key={}, error={}", key, e.getMessage());
            return Set.of();
        }
    }
    
    @Override
    public boolean isSetMember(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error("Redis isSetMember操作失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }
    
    @Override
    public void removeFromSet(String key, Object value) {
        try {
            redisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            log.error("Redis removeFromSet操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public void hSet(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
        } catch (Exception e) {
            log.error("Redis hSet操作失败: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }
    
    @Override
    public Object hGet(String key, String field) {
        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error("Redis hGet操作失败: key={}, field={}, error={}", key, field, e.getMessage());
            return null;
        }
    }
    
    @Override
    public void hDelete(String key, String field) {
        try {
            redisTemplate.opsForHash().delete(key, field);
        } catch (Exception e) {
            log.error("Redis hDelete操作失败: key={}, field={}, error={}", key, field, e.getMessage());
        }
    }
    
    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            log.error("Redis expire操作失败: key={}, error={}", key, e.getMessage());
        }
    }
    
    @Override
    public long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key);
            return expire != null ? expire : -1;
        } catch (Exception e) {
            log.error("Redis getExpire操作失败: key={}, error={}", key, e.getMessage());
            return -1;
        }
    }
}
