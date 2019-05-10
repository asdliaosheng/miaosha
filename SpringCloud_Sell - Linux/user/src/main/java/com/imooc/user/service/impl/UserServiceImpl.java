package com.imooc.user.service.impl;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.common.Const;
import com.imooc.user.common.TokenCache;
import com.imooc.user.dataobject.Shipping;
import com.imooc.user.dataobject.UserInfo;
import com.imooc.user.form.ShippingForm;
import com.imooc.user.form.UserForm;
import com.imooc.user.repository.ShippingRepository;
import com.imooc.user.repository.UserRepository;
import com.imooc.user.service.UserService;
import com.imooc.user.utils.KeyUtil;
import com.imooc.user.utils.MD5Util;
import com.netflix.discovery.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * create by ASheng
 * 2019/3/15 16:29
 */
@Service
//@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public UserDTO login(String userName, String userPassword) {
        UserInfo userInfo = userRepository.findByUserName(userName);
        if(userInfo.getUserPassword().equals(MD5Util.inputPassToDBPass(userPassword, userInfo.getSalt()))){
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userInfo, userDTO);
            return userDTO;
        }else{
            //密码错误
            //log.error("【登录错误】密码错误");
            return null;
        }
    }

    @Override
    public Integer register(UserForm userForm) throws UnsupportedEncodingException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userForm.getUserPhone());
        userInfo.setUserName(userForm.getUserName());
        userInfo.setUserPassword(MD5Util.inputPassToDBPass(userForm.getUserPassword(), userForm.getSalt()));
        userInfo.setSalt(userForm.getSalt());
        userInfo.setUserMail(userForm.getUserMail());
        userInfo.setUserPhone(userForm.getUserPhone());
        userInfo.setUserQuestion(userForm.getUserQuestion());
        userInfo.setUserAnswer(MD5Util.inputPassToDBPass(URLEncoder.encode(userForm.getUserAnswer(),"UTF-8"), userForm.getSalt()));
        //userInfo.setUserAnswer(MD5Util.inputPassToDBPass(userForm.getAnswer(),userForm.getSalt()));
        userInfo.setUserRole(Const.Role.ROLE_CUSTOMER);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userRepository.save(userInfo);
        return 0;
    }

    //检查用户名是否有效(str可以是用户名也可以是phone。对应的type是username和phone) 0-成功 1-已存在
    @Override
    public Integer checkValid(String str, String type) {
        if(type.equals("userName")){
            UserInfo userInfo = userRepository.findByUserName(str);
            if(userInfo != null){
                //log.error("【检查用户名】用户名已存在");
                return 1;
            }else{
                return 0;
            }
        }else if(type.equals("userPhone")){
            UserInfo userInfo = userRepository.findByUserPhone(str);
            if(userInfo != null){
                //log.error("【检查用户电话号码】该电话号码已存在");
                return 2;
            }else{
                return 0;
            }
        }else{
            return null;
        }
    }

    @Override
    //返回该登录用户信息,无参数传进(未用到)
    public UserDTO getUserInfo(String userName) {
        UserInfo userInfo = userRepository.findByUserName(userName);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        return userDTO;
    }

    @Override
    public String getQuestion(String userPhone) {
        UserInfo userInfo = userRepository.findByUserPhone(userPhone);
        return userInfo.getUserQuestion();
    }

    @Override
    public String checkAnswer(String userPhone, String userQuestion, String userAnswer) {
        UserInfo userInfo = userRepository.findByUserPhone(userPhone);
        String dbUserAnswer = null;
        try{
            dbUserAnswer = MD5Util.inputPassToDBPass(URLEncoder.encode(userAnswer, "UTF-8"), userInfo.getSalt());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        //dbUserAnswer = MD5Util.inputPassToDBPass(userAnswer, userInfo.getSalt());
        if(userInfo.getUserAnswer().equals(dbUserAnswer)){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userPhone,forgetToken);
            return forgetToken;//返回一个token
        }else{
            //log.error("【回答错误】问题回答错误");
            return null;
        }

    }

    @Override
    public Integer forgetResetPassword(String userPhone, String newPassword, String forgetToken) {
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userPhone);
        if(StringUtils.isBlank(token)){
            return 1;
        }
        if(forgetToken.equals(token)){
            UserInfo userInfo = userRepository.findByUserPhone(userPhone);
            userInfo.setUserPassword(MD5Util.inputPassToDBPass(newPassword, userInfo.getSalt()));
            userRepository.save(userInfo);
            return 0;
        }else{
            //log.error("【修改密码】修改密码失败");
            return 2;
        }
    }

    //登录中状态重置密码
    @Override
    public Integer resetPassword(String oldPassword, String newPassword, UserDTO userDTO) {
        UserInfo userInfo = userRepository.findByUserPhone(userDTO.getUserPhone());
        String dbOldPassword = MD5Util.inputPassToDBPass(oldPassword, userInfo.getSalt());
        String dbNewPassword = MD5Util.inputPassToDBPass(newPassword, userInfo.getSalt());
        if(dbOldPassword.equals(userInfo.getUserPassword())){
            userInfo.setUserPassword(dbNewPassword);
            userRepository.save(userInfo);
            return 0;//成功
        }else{
            return 1;
        }
    }

    //登录状态更新个人信息
    @Override
    public Integer updateInformation(UserDTO userDTO, String userName, String userMail,String userPhone,String userQuestion,String userAnswer) {
        UserInfo userInfo = userRepository.findByUserId(userDTO.getUserId());
        userInfo.setUserName(userName);
        userInfo.setUserMail(userMail);
        userInfo.setUserPhone(userPhone);
        userInfo.setUserQuestion(userQuestion);
        try{
            userInfo.setUserAnswer(MD5Util.inputPassToDBPass(URLEncoder.encode(userAnswer, "UTF-8"), userInfo.getSalt()));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        userRepository.save(userInfo);
        return 0;
    }

    //获取当前登录用户的详细信息，并强制登录
    @Override
    public UserForm getInformation(UserDTO userDTO) {
        UserInfo userInfo = userRepository.findByUserId(userDTO.getUserId());
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(userInfo, userForm);
        userForm.setUserPassword("系统消息：不给看不给看");
        userForm.setUserAnswer("系统消息：不给看不给看");
        return userForm;
    }

    //退出登录(未调用)
    @Override
    public Integer logout() {
        return null;
    }

    @Override
    public List<UserInfo> list(){
        List<UserInfo> userInfoList = userRepository.findByUserRole(Const.Role.ROLE_CUSTOMER);
        for(UserInfo userInfo: userInfoList){
            userInfo.setUserPassword("");
        }
        return userInfoList;
    }

    @Transactional
    @Override
    public Shipping createShipping(ShippingForm shippingForm, String userId) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm, shipping);
        shipping.setShippingId(KeyUtil.getUniqueKey());
        shipping.setUserId(userId);
        shipping.setCreateTime(new Date());
        shipping.setUpdateTime(new Date());
        return shippingRepository.save(shipping);
//        if(org.springframework.util.StringUtils.isEmpty(new Shipping())){
//            try{
//                System.out.println("执行");
//                throw new RuntimeException("故意出错");
//            }catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return shippingRepository.save(shipping);
//        return new Shipping();
    }

    @Override
    public Shipping updateShipping(ShippingForm shippingForm, String shippingId, String userId) {
        Shipping shipping = shippingRepository.findByShippingId(shippingId);
        if(userId.equals(shipping.getUserId())) {
            BeanUtils.copyProperties(shippingForm, shipping);
            shipping.setUpdateTime(new Date());
            return shippingRepository.save(shipping);
        }else{
            return null;
        }
    }

    @Override
    public Shipping shippingDetail(String shippingId) {
        return shippingRepository.findByShippingId(shippingId);
    }

    @Override
    public List<Shipping> shippingList(String userId) {
        return shippingRepository.findByUserId(userId);
    }

    @Override
    public void deleteShipping(String shippingId) {
        shippingRepository.deleteById(shippingId);
    }
}
