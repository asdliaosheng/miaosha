package com.imooc.miaosha.service;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.LoginVO;
import com.imooc.miaosha.dataobject.UserInfo;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.repository.MiaoshaUserRepository;
import com.imooc.miaosha.utils.MD5Util;
import com.imooc.miaosha.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/4/6 13:55
 */
@Service
@Transactional
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserRepository miaoshaUserRepository;

    @Autowired
    private RedisService redisService;


    //通过userId取用户对象信息
    public UserInfo getById(String userId){
        //先查缓存
        UserInfo user = redisService.get(MiaoshaUserKey.getById, userId, UserInfo.class);
        if(user != null){
            return user;
        }
        //缓存没有则取mysql,并加进缓存
        user = miaoshaUserRepository.findByUserId(userId);
        if(user != null){
            redisService.set(MiaoshaUserKey.token, userId, user);
        }
        return user;
    }

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

    public String login(HttpServletResponse response, LoginVO loginVO){
        if(loginVO == null){
            throw new GlobalException(CodeMsgVO.SERVER_ERROR);
        }
        String userId = loginVO.getUserId();
        String userPassword = loginVO.getUserPassword();
        //判断用户名是否存在
        UserInfo user = getById(userId);
        if(user == null){
            throw new GlobalException(CodeMsgVO.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getUserPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.inputPassToDBPass(userPassword, dbSalt);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsgVO.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_NAME_TOKEN)){//删除存放token的cookie
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setDomain("47.101.175.31");
                response.addCookie(cookie);
            }
        }
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, UserInfo userInfo) {
        redisService.set(MiaoshaUserKey.token, token, userInfo);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        //cookie.setDomain("47.101.175.31");
        cookie.setDomain("47.101.175.31");
        //cookie.setDomain("localhost");
        response.addCookie(cookie);
    }
}
