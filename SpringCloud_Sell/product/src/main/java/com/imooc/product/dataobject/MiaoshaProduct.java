package com.imooc.product.dataobject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品
 * create by ASheng
 * 2019/4/3 12:28
 */
@Entity
@Data
public class MiaoshaProduct {

    //商品Id
    @Id
    private String miaoshaProductId;

    //商品名称
    private String miaoshaProductName;

    //商品副标题
    private String miaoshaProductSubtitle;

    //产品主图,url相对地址
    private String miaoshaProductMainImage;

    //图片地址,json格式,扩展用
    private String miaoshaProductSubImages;

    //商品详情
    private String miaoshaProductDetail;

    //商品价格,单位是元,保留两位小数
    private BigDecimal miaoshaProductPrice;

    //商品库存
    private Integer miaoshaProductStock;

    //商品状态,1-在售,2-下架,3-删除
    private Integer miaoshaProductStatus;

    //秒杀开始时间
    @DateTimeFormat(pattern="yyyy/mm/dd HH:mm:ss")
    private Date miaoshaStartTime;

    //秒杀结束时间
    @DateTimeFormat(pattern="yyyy/mm/dd HH:mm:ss")
    private Date miaoshaEndTime;

    private Date createTime;

    private Date updateTime;
}
