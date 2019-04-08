package com.imooc.miaosha.redis;

/**
 * create by ASheng
 * 2019/4/5 18:26
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix){
        super(prefix);
    }
    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
