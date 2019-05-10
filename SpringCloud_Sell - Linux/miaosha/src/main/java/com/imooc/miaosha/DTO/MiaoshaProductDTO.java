package com.imooc.miaosha.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * create by ASheng
 * 2019/3/22 12:17
 */
@Data
public class MiaoshaProductDTO {

    //商品Id
    private String miaoshaProductId;

    //商品名称
    private String miaoshaProductName;

    //商品副标题
    private String miaoshaProductSubtitle;

    //产品主图,url相对地址
    private String miaoshaProductMainImage;

    //商品价格,单位是元,保留两位小数
    private BigDecimal miaoshaProductPrice;

}
