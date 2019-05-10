package com.imooc.product.converter;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.dataobject.ProductInfo;
import org.springframework.beans.BeanUtils;

/**
 * 转换器
 * create by ASheng
 * 2019/3/22 12:46
 */
public class ProductInfo2ProductDTOConverter {

    public static ProductDTO converter(ProductInfo productInfo){
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productInfo, productDTO);
        return productDTO;
    }
}
