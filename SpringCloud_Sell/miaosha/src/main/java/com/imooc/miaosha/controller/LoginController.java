package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.LoginVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * create by ASheng
 * 2019/4/6 16:37
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/do_login")
    public ResultVO<Boolean> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) {
        log.info(loginVO.toString());
        //登录
        miaoshaUserService.login(response, loginVO);
        return ResultVO.success(true);
    }

    @RequestMapping("/do_logout")
    public ResultVO<Boolean> doLogout(HttpServletRequest request, HttpServletResponse response) {
        //登出
        miaoshaUserService.logout(request, response);
        return ResultVO.success(true);
    }
}
