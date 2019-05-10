package com.imooc.product.VO;

import lombok.Data;

/**
 * create by ASheng
 * 2019/4/18 11:00
 */
@Data
public class CodeMsgVO {

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //通用错误码
    public static CodeMsgVO SUCCESS = new CodeMsgVO(0,"success");
    public static CodeMsgVO SERVER_ERROR = new CodeMsgVO(500100, "服务端异常");
    public static CodeMsgVO BIND_ERROR = new CodeMsgVO(500101, "参数校验异常");
    public static CodeMsgVO ADMIN_NOT_LOGIN = new CodeMsgVO(50020, "管理员未登录");

    //用户错误码
    public static CodeMsgVO USERNAME_ALREADY_EXIST = new CodeMsgVO(400200, "用户名已存在");
    public static CodeMsgVO USERPHONE_ALREADY_EXIST = new CodeMsgVO(400201, "手机号已存在");
    public static CodeMsgVO TOKEN_ERROR = new CodeMsgVO(400202, "Token不存在或者已经失效");
    public static CodeMsgVO USERPHONE_NOT_EXIST = new CodeMsgVO(400203, "手机号不存在");
    public static CodeMsgVO QUESTION_NOT_EXIST = new CodeMsgVO(400204, "找回密码问题不存在");
    public static CodeMsgVO ANSWER_ERROR = new CodeMsgVO(400205, "找回密码答案错误");
    public static CodeMsgVO TOKEN_INCORRECT = new CodeMsgVO(400206, "token不正确");
    public static CodeMsgVO USER_NOT_LOGIN = new CodeMsgVO(400207, "用户未登录");
    public static CodeMsgVO OLD_PASSWORD_ERROR = new CodeMsgVO(400208, "旧密码错误");
    public static CodeMsgVO SESSION_ERROR = new CodeMsgVO(400209, "Session不存在或者已经失效");
    public static CodeMsgVO USERNAME_NOT_EXIST = new CodeMsgVO(4002010, "用户名不存在");
    public static CodeMsgVO PASSWORD_ERROR = new CodeMsgVO(400211, "密码错误");
    public static CodeMsgVO NO_AUTHORITY = new CodeMsgVO(400212, "没有权限");

    public static CodeMsgVO SHIPPING_NOT_EXIST = new CodeMsgVO(400209, "收货地址不存在");

    //商品错误码
    public static CodeMsgVO NO_PARAMETER = new CodeMsgVO(400300, "无参数传递");
    public static CodeMsgVO PRODUCT_NOT_EXIST = new CodeMsgVO(400301, "商品不存在");
    public static CodeMsgVO PRODUCT_STATUS_ERROR = new CodeMsgVO(400302, "商品已下架或已删除");
    public static CodeMsgVO PRODUCT_LIST_IS_EMPTY = new CodeMsgVO(400303, "商品列表为空");
    public static CodeMsgVO CU_PRODUCT_ERROR = new CodeMsgVO(400304, "创建或更新商品失败");
    public static CodeMsgVO PRODUCT_ALREADY_EXIST = new CodeMsgVO(400305, "商品已存在");

    //类目错误码
    public static CodeMsgVO ADD_CATEGORY_ERROR = new CodeMsgVO(400320, "添加品类失败");
    public static CodeMsgVO UPDATE_CATEGORY_NAME_ERROR = new CodeMsgVO(400321, "更新品类名字失败");
    public static CodeMsgVO SUBCATEGORY_NOT_EXIST = new CodeMsgVO(400322, "该品类子类不存在");
    public static CodeMsgVO CATEGORY_NOT_EXIST = new CodeMsgVO(400323, "该品类不存在");

    public CodeMsgVO() {
    }

    public CodeMsgVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsgVO fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsgVO(code, message);
    }
}
