package com.imooc.user.service;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.dataobject.Shipping;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.form.ShippingForm;
import com.imooc.user.form.UserForm;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/15 13:01
 */
public interface UserService {

    //注册方法
    Integer register(UserForm userForm) throws IOException;

    //登录
    UserDTO login(String userName, String userPassword);

    //检查用户名是否有效(str可以是用户名也可以是手机号。对应的type是username和userPhone)
    Integer checkValid(String str, String type);

    //获取用户登录信息
    UserDTO getUserInfo(String userPhone);

    //忘记密码获取问题
    String getQuestion(String userPhone);

    //提交问题答案(返回一个token)
    String checkAnswer(String userPhone, String userQuestion, String userAnswer);

    //忘记密码的重设密码
    Integer forgetResetPassword(String userPhone, String newPassword, String forgetToken);

    //登录状态中重设密码
    Integer resetPassword(String oldPassword, String newPassword, UserDTO userDTO);

    //登录状态更新个人信息
    Integer updateInformation(UserDTO userDTO,String userName, String userMail,String userPhone,String userQuestion,String userAnswer);

    //获取当前登录用户的详细信息，并强制登录
    UserForm getInformation(UserDTO userDTO);

    //退出登录
    Integer logout();

    //管理员查询用户列表
    List<UserInfo> list();

    //添加收货地址
    Shipping createShipping(ShippingForm shippingForm, String userId);

    //更新收货地址
    Shipping updateShipping(ShippingForm shippingForm, String shippingId, String userId);

    //查看收货地址
    Shipping shippingDetail(String shippingId);

    //查看收货地址列表
    List<Shipping> shippingList(String userId);

    //删除收货地址
    void deleteShipping(String shippingId);

}
