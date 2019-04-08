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
import com.netflix.discovery.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(userInfo.getUserPassword().equals(userPassword)){
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
    public Integer register(UserForm userForm) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(KeyUtil.getUniqueKey());
        userInfo.setUserName(userForm.getUsername());
        userInfo.setUserPassword(userForm.getPassword());
        userInfo.setUserMail(userForm.getEmail());
        userInfo.setUserPhone(userForm.getPhone());
        userInfo.setUserQuestion(userForm.getQuestion());
        userInfo.setUserAnswer(userForm.getAnswer());
        userInfo.setUserRole(Const.Role.ROLE_CUSTOMER);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userRepository.save(userInfo);
        return 0;
    }

    //检查用户名是否有效(str可以是用户名也可以是email。对应的type是username和email) 0-成功 1-已存在
    @Override
    public Integer checkValid(String str, String type) {
        if(type.equals("username")){
            UserInfo userInfo = userRepository.findByUserName(str);
            if(userInfo != null){
                //log.error("【检查用户名】用户名已存在");
                return 1;
            }else{
                return 0;
            }
        }else{
            UserInfo userInfo = userRepository.findByUserMail(str);
            if(userInfo != null){
                //log.error("【检查用户邮箱】该邮箱已存在");
                return 1;
            }else{
                return 0;
            }
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
    public String getQuestion(String userName) {
        UserInfo userInfo = userRepository.findByUserName(userName);
        if(!StringUtils.isEmpty(userInfo.getUserQuestion())){
            return userInfo.getUserQuestion();
        }else{
            //log.error("【问题为空】该用户未设置问题");
            return null;
        }

    }

    @Override
    public String checkAnswer(String userName, String userQuestion, String userAnswer) {
        UserInfo userInfo = userRepository.findByUserName(userName);
        if(userInfo.getUserAnswer().equals(userAnswer)){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userName,forgetToken);
            return forgetToken;//返回一个token
        }else{
            //log.error("【回答错误】问题回答错误");
            return null;
        }

    }

    @Override
    public Integer forgetResetPassword(String userName, String newPassword, String forgetToken) {
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userName);
        if(StringUtils.isBlank(token)){
            return 1;
        }
        if(forgetToken.equals(token)){
            UserInfo userInfo = userRepository.findByUserName(userName);
            userInfo.setUserPassword(newPassword);
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
        UserInfo userInfo = userRepository.findByUserName(userDTO.getUserName());
        if(oldPassword.equals(userInfo.getUserPassword())){
            userInfo.setUserPassword(newPassword);
            userRepository.save(userInfo);
            return 0;//成功
        }else{
            return 1;
        }
    }

    //登录状态更新个人信息
    @Override
    public Integer updateInformation(UserDTO userDTO, String userMail, String userPhone, String userQuestion, String userAnswer) {
        UserInfo userInfo = userRepository.findByUserName(userDTO.getUserName());
        userInfo.setUserMail(userMail);
        userInfo.setUserPhone(userPhone);
        userInfo.setUserQuestion(userQuestion);
        userInfo.setUserAnswer(userAnswer);
        userRepository.save(userInfo);
        return 0;
    }

    //获取当前登录用户的详细信息，并强制登录
    @Override
    public UserInfo getInformation(UserDTO userDTO) {
        UserInfo userInfo = userRepository.findByUserName(userDTO.getUserName());
        userInfo.setUserPassword("");
        return userInfo;
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
