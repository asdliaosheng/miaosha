package com.imooc.cart.repository;

import com.imooc.cart.dataobject.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/25 10:19
 */
public interface CartDetailRepository extends JpaRepository<CartDetail, String> {

    List<CartDetail> findAllByCartId(String cartId);
    CartDetail findByCartIdAndProductId(String cartId, String productId);

}
