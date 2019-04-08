package com.imooc.order.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情
 * create by ASheng
 * 2019/3/26 10:10
 */
@Data
@Entity
public class OrderDetail {

    //订单详情id
    @Id
    private String detailId;

    //订单id
    private String orderId;

    //商品id
    private String productId;

    //商品名称
    private String productName;

    //生成订单时商品价格
    private BigDecimal productCurrentPrice;

    //商品数量
    private Integer productQuantity;

    //商品图片地址
    private String productImage;

    private Date createTime;

    private Date updateTime;

}
