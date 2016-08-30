package org.zzy.distributed.session.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.zzy.distributed.session.DefaultSession;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoyu on 16-8-19.
 */
public class RedisSessionRepository implements SessionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionRepository.class);

    private RedisTemplate<String, DefaultSession> redisTemplate;

    public RedisSessionRepository() {
    }

    public RedisSessionRepository(RedisTemplate<String, DefaultSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public DefaultSession get(Object key) {
        LOGGER.info("get session from redis : {}.", key);
        return redisTemplate.opsForValue().get(generateKey(key));
    }

    @Override
    public void put(Object key, DefaultSession session) {
        LOGGER.info("put session into redis : {}.", session);
        redisTemplate.opsForValue().set(
                generateKey(key),
                session,
                session.getMaxInactiveInterval(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public void put(Object key, DefaultSession session, long timeout) {
        LOGGER.info("put session into redis : {}.", session);
        redisTemplate.opsForValue().set(
                generateKey(key),
                session,
                timeout,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void putIfAbsent(Object key, DefaultSession defaultSession) {
        redisTemplate.opsForValue().setIfAbsent(
                generateKey(key),
                defaultSession
        );
    }

    @Override
    public void evict(Object key) {
        redisTemplate.delete(
                generateKey(key)
        );
    }

    public void setRedisTemplate(RedisTemplate<String, DefaultSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
