package com.imooc.cart.common;

import com.imooc.cart.VO.CodeMsgVO;

/**
 * create by ASheng
 * 2019/3/18 12:44
 */
public class UserException extends RuntimeException {

    private CodeMsgVO codeMsgVO;

    public UserException(CodeMsgVO codeMsgVO){
        super(codeMsgVO.toString());
        this.codeMsgVO = codeMsgVO;
    }

    public CodeMsgVO getCodeMsgVO(){
        return codeMsgVO;
    }
}
