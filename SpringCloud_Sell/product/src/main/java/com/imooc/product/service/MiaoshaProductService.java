package com.imooc.product.service;

import com.imooc.product.dataobject.MiaoshaProduct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by ASheng
 * 2019/4/3 12:48
 */
public interface MiaoshaProductService {
    MiaoshaProduct getMiaoshaProduct(String miaoshaProductId);
    MiaoshaProduct createOrUpdateProduct(MiaoshaProduct miaoshaProduct);
    List<MiaoshaProduct> getMiaoshaProductList();
}
