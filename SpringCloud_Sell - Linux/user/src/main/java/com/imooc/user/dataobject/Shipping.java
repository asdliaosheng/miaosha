package com.imooc.user.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 收货地址信息
 * create by ASheng
 * 2019/3/26 16:36
 */
@Entity
@Data
public class Shipping {

    //收货地址Id
    @Id
    private String shippingId;

    //用户id
    private String userId;

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

    private Date createTime;

    private Date updateTime;
}
