package com.imooc.miaosha.service;

import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.repository.MiaoshaProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by ASheng
 * 2019/4/3 12:48
 */
@Service
public class MiaoshaProductService {

    @Autowired
    private MiaoshaProductRepository miaoshaProductRepository;

    public MiaoshaProduct getMiaoshaProduct(String miaoshaProductId) {
        return miaoshaProductRepository.findByMiaoshaProductId(miaoshaProductId);
    }

    public MiaoshaProduct createOrUpdateProduct(MiaoshaProduct miaoshaProduct) {
        return miaoshaProductRepository.save(miaoshaProduct);
    }

    public List<MiaoshaProduct> getMiaoshaProductList() {
        return miaoshaProductRepository.findAll();
    }


    public void deleteMiaoshaProduct(String miaoshaProductId) {
        miaoshaProductRepository.deleteById(miaoshaProductId);
    }

    public int reduceStock(MiaoshaProduct miaoshaProduct) {
        return miaoshaProductRepository.reduceProductStock(miaoshaProduct.getMiaoshaProductId());
    }

    public void save(MiaoshaProduct miaoshaProduct) {
        miaoshaProductRepository.save(miaoshaProduct);
    }

    public void resetStock(List<MiaoshaProduct> productList) {
        for(MiaoshaProduct product: productList){
            miaoshaProductRepository.save(product);
        }
    }
}
