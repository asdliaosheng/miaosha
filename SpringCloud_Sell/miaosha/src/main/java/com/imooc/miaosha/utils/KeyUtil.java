package com.imooc.miaosha.utils;

import java.util.Random;

/**
 * create by ASheng
 * 2019/3/15 17:46
 */
public class KeyUtil {

    /**
     * 生成唯一主键
     * 格式: 时间+随机数
     * 不确定能否应对高并发
     * @return
     */
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;//六位随机数

        return System.currentTimeMillis() + String.valueOf(number);
    }

    public static void main(String[] arg){
        for(int i = 0;i<10000;i++){
            System.out.println(""+getUniqueKey()+"---"+i);
        }
    }
}
