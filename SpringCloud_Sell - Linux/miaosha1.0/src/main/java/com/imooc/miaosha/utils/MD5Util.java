package com.imooc.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * create by ASheng
 * 2019/4/5 23:10
 */
public class MD5Util {

    //一次md5加密
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    //用户输入--> 表单数据 (应该在浏览器做)
    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
//        System.out.println(str);
        return md5(str);
    }

    //表单数据--> 数据库数据
    public static String formPassToDBPass(String formPass, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
//        System.out.println(str);
        return md5(str);
    }

    //用户输入--> 数据库数据
    public static String inputPassToDBPass(String inputPass, String saltDB){
        String formPass = inputPassToFormPass(inputPass);
//        System.out.println(formPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
//        System.out.println(dbPass);
        return md5(dbPass);
    }

    public static void main(String[] args) {
//        System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));//17831d20a8eb781923856d66d599dba3
    }
}
