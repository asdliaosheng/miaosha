package com.imooc.user.form;

import lombok.Data;

import java.util.Date;

/**
 * create by ASheng
 * 2019/3/26 16:48
 */
@Data
public class ShippingForm {

//    //收货地址Id
//    private String shippingId;

//    //用户id
//    private String userId;

    //收货姓名
    private String receiverName;

    //收货电话
    private String receiverPhone;

    //省份
    private String receiverProvince;

    //城市
    private String receiverCity;

    //区/县
    private String receiverDistrict;

    //详细地址
    private String receiverAddress;

    //邮编
    private String receiverZip;

//    private Date createTime;
//
//    private Date updateTime;
}
