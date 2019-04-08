package com.imooc.order.client;

import com.imooc.order.DTO.CartDTO;
import com.imooc.order.VO.ResultVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * create by ASheng
 * 2019/3/26 11:40
 */
@FeignClient(name = "cart")
@Repository
public interface CartClient {

    @PostMapping("/cart/search")
    CartDTO search();
}
