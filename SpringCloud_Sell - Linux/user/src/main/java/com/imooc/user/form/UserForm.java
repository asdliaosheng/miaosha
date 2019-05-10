package com.imooc.user.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 用户注册表单对象
 * create by ASheng
 * 2019/3/18 12:15
 */
@Data
public class UserForm {

    //用户名
    @NotEmpty(message = "用户名必填")
    private String userName;

    //密码
    @NotEmpty(message = "密码必填")
    private String userPassword;

    //密码
    @NotEmpty(message = "salt必填")
    private String salt;

    //邮箱
    @NotEmpty(message = "邮箱必填")
    private String userMail;

    //电话
    @NotEmpty(message = "电话必填")
    private String userPhone;

    //问题
    //@NotEmpty(message = "问题必填")
    private String userQuestion;

    //答案
    //@NotEmpty(message = "回答必填")
    private String userAnswer;

    public UserForm(String userName, String userPassword, String salt, String userMail, String userPhone, String userQuestion, String userAnswer) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.salt = salt;
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.userQuestion = userQuestion;
        this.userAnswer = userAnswer;
    }

    public UserForm() {
    }
}
