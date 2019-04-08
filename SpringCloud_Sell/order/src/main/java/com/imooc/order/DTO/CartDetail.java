package com.imooc.order.DTO;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * create by ASheng
 * 2019/3/25 9:54
 */
@Data
public class CartDetail {

    //详情表Id
    private String cartDetailId;

    //购物车主表Id
    private String cartId;

    //是否选择
    private Integer checked;

    //商品Id
    private String productId;

    //商品名称
    private String productName;

    //商品价格
    private BigDecimal productCurrentPrice;

    //商品数量
    private Integer productQuantity;

    //商品图片地址
    private String productImage;

    private Date createTime;

    private Date updateTime;
}
