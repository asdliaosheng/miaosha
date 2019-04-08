package com.imooc.miaosha.service;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.LoginVO;
import com.imooc.miaosha.dataobject.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.repository.MiaoshaUserRepository;
import com.imooc.miaosha.utils.MD5Util;
import com.imooc.miaosha.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/4/6 13:55
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserRepository miaoshaUserRepository;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(String userId){
        return miaoshaUserRepository.findByUserId(userId);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;
    }

    public boolean login(HttpServletResponse response, LoginVO loginVO){
        if(loginVO == null){
            throw new GlobalException(CodeMsgVO.SERVER_ERROR);
        }
        String userId = loginVO.getUserId();
        String userPassword = loginVO.getUserPassword();
        //判断用户名是否存在
        MiaoshaUser user = getById(userId);
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
        return true;
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_NAME_TOKEN)){//删除存放token的cookie
                cookie.setMaxAge(0);
                cookie.setPath("/");
                cookie.setDomain("localhost");
                response.addCookie(cookie);
            }
        }
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
    }
}
