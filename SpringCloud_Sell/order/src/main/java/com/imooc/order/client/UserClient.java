package com.imooc.order.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * create by ASheng
 * 2019/3/25 16:16
 */
@FeignClient(name = "user")
@Repository
public interface UserClient {

    @GetMapping("/user/get_user_id")
    String getUserId();

    @GetMapping("/manage/user/check")
    Boolean check();
}
