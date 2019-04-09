package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ASheng
 * 2019/4/5 10:35
 */
@Service
public class RedisService {


    @Autowired
    RedisPoolFactory redisPoolFactory;

    /**
     * 取单个对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
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
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
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

    //减少值
    public Long decrease(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try{
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally{
            returnToPool(jedis);
        }
    }

    /*
    *删除单个
     */
    public <T> boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long result = jedis.del(realKey);
            return result > 0;
        }finally{
            returnToPool(jedis);
        }
    }

    /*
    模糊批量删除
     */
    public <T> boolean delete(KeyPrefix prefix){
        if(prefix == null){
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys == null || keys.size() <= 0){
            return true;
        }
        Jedis jedis = null;
        try{
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        }catch (final Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            returnToPool(jedis);
        }
    }

    public List<String> scanKeys(String key){
        Jedis jedis = null;
        try{
            JedisPool jedisPool = redisPoolFactory.redisPoolFactory();
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            String cursor = "0";
            ScanParams scanParams = new ScanParams();
            scanParams.match("*"+key+"*");
            scanParams.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, scanParams);
                List<String> result = ret.getResult();
                if(result != null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getStringCursor();
            }while(!cursor.equals("0"));
            return keys;
        }finally{
            returnToPool(jedis);
        }
    }
}
