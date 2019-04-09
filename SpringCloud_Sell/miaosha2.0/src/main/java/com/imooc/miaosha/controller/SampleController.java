package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by ASheng
 * 2019/4/5 14:49
 */

@RestController
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    RedisService redisService;

    @RequestMapping("/redis/get")
    public ResultVO redisGet(){
        return ResultVOUtil.success(redisService.get(UserKey.getById,"myKey", String.class));
    }

    @RequestMapping("/redis/set")
    public ResultVO redisSet(){
        return ResultVOUtil.success(redisService.set(UserKey.getById,"myKey", "Hello World"));
    }

}
