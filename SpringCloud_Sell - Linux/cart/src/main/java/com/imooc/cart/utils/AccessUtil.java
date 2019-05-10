package com.imooc.cart.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/5/5 23:33
 */
public class AccessUtil {

    public static void allowAccess(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "http://47.101.175.31");
    }
}
