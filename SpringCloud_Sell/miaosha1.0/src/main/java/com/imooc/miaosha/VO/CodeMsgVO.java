package com.imooc.miaosha.VO;

import lombok.Data;

/**
 * create by ASheng
 * 2019/4/6 14:59
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
    public static CodeMsgVO BIND_ERROR = new CodeMsgVO(500101, "参数校验异常：%s");
    //登录模块 5002XX
    public static CodeMsgVO LOGIN_SUCCESS = new CodeMsgVO(500200, "登录成功");
    public static CodeMsgVO SESSION_ERROR = new CodeMsgVO(500210, "Session不存在或者已经失效");
    public static CodeMsgVO PASSWORD_EMPTY = new CodeMsgVO(500211, "登录密码不能为空");
    public static CodeMsgVO MOBILE_EMPTY = new CodeMsgVO(500212, "手机号不能为空");
    public static CodeMsgVO MOBILE_ERROR = new CodeMsgVO(500213, "手机号格式错误");
    public static CodeMsgVO MOBILE_NOT_EXIST = new CodeMsgVO(500214, "手机号不存在");
    public static CodeMsgVO PASSWORD_ERROR = new CodeMsgVO(500215, "密码错误");
    //商品模块 5003XX
    public static CodeMsgVO PRODUCT_NOT_EXIST = new CodeMsgVO(500300, "无该秒杀商品或操作失败");
    public static CodeMsgVO PRODUCT_NOT_ON_SALE = new CodeMsgVO(500301, "该秒杀商品已下架或已删除");
    //订单模块 5004XX
    public static CodeMsgVO ORDER_NOT_EXIST = new CodeMsgVO(500400, "订单不存在");

    //秒杀模块 5005XX
    public static CodeMsgVO MIAO_SHA_OVER = new CodeMsgVO(500500, "商品已经秒杀完毕");
    public static CodeMsgVO REPEATE_MIAOSHA = new CodeMsgVO(500501, "不能重复秒杀");


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
