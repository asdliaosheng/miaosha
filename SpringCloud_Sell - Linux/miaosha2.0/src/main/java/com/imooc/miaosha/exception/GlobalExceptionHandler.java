package com.imooc.miaosha.exception;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.ResultVO;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * create by ASheng
 * 2019/4/6 15:07
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class)
    public ResultVO<String> exceptionHandler(HttpServletRequest request, Exception e){
        e.printStackTrace();
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return ResultVO.error(ex.getCodeMsgVO());
        }else if(e instanceof BindException){
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return ResultVO.error(CodeMsgVO.BIND_ERROR.fillArgs(msg));
        }else {
            return ResultVO.error(CodeMsgVO.SERVER_ERROR);
        }
    }
}
