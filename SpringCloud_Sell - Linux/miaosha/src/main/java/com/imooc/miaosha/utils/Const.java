package com.imooc.miaosha.utils;

/**
 * create by ASheng
 * 2019/4/3 20:53
 */
public class Const {

    public static final Integer ON_SALE = 1;

    //订单状态,0-已取消,10-未付款,20-已付款,30-未发货,40-已发货,50-交易成功,60-交易关闭
    public interface OrderStatus{
        public final int UNPAID = 10;
    }

}
