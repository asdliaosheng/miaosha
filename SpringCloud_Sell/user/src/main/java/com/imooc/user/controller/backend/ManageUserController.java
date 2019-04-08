package com.imooc.user.controller.backend;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.VO.ResultVO;
import com.imooc.user.common.Const;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    private boolean managerCheck = false;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO login(HttpServletRequest request, HttpSession session) {
        String userName = request.getParameter("username");
        String userPassword = request.getParameter("password");
        if(userService.checkValid(userName,"username") == 0){
            log.error("【检查用户名】用户名不存在");
            //throw new UserException(ResultEnum.USERNAME_NOT_EXISTS);
            return ResultVOUtil.fail("用户名不存在", null);
        }
        UserDTO userDTO = userService.login(userName,userPassword);
        if(userDTO != null){
            managerCheck = true;
            session.setAttribute("currentUser", userDTO);
            return ResultVOUtil.success("登录成功", userDTO);
        }else{
            log.error("【登录错误】密码错误");
            return ResultVOUtil.fail("密码错误", null);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(HttpSession session){
        UserDTO userDTO = (UserDTO)session.getAttribute(Const.CURRENT_USER);
        if(userDTO.getUserRole() != Const.Role.ROLE_ADMIN){
            return ResultVOUtil.fail("没有权限",null);
        }
        List<UserInfo> userInfoList = userService.list();
        return ResultVOUtil.success("查询用户列表成功",userInfoList);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultVO logout(HttpSession session){
        managerCheck = false;
        session.removeAttribute("currentUser");
        return ResultVOUtil.success("登出成功",null);
    }

    //检查是否为管理员权限,供其他服务调用
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Boolean check(){
        return managerCheck;//true为管理员已登录
    }
}
