package com.imooc.product.repository;

import com.imooc.product.dataobject.MiaoshaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * create by ASheng
 * 2019/4/3 12:43
 */
@Repository
public interface MiaoshaProductRepository extends JpaRepository<MiaoshaProduct, String> {
    MiaoshaProduct findByMiaoshaProductId(String miaoshaProductId);
}
