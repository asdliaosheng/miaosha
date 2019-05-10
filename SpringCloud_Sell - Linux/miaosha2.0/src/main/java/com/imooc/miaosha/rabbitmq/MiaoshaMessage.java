package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.dataobject.UserInfo;
import lombok.Data;

/**
 * create by ASheng
 * 2019/4/9 16:42
 */
@Data
public class MiaoshaMessage {

    private UserInfo user;
    private String productId;
}
