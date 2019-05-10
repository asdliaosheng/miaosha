package com.imooc.cart.common;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户
 * create by ASheng
 * 2019/3/14 17:21
 */
@Data
@DynamicUpdate
public class UserInfo {

    //用户Id
    @Id
    private String userId;

    //用户名
    private String userName;

    //密码
    private String userPassword;

    //salt
    private String Salt;

    //邮箱
    private String userMail;

    //电话
    private String userPhone;

    //找回密码问题
    private String userQuestion;

    //找回密码答案
    private String userAnswer;

    //角色
    private Integer userRole;

    private Date createTime;

    private Date updateTime;

}
