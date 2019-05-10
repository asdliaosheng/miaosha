package com.imooc.product.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * create by ASheng
 * 2019/4/17 15:14
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            //JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            String value = jedis.get(realKey);
            T t = stringToBean(value, clazz);
            return t;
        }finally{
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            //JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() < 0){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if(seconds <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally{
            returnToPool(jedis);
        }
    }



    public static <T> String beanToString(T value) {
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return ""+value;
        }else if(clazz == long.class || clazz == Long.class){
            return ""+value;
        }else if(clazz == boolean.class || clazz == Boolean.class){
            return ""+value;
        }else if(clazz == float.class || clazz == Float.class){
            return ""+value;
        }else if(clazz == double.class || clazz == Double.class){
            return ""+value;
        }else if(clazz == String.class){
            return (String) value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length()<= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else if(clazz == boolean.class || clazz == Boolean.class){
            return (T) Boolean.valueOf(str);
        }else if(clazz == float.class || clazz == Float.class){
            return (T) Float.valueOf(str);
        }else if(clazz == double.class || clazz == Double.class){
            return (T) Double.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }
}
