package com.imooc.product.DTO;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * create by ASheng
 * 2019/3/22 12:17
 */
@Data
public class ProductDTO {

    //商品Id
    private String productId;

    //类目Id
    private Integer categoryId;

    //商品名称
    private String productName;

    //商品副标题
    private String productSubtitle;

    //产品主图,url相对地址
    private String productMainImage;

    //商品价格,单位是元,保留两位小数
    private BigDecimal productPrice;

    //商品状态,1-在售,2-下架,3-删除
    private Integer productStatus;

    //参与秒杀状态
    private Integer productSeckillStatus;

    //秒杀价格
    private BigDecimal productSeckillPrice;

}
