package com.imooc.miaosha.exception;

import com.imooc.miaosha.VO.CodeMsgVO;

/**
 * create by ASheng
 * 2019/4/6 14:56
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsgVO codeMsgVO;

    public GlobalException(CodeMsgVO codeMsgVO){
        super(codeMsgVO.toString());
        this.codeMsgVO = codeMsgVO;
    }

    public CodeMsgVO getCodeMsgVO(){
        return codeMsgVO;
    }
}
