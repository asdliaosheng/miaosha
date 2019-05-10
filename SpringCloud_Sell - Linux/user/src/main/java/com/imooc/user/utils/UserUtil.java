package com.imooc.user.utils;

import com.imooc.user.VO.CodeMsgVO;
import com.imooc.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * create by ASheng
 * 2019/4/7 16:53
 */
@Slf4j
public class UserUtil {

    //从cookie中判断该用户是否登录
    public static String checkSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.error("Session不存在或者已经失效");
            throw new UserException(CodeMsgVO.SESSION_ERROR);
        }
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {//取到token
                token = cookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)) {
            log.error("Session不存在或者已经失效");
            throw new UserException(CodeMsgVO.SESSION_ERROR);
        }
        return token;
    }

}
