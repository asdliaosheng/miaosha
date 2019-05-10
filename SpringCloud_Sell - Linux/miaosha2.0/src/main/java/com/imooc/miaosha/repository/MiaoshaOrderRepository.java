package com.imooc.miaosha.repository;

import com.imooc.miaosha.dataobject.MiaoshaOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * create by ASheng
 * 2019/4/3 20:31
 */
public interface MiaoshaOrderRepository extends JpaRepository<MiaoshaOrder, String> {
    MiaoshaOrder findByUserIdAndMiaoshaProductId(String userId, String productId);
}
