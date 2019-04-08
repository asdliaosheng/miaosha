package com.imooc.miaosha.utils;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static com.imooc.miaosha.service.MiaoshaUserService.COOKIE_NAME_TOKEN;

/**
 * create by ASheng
 * 2019/4/7 16:53
 */
@Slf4j
public class UserUtil {

    public static String checkSession(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            log.error("Session不存在或者已经失效");
            throw new GlobalException(CodeMsgVO.SESSION_ERROR);
        }
        String token = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_NAME_TOKEN)){//取到token
                token = cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            log.error("Session不存在或者已经失效");
            throw new GlobalException(CodeMsgVO.SESSION_ERROR);
        }
        return token;
    }
}
