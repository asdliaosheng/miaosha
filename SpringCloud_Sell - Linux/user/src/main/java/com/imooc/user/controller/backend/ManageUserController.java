package com.imooc.user.controller.backend;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.VO.CodeMsgVO;
import com.imooc.user.VO.ResultVO;
import com.imooc.user.common.Const;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.exception.UserException;
import com.imooc.user.redis.MiaoshaUserService;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.AccessUtil;
import com.imooc.user.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/17 9:49
 */
@Slf4j
@RestController
@RequestMapping("/manage/user")
public class ManageUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    private boolean managerCheck = false;

    /*
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO login(HttpServletRequest request, HttpSession session) {
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        if(userService.checkValid(userName,"userName") == 0){
            log.error("【检查用户名】用户名不存在");
            //throw new UserException(ResultEnum.USERNAME_NOT_EXISTS);
            return ResultVO.error(CodeMsgVO.USERNAME_NOT_EXIST);
        }
        UserDTO userDTO = userService.login(userName,userPassword);
        if(userDTO != null){
            managerCheck = true;
            session.setAttribute("currentUser", userDTO);
            return ResultVO.success(CodeMsgVO.SUCCESS, userDTO);
        }else{
            log.error("【登录错误】密码错误");
            return ResultVO.error(CodeMsgVO.PASSWORD_ERROR);
        }
    }
    */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        if(userDTO.getUserRole() != Const.Role.ROLE_ADMIN){
            return ResultVO.error(CodeMsgVO.NO_AUTHORITY);
        }
        List<UserInfo> userInfoList = userService.list();
        return ResultVO.success(CodeMsgVO.SUCCESS,userInfoList);
    }

    /*
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultVO logout(HttpSession session){
        managerCheck = false;
        session.removeAttribute("currentUser");
        return ResultVO.success(CodeMsgVO.SUCCESS,null);
    }
    */

    /*
    //检查是否为管理员权限,供其他服务调用
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Boolean check(){
        return managerCheck;//true为管理员已登录
    }
    */

    private UserDTO checkLogin(HttpServletRequest request, HttpServletResponse response){
        //判断登录状态
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo == null){
            log.error("登录已失效");
            throw new UserException(CodeMsgVO.TOKEN_ERROR);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        return userDTO;
    }
}
