package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.dataobject.MiaoshaUser;
import lombok.Data;

/**
 * create by ASheng
 * 2019/4/9 16:42
 */
@Data
public class MiaoshaMessage {

    private MiaoshaUser user;
    private String productId;
}
