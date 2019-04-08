package com.imooc.user.controller.protal;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.VO.ResultVO;
import com.imooc.user.common.Const;
import com.imooc.user.dataobject.Shipping;
import com.imooc.user.enums.ResultEnum;
import com.imooc.user.exception.UserException;
import com.imooc.user.form.ShippingForm;
import com.imooc.user.form.UserForm;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * create by ASheng
 * 2019/3/17 9:43
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    //提供接口
    private String currentUserId = null;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO login(HttpServletRequest request, HttpSession session){
        String userName = request.getParameter("username");
        String userPassword = request.getParameter("password");
        if(userService.checkValid(userName,"username") == 0){
            log.error("【检查用户名】用户名不存在");
            //throw new UserException(ResultEnum.USERNAME_NOT_EXISTS);
            return ResultVOUtil.fail("用户名不存在", null);
        }
        UserDTO userDTO = userService.login(userName,userPassword);
        if(userDTO != null){
            currentUserId = userDTO.getUserId();
            session.setAttribute("currentUser", userDTO);
            return ResultVOUtil.success("登录成功", userDTO);
        }else{
            log.error("【登录错误】密码错误");
            return ResultVOUtil.fail("密码错误", null);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultVO register(@Valid UserForm userForm,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("【用户注册】参数不正确，userForm={}",userForm);
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        if(userService.checkValid(userForm.getUsername(),Const.USERNAME) == 1){
            log.error("【检查用户名】用户名已存在");
            //throw new UserException(ResultEnum.USERNAME_ALREADY_EXISTS);
            return ResultVOUtil.fail("用户名已存在",null);
        }
        //UserForm --> User
        Integer result = userService.register(userForm);
        if(result == 0){
            return ResultVOUtil.success("注册成功", null);
        }else{
            //不可能出现
            return ResultVOUtil.fail("未知错误",null);
        }
    }

    @RequestMapping(value = "/check_valid", method = RequestMethod.POST)
    public ResultVO checkValid(@RequestParam("str") String str, @RequestParam("type") String type){
        Integer result = userService.checkValid(str, type);
        if(result == 1){
            log.error("【检查用户】用户已存在");
            return ResultVOUtil.fail("用户已存在",null);
        }else{
            return ResultVOUtil.success("检验成功", null);
        }
    }

    @RequestMapping(value = "/get_user_info", method = RequestMethod.POST)
    public ResultVO getUserInfo(HttpSession session){
        UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");
        if(userDTO != null){
            return ResultVOUtil.success("获取登录信息成功",userDTO);
        }else{
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,无法获取当前用户信息",null);
        }
    }

    @RequestMapping(value = "/forget_get_question", method = RequestMethod.POST)
    public ResultVO forgetGetQuestion(@RequestParam("username") String userName){
        if(userService.checkValid(userName,"username") == 0){
            log.error("【检查用户名】用户名不存在");
            //throw new UserException(ResultEnum.USERNAME_NOT_EXISTS);
            return ResultVOUtil.fail("用户名不存在", null);
        }
        String question = userService.getQuestion(userName);
        if(question != null){
            return ResultVOUtil.success("查询成功", question);
        }else{
            log.error("【查询问题】该用户未设置找回密码问题");
            return ResultVOUtil.fail("该用户未设置找回密码问题", null);
        }
    }

    @RequestMapping(value = "/forget_check_answer", method = RequestMethod.POST)
    public ResultVO forgetCheckAnswer(@RequestParam("username") String userName,
                                      @RequestParam("question") String userQuestion,
                                      @RequestParam("answer") String userAnswer){
        String forgetToken = userService.checkAnswer(userName,userQuestion,userAnswer);
        if(forgetToken != null){
            return ResultVOUtil.success("问题校验成功",forgetToken);
        }else{
            return ResultVOUtil.fail("问题答案错误", null);
        }
    }

    @RequestMapping(value = "/forget_reset_password", method = RequestMethod.POST)
    public ResultVO forgetResetPassword(@RequestParam("username") String userName,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("forgetToken") String forgetToken){
        if(userService.checkValid(userName, Const.USERNAME) == 0){
            log.error("【检查用户名】用户名不存在");
            return ResultVOUtil.fail("用户名不存在", null);
        }
        Integer result = userService.forgetResetPassword(userName, newPassword, forgetToken);
        if(result == 0){
            return ResultVOUtil.success("修改密码成功", null);
        }else if(result == 1){
            log.error("【token检查】token无效或者过期");
            return ResultVOUtil.fail("token无效或者过期",null);
        }else{
            log.error("【修改密码】修改密码操作失败");
            return ResultVOUtil.fail("修改密码操作失败",null);
        }
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public ResultVO resetPassword(HttpSession session, String oldPassword, String newPassword){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,无法获取当前用户信息",null);
        }else{
            if(userService.resetPassword(oldPassword, newPassword, userDTO) == 0){
                return ResultVOUtil.success("修改密码成功",null);
            }else{
                log.error("【修改密码】旧密码输入错误");
                return ResultVOUtil.fail("旧密码输入错误",null);
            }
        }
    }

    @RequestMapping(value = "/update_information", method = RequestMethod.POST)
    public ResultVO updateInformation(HttpSession session, String email,String phone,String question,String answer){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录",null);
        }else{
            userService.updateInformation(userDTO, email, phone, question, answer);
            return ResultVOUtil.success("更新个人信息成功",null);
        }
    }

    @RequestMapping(value = "/get_information", method = RequestMethod.POST)
    public ResultVO getInformation(HttpSession session){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,无法获取当前用户信息,status=10,强制登录",null);
        }else{
            return ResultVOUtil.success("获取信息成功",userService.getInformation(userDTO));
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultVO logout(HttpSession session){
        currentUserId = null;
        session.removeAttribute(Const.CURRENT_USER);
        return ResultVOUtil.success("登出成功",null);
    }

    @RequestMapping(value = "/create_shipping", method = RequestMethod.POST)
    public ResultVO createShipping(HttpSession session, ShippingForm shippingForm){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,强制登录",null);
        }else{
            return ResultVOUtil.success("创建收货地址信息成功",userService.createShipping(shippingForm, userDTO.getUserId()));
            //throw new RuntimeException("故意出错");
        }
    }

    @RequestMapping(value = "/update_shipping", method = RequestMethod.POST)
    public ResultVO updateShipping(HttpSession session, ShippingForm shippingForm, String shippingId){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,强制登录",null);
        }else{
            Shipping shipping = userService.updateShipping(shippingForm, shippingId, userDTO.getUserId());
            if(shipping == null){
                return ResultVOUtil.fail("更新收货地址信息失败",null);
            }else {
                return ResultVOUtil.success("更新收货地址信息成功", shipping);
            }
        }
    }

    @RequestMapping(value = "/shipping_detail", method = RequestMethod.POST)
    public ResultVO shippingDetail(HttpSession session, String shippingId){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,强制登录",null);
        }else{
            Shipping shipping = userService.shippingDetail(shippingId);
            if(shipping == null){
                return ResultVOUtil.fail("获取收货地址信息失败",null);
            }else {
                return ResultVOUtil.success("获取收货地址信息成功", shipping);
            }
        }
    }

    @RequestMapping(value = "/shipping_list", method = RequestMethod.POST)
    public ResultVO shippingList(HttpSession session){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,强制登录",null);
        }else{
            return ResultVOUtil.success("获取收货地址信息列表成功",userService.shippingList(userDTO.getUserId()));
        }
    }

    @RequestMapping(value = "/delete_shipping", method = RequestMethod.POST)
    public ResultVO deleteShipping(HttpSession session, String shippingId){
        UserDTO userDTO = (UserDTO) session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("用户未登录,强制登录",null);
        }else{
            userService.deleteShipping(shippingId);
            return ResultVOUtil.success("删除收货地址信息成功",null);
        }
    }

    //供其他服务调用
    @RequestMapping(value = "/get_user_id", method = RequestMethod.GET)
    public String getUserId(){
        return currentUserId;
    }


}
