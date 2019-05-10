package com.imooc.miaosha.redis;

/**
 * create by ASheng
 * 2019/4/9 15:48
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaOrderByUidPid = new OrderKey(0, "moup");
}
