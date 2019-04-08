package com.imooc.user.exception;

import com.imooc.user.enums.ResultEnum;

/**
 * create by ASheng
 * 2019/3/18 12:44
 */
public class UserException extends RuntimeException {

    private Integer code;

    public UserException(Integer code , String msg){
        super(msg);
        this.code = code;
    }

    public UserException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
