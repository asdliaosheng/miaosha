package com.imooc.cart.client;

import com.imooc.cart.dataobject.ProductInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * create by ASheng
 * 2019/3/25 12:41
 */
@FeignClient(name = "product")
@Repository
public interface ProductClient {

    @PostMapping("/product/search")
    public ProductInfo search(@RequestBody String productId);
}
