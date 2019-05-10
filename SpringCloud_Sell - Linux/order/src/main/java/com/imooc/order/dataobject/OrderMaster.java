package com.imooc.order.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 * create by ASheng
 * 2019/3/26 10:10
 */
@Data
@Entity
public class OrderMaster {

    //订单Id
    @Id
    private String orderId;

    //用户Id
    private String userId;

    //送货地址Id
    private String shippingId;

    //订单总金额
    private BigDecimal orderTotalPrice;

    //支付类型,1-在线支付
    private Integer paymentType;

    //运费,单位是元
    private BigDecimal orderPostage;

    //订单状态,0-已取消,10-未付款,20-已付款,30-未发货,40-已发货,50-交易成功,60-交易关闭
    private Integer orderStatus;

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
