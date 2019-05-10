package com.imooc.product.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * create by ASheng
 * 2019/4/5 10:42
 */
@Service
@Configuration
public class RedisPoolFactory {

    @Bean
    public JedisPool jedisPool(@Value("${spring.redis.host}")String host,
                                      @Value("${spring.redis.port}")Integer port,
                                      @Value("${spring.redis.timeout}")Integer timeout,
                                      @Value("${spring.redis.password}")String password,
                                      @Value("${spring.redis.jedis.pool.max-active}")Integer maxActive,
                                      @Value("${spring.redis.jedis.pool.max-idle}")Integer maxIdle,
                                      @Value("${spring.redis.jedis.pool.max-wait}")Integer maxWait
                                      ){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);//最大空闲数，数据库连接的最大空闲时间。超过空闲时间，数据库连接将被标记为不可用，然后被释放。设为0表示无限制。
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait * 1000);
        return new JedisPool(poolConfig, host, port,
                timeout * 1000, password, 0);
    }
}
