package com.imooc.cart.redis;

/**
 * create by ASheng
 * 2019/4/5 17:59
 */
public interface KeyPrefix {

    //到期时间,为0则永不过期
    public Integer expireSeconds();

    public String getPrefix();
}
