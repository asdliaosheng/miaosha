package com.imooc.order.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/25 16:06
 */
@Data
public class CartDTO {

    //购物车Id
    private String cartId;

    //用户Id
    private String userId;

    //购物车总金额
    private BigDecimal cartTotalPrice;

    private Date createTime;

    private Date updateTime;

    private List<CartDetail> cartDetailList;
}
