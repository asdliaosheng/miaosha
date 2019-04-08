package com.imooc.product.service.impl;

import com.imooc.product.dataobject.MiaoshaProduct;
import com.imooc.product.repository.MiaoshaProductRepository;
import com.imooc.product.service.MiaoshaProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by ASheng
 * 2019/4/3 12:48
 */
@Service
public class MiaoshaProductServiceImpl implements MiaoshaProductService {

    @Autowired
    private MiaoshaProductRepository miaoshaProductRepository;

    @Override
    public MiaoshaProduct getMiaoshaProduct(String miaoshaProductId) {
        return miaoshaProductRepository.findByMiaoshaProductId(miaoshaProductId);
    }

    @Override
    public MiaoshaProduct createOrUpdateProduct(MiaoshaProduct miaoshaProduct) {
        return miaoshaProductRepository.save(miaoshaProduct);
    }

    @Override
    public List<MiaoshaProduct> getMiaoshaProductList() {
        return miaoshaProductRepository.findAll();
    }


}
