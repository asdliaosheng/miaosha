package com.imooc.product.VO;

import lombok.Data;

/**
 * create by ASheng
 * 2019/3/15 16:15
 */
@Data
public class ResultVO<T> {

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //具体内容
    private T data;
}
