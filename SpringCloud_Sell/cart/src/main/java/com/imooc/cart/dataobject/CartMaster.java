package com.imooc.cart.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * create by ASheng
 * 2019/3/25 9:54
 */
@Entity
@Data
public class CartMaster {

    //购物车Id
    @Id
    private String cartId;

    //用户Id
    private String userId;

    //购物车总金额
    private BigDecimal cartTotalPrice;

    private Date createTime;

    private Date updateTime;
}
