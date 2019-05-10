package com.imooc.order.DTO;

import com.imooc.order.dataobject.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 10:43
 */
@Data
public class OrderDTO {

    //订单Id
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

    private List<OrderDetail> orderDetailList;
}
