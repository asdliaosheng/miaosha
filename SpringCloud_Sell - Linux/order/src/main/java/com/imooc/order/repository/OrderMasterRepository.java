package com.imooc.order.repository;

import com.imooc.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 10:15
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    List<OrderMaster> findByUserId(String userId);
    OrderMaster findByOrderId(String orderId);
}
