package com.imooc.order.service;

import com.imooc.order.DTO.OrderDTO;
import com.imooc.order.VO.ResultVO;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 10:16
 */
public interface OrderService {

    ResultVO create();
    List<OrderDTO> list(String userId);
    OrderDTO detail(String orderId);
    OrderDTO cancel(String orderId);
    OrderDTO updateShippingId(String orderId, String shippingId);
    OrderDTO update(String orderId, Integer orderStatus);
}
