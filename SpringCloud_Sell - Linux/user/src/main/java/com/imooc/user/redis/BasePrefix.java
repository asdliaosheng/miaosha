package com.imooc.user.redis;

/**
 * create by ASheng
 * 2019/4/5 18:06
 */
public abstract class BasePrefix implements KeyPrefix {

    private Integer expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public Integer expireSeconds(){
        return expireSeconds;
    }

    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
