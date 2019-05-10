package com.imooc.cart.redis;

import com.imooc.cart.common.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/4/17 15:13
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private RedisService redisService;

    public UserInfo getByToken(HttpServletResponse response, String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        UserInfo userInfo = redisService.get(MiaoshaUserKey.token, token, UserInfo.class);
        //延长有效期
        if(userInfo != null) {
            addCookie(response, token, userInfo);
        }
        return userInfo;
    }

    private void addCookie(HttpServletResponse response, String token, UserInfo userInfo) {
        redisService.set(MiaoshaUserKey.token, token, userInfo);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        cookie.setDomain("47.101.175.31");
        //cookie.setDomain("localhost");
        response.addCookie(cookie);
    }
}
