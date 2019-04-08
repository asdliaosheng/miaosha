package com.imooc.user.enums;

import lombok.Getter;

/**
 * create by ASheng
 * 2019/3/18 13:08
 */
@Getter
public enum ResultEnum {

    PARAM_ERROR(1, "参数错误"),
    USERNAME_NOT_EXISTS(2, "用户名不存在"),
    USERNAME_ALREADY_EXISTS(3, "用户名已存在"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
