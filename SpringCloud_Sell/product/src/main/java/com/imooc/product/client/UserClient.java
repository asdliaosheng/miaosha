package com.imooc.product.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * create by ASheng
 * 2019/3/26 18:25
 */
@FeignClient(name = "user")
@Repository
public interface UserClient {

    @GetMapping("/manage/user/check")
    Boolean check();
}
