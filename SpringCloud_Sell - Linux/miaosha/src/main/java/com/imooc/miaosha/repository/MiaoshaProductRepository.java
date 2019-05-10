package com.imooc.miaosha.repository;

import com.imooc.miaosha.dataobject.MiaoshaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * create by ASheng
 * 2019/4/3 12:43
 */
@Repository
public interface MiaoshaProductRepository extends JpaRepository<MiaoshaProduct, String> {

    MiaoshaProduct findByMiaoshaProductId(String miaoshaProductId);

    @Transactional
    @Modifying
    @Query(value = "update miaosha_product p set p.miaosha_product_stock = ?1 where p.miaosha_product_id = ?2", nativeQuery = true)
    int reduceProductStock(int stock, String productId);


}
