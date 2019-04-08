package com.imooc.user.repository;

import com.imooc.user.dataobject.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 16:41
 */
public interface ShippingRepository extends JpaRepository<Shipping, String> {

    Shipping findByShippingId(String shippingId);
    List<Shipping> findByUserId(String userId);
}
