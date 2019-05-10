package com.imooc.user.service;

import com.imooc.user.DTO.UserDTO;
import com.imooc.user.UserApplicationTests;
import com.imooc.user.form.UserForm;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * create by ASheng
 * 2019/3/15 17:14
 */
public class UserServiceTest extends UserApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void register() throws IOException {
        UserForm userForm = new UserForm("风太郎","123fengtailang","1a2b3c4d","fengtailang@jingxi.com",
                "18100001000","风太郎的问题","风太郎的答案");
        Integer result = userService.register(userForm);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void login() {
        UserDTO userDTO = userService.login("1sanjiu", "123sanjiu");
        Assert.assertTrue(userDTO.getUserMail().equals("sanjiu@jingxi.com"));
    }

    @Test
    public void checkUserName() {
        Integer result = userService.checkValid("yihua", "username");
        Assert.assertTrue(result==1);
    }

    @Test
    public void getUserInfo() {
        UserDTO userDTO = userService.getUserInfo("18200000001");
        Assert.assertNotNull(userDTO.getUserId());
    }

    @Test
    public void forgetGetQuestion() {
        String question = userService.getQuestion("18200000001");
        Assert.assertNotNull(question);
    }

    @Test
    public void forgetCheckAnswer() {
        String token = userService.checkAnswer("18200000001", "一花的问题","一花的答案");
        Assert.assertNotNull(token);
    }

    @Test
    public void forgetResetPassword() {
        int code = userService.forgetResetPassword("18200000001","123ernai","xxx");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void resetPassword() {
    }

    @Test
    public void updateInformation() {
    }

    @Test
    public void getInformation() {
    }

    @Test
    public void logout() {
    }

//    @Test
//    public void delete() {
//        userService.de
//    }
}