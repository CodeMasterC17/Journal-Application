package net.engineeringdigest.journalapplication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                log.warn("No value found in Redis for key: {}", key);
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            if (o instanceof String jsonString) {
                log.info("Value found in Redis for key: {}", key);
                return mapper.readValue(jsonString, entityClass);
            } else {
                log.warn("Unexpected object type for key {}: {}", key, o.getClass().getName());
                return null;
            }


        } catch (Exception e) {
            log.error("Exception while reading from Redis key: {}", key, e);
            return null;
        }
    }


    public void set(String key, Object o, Long ttl) {
        if (o == null) {
            log.warn("Attempted to cache a null object for key: {}", key);
            return;
        }
        try {
            ObjectMapper objectmapper = new ObjectMapper();
            String jsonValue = objectmapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue,ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
}