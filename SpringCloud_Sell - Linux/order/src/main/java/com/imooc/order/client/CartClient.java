package com.imooc.order.client;

import com.imooc.order.DTO.CartDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * create by ASheng
 * 2019/3/26 11:40
 */
@FeignClient(name = "cart")
@Repository
public interface CartClient {

    @PostMapping("/cart/search")
    CartDTO search(@RequestBody String userId);

    @PostMapping("/cart/delete_checked")
    CartDTO deleteChecked(@RequestBody String userId);

}
