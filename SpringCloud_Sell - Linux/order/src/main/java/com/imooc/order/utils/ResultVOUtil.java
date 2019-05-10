package com.imooc.order.utils;

import com.imooc.order.VO.ResultVO;

/**
 * create by ASheng
 * 2019/3/17 11:36
 */
public class ResultVOUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(object);
        return resultVO;
    }

}
