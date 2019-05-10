package com.imooc.product.dataobject;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * create by ASheng
 * 2019/4/22 21:50
 */
@Data
public class ProductForm {

    //类目Id
    private Integer categoryId;

    //商品名称
    private String productName;

    //商品副标题
    private String productSubtitle;

    //产品主图,url相对地址
    private String productMainImage;

    //图片地址,json格式,扩展用
    private String productSubImages;

    //商品详情
    private String productDetail;

    //商品价格,单位是元,保留两位小数
    private BigDecimal productPrice;

    //商品库存
    private Integer productStock;

    //商品状态,1-在售,2-下架,3-删除
    private Integer productStatus;

}
