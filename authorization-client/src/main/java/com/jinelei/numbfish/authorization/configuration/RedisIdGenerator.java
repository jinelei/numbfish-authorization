package com.jinelei.numbfish.authorization.configuration;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisIdGenerator implements IdentifierGenerator {
    private static final String KEY_PREFIX = "KEY_PREFIX:";
    private static final long INITIAL_VALUE = 200000L;
    private final RedisTemplate<String, Long> redisTemplate;

    public RedisIdGenerator(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Number nextId(Object entity) {
        String className = entity.getClass().getSimpleName();
        String key = KEY_PREFIX + className;
        redisTemplate.opsForValue().setIfAbsent(key, INITIAL_VALUE);
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public String nextUUID(Object entity) {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
