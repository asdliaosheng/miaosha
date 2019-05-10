package com.imooc.order.client;

import com.imooc.order.dataobject.OrderDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 14:50
 */
@FeignClient(name = "product")
@Repository
public interface ProductClient {

    @RequestMapping(value = "/product/decrease_stock", method = RequestMethod.POST)
    String decreaseStock(@RequestBody List<OrderDetail> orderDetailList);
}
