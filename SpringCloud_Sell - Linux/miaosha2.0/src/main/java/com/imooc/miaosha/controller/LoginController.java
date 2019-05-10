package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.LoginVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.utils.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/do_login", method = RequestMethod.POST)
    public ResultVO<String> doLogin(@Valid LoginVO loginVO, HttpServletResponse response) {
        log.info(loginVO.toString());
        //登录
        AccessUtil.allowAccess(response);
        String token = miaoshaUserService.login(response, loginVO);
        return ResultVO.success(token);
    }

    @RequestMapping(value = "/do_login_test", method = RequestMethod.GET)
    public ResultVO<String> doLoginTest(@Valid LoginVO loginVO, HttpServletResponse response) {
        log.info(loginVO.toString());
        //登录
        AccessUtil.allowAccess(response);
        String token = miaoshaUserService.login(response, loginVO);
        return ResultVO.success(token);
    }

    @RequestMapping(value = "/do_logout", method = RequestMethod.POST)
    public ResultVO<Boolean> doLogout(HttpServletRequest request, HttpServletResponse response) {
        //登出
        AccessUtil.allowAccess(response);
        miaoshaUserService.logout(request, response);
        return ResultVO.success(true);
    }

    @RequestMapping(value = "/do_logout_test", method = RequestMethod.GET)
    public ResultVO<Boolean> doLogoutTest(HttpServletRequest request, HttpServletResponse response) {
        //登出
        AccessUtil.allowAccess(response);
        miaoshaUserService.logout(request, response);
        return ResultVO.success(true);
    }

//    private void allowAccess(HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin", "http://47.101.175.31");
//    }
}
