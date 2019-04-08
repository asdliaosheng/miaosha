package com.imooc.miaosha.VO;

import lombok.Data;

/**
 * create by ASheng
 * 2019/4/6 14:44
 */
@Data
public class LoginVO {

    private String userId;

    private String userPassword;

    /**
     * lombok提供了
     * 所有属性的get和set方法
     * toString 方法
     * hashCode方法
     * equals方法
     *
     * return "LoginVO(userName=" + userName + ", userPassword=" + userPassword + ")";
     */
//    @Override
//    public String toString(){
//        return "LoginVO [userName="+userName+",userPassword="+userPassword+"]";
//    }
}
