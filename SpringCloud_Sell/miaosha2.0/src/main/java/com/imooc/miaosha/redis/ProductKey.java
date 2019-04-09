package com.imooc.miaosha.redis;

/**
 * create by ASheng
 * 2019/4/9 15:48
 */
public class ProductKey extends BasePrefix {

    public ProductKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static ProductKey getMiaoshaProductStock = new ProductKey(0, "ps");
    public static ProductKey isProductOver = new ProductKey(0, "po");
}
