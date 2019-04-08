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
    private String username;

    //密码
    @NotEmpty(message = "密码必填")
    private String password;

    //邮箱
    @NotEmpty(message = "邮箱必填")
    private String email;

    //电话
    @NotEmpty(message = "电话必填")
    private String phone;

    //问题
    //@NotEmpty(message = "问题必填")
    private String question;

    //答案
    //@NotEmpty(message = "回答必填")
    private String answer;

    public UserForm(String username, String password, String email, String phone, String question, String answer) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
    }

    public UserForm() {
    }
}
