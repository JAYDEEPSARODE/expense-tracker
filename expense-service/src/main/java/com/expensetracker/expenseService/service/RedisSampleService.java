package com.expensetracker.expenseService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSampleService {
    private static final String KEY = "sampleKey";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveValue(String value) {
        redisTemplate.opsForValue().set(KEY, value);
    }

    public String getValue() {
        Object value = redisTemplate.opsForValue().get(KEY);
        return value != null ? value.toString() : null;
    }

    public void deleteValue() {
        redisTemplate.delete(KEY);
    }
}

