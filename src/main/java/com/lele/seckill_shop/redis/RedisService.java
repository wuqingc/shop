package com.lele.seckill_shop.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取对象.
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            String str = jedis.get(key);
            return StringToBean(str,clazz);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            key = prefix.getPrefix() + key;
            if (StringUtils.isBlank(str)) {
                return false;
            }

            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(key,str);
            } else {
                jedis.setex(key,seconds,str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * 判断是否存在
     * @param prefix
     * @param key
     * @return
     */
    public boolean exist(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long delete(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.del(key);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 增加值
     * @param prefix
     * @param key
     * @return
     */
    public Long incr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.incr(key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * @param prefix
     * @param key
     * @return
     */
    public Long decr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.decr(key);
        } finally {
            returnToPool(jedis);
        }
    }


    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class){
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class){
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T StringToBean(String str,Class<T> clazz) {
        if (StringUtils.isBlank(str) || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
