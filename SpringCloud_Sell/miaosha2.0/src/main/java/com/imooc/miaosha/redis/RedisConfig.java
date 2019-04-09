package com.imooc.miaosha.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * create by ASheng
 * 2019/4/5 10:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;
    private Integer port;
    private Integer timeout;
    private String password;
    private Integer poolMaxTotal;
    private Integer poolMaxIdle;
    private Integer poolMaxWait;
}
