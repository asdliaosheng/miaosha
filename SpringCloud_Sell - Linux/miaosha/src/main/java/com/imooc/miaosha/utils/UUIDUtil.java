package com.imooc.miaosha.utils;

import java.util.UUID;

/**
 * create by ASheng
 * 2019/4/6 14:19
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(uuid());//4cc1047a37b645ce97354f84d5b31777
    }
}
