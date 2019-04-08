package com.imooc.product.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * create by ASheng
 * 2019/3/15 16:14
 */
@Data
public class UserDTO {

    //用户Id
    @JsonProperty("id")
    private String userId;

    //用户名
    @JsonProperty("userName")
    private String userName;

    //邮箱
    @JsonProperty("email")
    private String userMail;

    //电话
    @JsonProperty("phone")
    private String userPhone;

    //角色
    @JsonProperty("role")
    private Integer userRole;

    @JsonProperty("createTime")
    private Date createTime;

    @JsonProperty("updateTime")
    private Date updateTime;
}
