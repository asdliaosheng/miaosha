package com.imooc.cart.utils;

import java.util.Random;

/**
 * create by ASheng
 * 2019/3/6 10:04
 */
public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     */
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;//六位随机数

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
