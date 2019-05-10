package com.imooc.miaosha.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀订单
 * create by ASheng
 * 2019/4/3 20:21
 */
@Entity
@Data
public class MiaoshaOrder {

    //订单Id
    @Id
    private String miaoshaOrderId;

    //用户Id
    private String userId;

    //送货地址Id
    private String shippingId;

    //商品id
    private String miaoshaProductId;

    //商品名称
    private String miaoshaProductName;

    //生成订单时商品价格
    private BigDecimal miaoshaProductPrice;

    //商品数量
    private Integer miaoshaProductQuantity;

    //商品图片地址
    private String miaoshaProductMainImage;

    //支付类型,1-在线支付
    private Integer paymentType;

    //运费,单位是元
    private BigDecimal miaoshaOrderPostage;

    //订单总金额
    private BigDecimal miaoshaOrderTotalPrice;

    //订单状态,0-已取消,10-未付款,20-已付款,30-未发货,40-已发货,50-交易成功,60-交易关闭
    private Integer miaoshaOrderStatus;

    //支付时间
    private Date paymentTime;

    //发货时间
    private Date sendTime;

    //交易完成时间
    private Date endTime;

    //交易关闭时间
    private Date closeTime;


    private Date createTime;

    private Date updateTime;
}
