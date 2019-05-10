package com.imooc.user.controller.protal;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.VO.CodeMsgVO;
import com.imooc.user.VO.ResultVO;
import com.imooc.user.common.Const;
import com.imooc.user.dataobject.Shipping;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.exception.UserException;
import com.imooc.user.form.ShippingForm;
import com.imooc.user.form.UserForm;
import com.imooc.user.redis.MiaoshaUserService;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.AccessUtil;
import com.imooc.user.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    /*
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
    */

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultVO register(@Valid UserForm userForm,
                             BindingResult bindingResult,
                             HttpServletResponse response) throws IOException {
        AccessUtil.allowAccess(response);
        if(bindingResult.hasErrors()){
            log.error("【用户注册】参数不正确，userForm={}",userForm);
            throw new UserException(CodeMsgVO.BIND_ERROR);
        }
        if(userService.checkValid(userForm.getUserName(),Const.USERNAME) == 1){
            log.error("【检查用户名】用户名已存在");
            //throw new UserException(CodeMsgVO.USERNAME_ALREADY_EXIST);
            return ResultVO.error(CodeMsgVO.USERNAME_ALREADY_EXIST);
        }
        if(userService.checkValid(userForm.getUserPhone(),Const.USERPHONE) == 2){
            log.error("【检查手机号】手机号已存在");
            //throw new UserException(CodeMsgVO.USERPHONE_ALREADY_EXIST);
            return ResultVO.error(CodeMsgVO.USERPHONE_ALREADY_EXIST);
        }
        //UserForm --> User
        Integer result = userService.register(userForm);
        if(result == 0){
            return ResultVO.success(CodeMsgVO.SUCCESS, null);
        }else{
            //不可能出现,玄学错误
            return ResultVO.error(CodeMsgVO.SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/check_valid", method = RequestMethod.POST)
    public ResultVO checkValid(@RequestParam("str") String str, @RequestParam("type") String type,
                               HttpServletResponse response){
        AccessUtil.allowAccess(response);
        Integer result = userService.checkValid(str, type);
        if(result == 1){
            log.error("【检查用户名】用户名已存在");
            return ResultVO.error(CodeMsgVO.USERNAME_ALREADY_EXIST);
        }else if(result == 2){
            log.error("【检查手机号】手机号已存在");
            return ResultVO.error(CodeMsgVO.USERPHONE_ALREADY_EXIST);
        }else{
            return ResultVO.success(CodeMsgVO.SUCCESS, null);
        }
    }

    /*
    @RequestMapping(value = "/get_user_info", method = RequestMethod.POST)
    public ResultVO getUserInfo(HttpServletRequest request, HttpServletResponse response){
        //判断登录状态
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo == null){
            log.error("Token不存在或者已经失效");
            throw new UserException(CodeMsgVO.TOKEN_ERROR);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        return ResultVO.success(CodeMsgVO.SUCCESS, null);
    }
    */

    @RequestMapping(value = "/forget_get_question", method = RequestMethod.POST)
    public ResultVO forgetGetQuestion(@RequestParam("userPhone") String userPhone,
                                      HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(userService.checkValid(userPhone,"userPhone") == 0){
            log.error("【检查用户】手机号不存在");
            //throw new UserException(ResultEnum.USERNAME_NOT_EXISTS);
            return ResultVO.error(CodeMsgVO.USERPHONE_NOT_EXIST);
        }
        String question = userService.getQuestion(userPhone);
        if(!StringUtils.isEmpty(question)){
            return ResultVO.success(CodeMsgVO.SUCCESS, question);
        }else{
            log.error("【查询问题】该用户未设置找回密码问题");
            return ResultVO.error(CodeMsgVO.QUESTION_NOT_EXIST);
        }
    }

    @RequestMapping(value = "/forget_check_answer", method = RequestMethod.POST)
    public ResultVO forgetCheckAnswer(@RequestParam("userPhone") String userPhone,
                                      @RequestParam("userQuestion") String userQuestion,
                                      @RequestParam("userAnswer") String userAnswer,
                                      HttpServletResponse response){
        AccessUtil.allowAccess(response);
        String forgetToken = userService.checkAnswer(userPhone,userQuestion,userAnswer);
        if(forgetToken != null){
            return ResultVO.success(CodeMsgVO.SUCCESS, forgetToken);
        }else{
            return ResultVO.error(CodeMsgVO.ANSWER_ERROR);
        }
    }

    @RequestMapping(value = "/forget_reset_password", method = RequestMethod.POST)
    public ResultVO forgetResetPassword(@RequestParam("userPhone") String userPhone,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("forgetToken") String forgetToken,
                                        HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(userService.checkValid(userPhone, Const.USERPHONE) == 0){
            log.error("【检查手机号】手机号不存在");
            return ResultVO.error(CodeMsgVO.USERPHONE_NOT_EXIST);
        }
        Integer result = userService.forgetResetPassword(userPhone, newPassword, forgetToken);
        if(result == 0){
            return ResultVO.success(CodeMsgVO.SUCCESS, null);
        }else if(result == 1){
            log.error("Token不存在或者已经失效");
            throw new UserException(CodeMsgVO.TOKEN_ERROR);
        }else{
            log.error("【修改密码】Token不正确，修改密码操作失败");
            return ResultVO.error(CodeMsgVO.TOKEN_INCORRECT);
        }
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public ResultVO resetPassword(String oldPassword, String newPassword,
                                  HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        if(userService.resetPassword(oldPassword, newPassword, userDTO) == 0){
            return ResultVO.success(CodeMsgVO.SUCCESS, null);
        }else{
            log.error("【修改密码】旧密码输入错误");
            return ResultVO.error(CodeMsgVO.OLD_PASSWORD_ERROR);
        }
    }

    @RequestMapping(value = "/update_information", method = RequestMethod.POST)
    public ResultVO updateInformation(String userName, String userMail,String userPhone,String userQuestion,String userAnswer,
                                      HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        if(userService.checkValid(userName,Const.USERNAME) == 1){
            log.error("【检查用户名】用户名已存在");
            //throw new UserException(CodeMsgVO.USERNAME_ALREADY_EXIST);
            return ResultVO.error(CodeMsgVO.USERNAME_ALREADY_EXIST);
        }
        if(userService.checkValid(userPhone,Const.USERPHONE) == 2){
            log.error("【检查手机号】手机号已存在");
            //throw new UserException(CodeMsgVO.USERPHONE_ALREADY_EXIST);
            return ResultVO.error(CodeMsgVO.USERPHONE_ALREADY_EXIST);
        }
        userService.updateInformation(userDTO, userName, userMail, userPhone, userQuestion, userAnswer);
        return ResultVO.success(CodeMsgVO.SUCCESS, null);
    }

    @RequestMapping(value = "/get_information", method = RequestMethod.POST)
    public ResultVO getInformation(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        return ResultVO.success(CodeMsgVO.SUCCESS, userService.getInformation(userDTO));
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResultVO checkLoginStatus(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        return ResultVO.success(CodeMsgVO.SUCCESS, userService.getInformation(userDTO));
    }
    /*
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultVO logout(HttpSession session){
        currentUserId = null;
        session.removeAttribute(Const.CURRENT_USER);
        return ResultVOUtil.success("登出成功",null);
    }
    */

    @RequestMapping(value = "/create_shipping", method = RequestMethod.POST)
    public ResultVO createShipping(ShippingForm shippingForm,
                                   HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        return ResultVO.success(userService.createShipping(shippingForm, userDTO.getUserId()));
        //throw new RuntimeException("故意出错");
    }

    @RequestMapping(value = "/update_shipping", method = RequestMethod.POST)
    public ResultVO updateShipping(ShippingForm shippingForm, String shippingId,
                                   HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        Shipping shipping = userService.updateShipping(shippingForm, shippingId, userDTO.getUserId());
        if(shipping == null){
            return ResultVO.error(CodeMsgVO.SHIPPING_NOT_EXIST);
        }else {
            return ResultVO.success(shipping);
        }

    }

    @RequestMapping(value = "/shipping_detail", method = RequestMethod.POST)
    public ResultVO shippingDetail(String shippingId,
                                   HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        Shipping shipping = userService.shippingDetail(shippingId);
        if(shipping == null){
            return ResultVO.error(CodeMsgVO.SHIPPING_NOT_EXIST);
        }else {
            return ResultVO.success(shipping);
        }
    }

    @RequestMapping(value = "/shipping_list", method = RequestMethod.POST)
    public ResultVO shippingList(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        return ResultVO.success(userDTO);
    }

    @RequestMapping(value = "/delete_shipping", method = RequestMethod.POST)
    public ResultVO deleteShipping(String shippingId,
                                   HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        userService.deleteShipping(shippingId);
        return ResultVO.success(null);
    }


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


    /*
    //供其他服务调用
    @RequestMapping(value = "/get_user_id", method = RequestMethod.GET)
    public String getUserId(){
        return currentUserId;
    }
    */

}
