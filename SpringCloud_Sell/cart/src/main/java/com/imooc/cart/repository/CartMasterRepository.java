package com.imooc.cart.repository;

import com.imooc.cart.dataobject.CartMaster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * create by ASheng
 * 2019/3/25 10:18
 */
public interface CartMasterRepository extends JpaRepository<CartMaster, String> {

    CartMaster findByUserId(String userId);
}
