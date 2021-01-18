package com.youyu.conference.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    final String KEY = "conference_";

    /**
     * 添加缓存
     * 过期时间默认1小时
     *
     * @param key
     * @param value
     */
    public void putCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(KEY + key, value, 1, TimeUnit.HOURS);
        return;
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param timeout 超时时间，单位小时，如果为0，则无限制
     */
    public void putCache(String key, String value, long timeout) {
        if (timeout == 0) {
            stringRedisTemplate.opsForValue().set(KEY + key, value);
        } else {
            stringRedisTemplate.opsForValue().set(KEY + key, value, timeout, TimeUnit.HOURS);
        }
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param timeout 超时时间，单位小时，如果为0，则无限制
     */
    public void putCache(String key, String value, long timeout, TimeUnit timeUnit) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("time out cannot less than zero");
        }
        stringRedisTemplate.opsForValue().set(KEY + key, value, timeout, timeUnit);
    }


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public String getCache(String key) throws RedisConnectionFailureException {
        return stringRedisTemplate.opsForValue().get(KEY + key);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void delCache(String key) {
        stringRedisTemplate.delete(KEY + key);
    }

    //操作list 对象

    /**
     * 获取key中全部对象
     *
     * @param key
     * @return
     */
    public List<Object> lGetObjectAll(String key) {
        return redisTemplate.opsForList().range(KEY + key, 0, -1);
    }

    /**
     * 通过索引获取列表中的元素
     */
    public Object lIndexObject(String key, long index) {
        return redisTemplate.opsForList().index(KEY + key, index);
    }

    /**
     * 获取列表指定范围内的元素
     */
    public List<Object> lRangeObject(String key, long start, long end) {
        return redisTemplate.opsForList().range(KEY + key, start, end);
    }

    /**
     * 存储在list头部
     */
    public Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(KEY + key, value);
    }

    /**
     * 存储在list头部
     */
    public Long lLeftPushAll(String key, Object... value) {
        return redisTemplate.opsForList().leftPushAll(KEY + key, value);
    }

    /**
     * 存储在list头部
     */
    public Long lLeftPushAllObject(String key, Collection<Object> value) {
        return redisTemplate.opsForList().leftPushAll(KEY + key, value);
    }

    /**
     * 当list存在的时候才加入
     */
    public Long lLeftPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(KEY + key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     */
    public Long lLeftPush(String key, String pivot, Object value) {
        return redisTemplate.opsForList().leftPush(KEY + key, pivot, value);
    }

    /**
     * 存储在list尾部
     */
    public Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(KEY + key, value);
    }

    /**
     * 存储在list尾部
     */
    public Long lRightPushAll(String key, Object... value) {
        return redisTemplate.opsForList().rightPushAll(KEY + key, value);
    }

    /**
     * 存储在list尾部
     */
    public Long lRightPushAllObject(String key, Collection<Object> value) {
        return redisTemplate.opsForList().rightPushAll(KEY + key, value);
    }

    /**
     * 为已存在的列表添加值
     */
    public Long lRightPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(KEY + key, value);
    }

    /**
     * 在pivot元素的右边添加值
     */
    public Long lRightPush(String key, String pivot, Object value) {
        return redisTemplate.opsForList().rightPush(KEY + key, pivot, value);
    }
}
