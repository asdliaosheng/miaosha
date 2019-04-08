package com.imooc.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by ASheng
 * 2019/3/28 12:09
 */
@Slf4j
@RestController
public class TestUserController {

    @RequestMapping(value = "/get_msg", method = RequestMethod.GET)
    public String getMsg(){
        return "hello";
    }
}
