package com.imooc.order.VO;

import lombok.Data;

/**
 * create by ASheng
 * 2019/3/15 16:15
 */
@Data
public class ResultVO<T> {

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //具体内容
    private T data;

    /**
     *  成功时候的调用
     * */
    public static  <T> ResultVO<T> success(T data){
        return new ResultVO<T>(CodeMsgVO.SUCCESS, data);
    }

    public static  <T> ResultVO<T> success(CodeMsgVO codeMsgVO, T data){
        return new ResultVO<T>(codeMsgVO, data);
    }

    /**
     *  失败时候的调用
     * */
    public static  <T> ResultVO<T> error(CodeMsgVO codeMsgVO){
        return new ResultVO<T>(codeMsgVO);
    }

    public static  <T> ResultVO<T> error(CodeMsgVO codeMsgVO, T data){
        return new ResultVO<T>(codeMsgVO, data);
    }

    private ResultVO(T data) {
        this.data = data;
    }

    public ResultVO() {
    }

    private ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVO(CodeMsgVO codeMsgVO) {
        if(codeMsgVO != null) {
            this.code = codeMsgVO.getCode();
            this.msg = codeMsgVO.getMsg();
        }
    }

    private ResultVO(CodeMsgVO codeMsgVO,  T data) {
        if(codeMsgVO != null) {
            this.code = codeMsgVO.getCode();
            this.msg = codeMsgVO.getMsg();
        }
        this.data = data;
    }
}