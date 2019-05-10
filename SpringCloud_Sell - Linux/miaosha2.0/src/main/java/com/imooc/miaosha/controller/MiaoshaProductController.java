package com.imooc.miaosha.controller;

import com.imooc.miaosha.DTO.MiaoshaProductDTO;
import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.service.MiaoshaProductService;
import com.imooc.miaosha.utils.AccessUtil;
import com.imooc.miaosha.utils.Const;
import com.imooc.miaosha.utils.ResultVOUtil;
import com.imooc.miaosha.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.imooc.miaosha.service.MiaoshaUserService.COOKIE_NAME_TOKEN;

/**
 * create by ASheng
 * 2019/4/3 19:13
 */
@Slf4j
@RestController
@RequestMapping("/miaosha_product")
public class MiaoshaProductController {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserUtil.checkSession(request);
        List<MiaoshaProduct> miaoshaProductList = miaoshaProductService.getMiaoshaProductList();
        List<MiaoshaProductDTO> miaoshaProductDTOList = new ArrayList<>();
        for(MiaoshaProduct miaoshaProduct: miaoshaProductList){
            if(miaoshaProduct.getMiaoshaProductStatus() == Const.ON_SALE) {//只取在售秒杀商品
                MiaoshaProductDTO miaoshaProductDTO = new MiaoshaProductDTO();
                BeanUtils.copyProperties(miaoshaProduct, miaoshaProductDTO);
                miaoshaProductDTOList.add(miaoshaProductDTO);
            }
        }
        //return ResultVOUtil.success("查询秒杀商品列表成功", miaoshaProductDTOList);
        return ResultVO.success(CodeMsgVO.SUCCESS, miaoshaProductDTOList);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(HttpServletRequest request, HttpServletResponse response, String productId){
        AccessUtil.allowAccess(response);
        UserUtil.checkSession(request);
        MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(productId);
        if(miaoshaProduct == null){
            log.error("【按Id查询秒杀商品】操作失败");
            //return ResultVOUtil.fail("无该秒杀商品或操作失败", null);
            return ResultVO.error(CodeMsgVO.PRODUCT_NOT_EXIST);
        }else if(miaoshaProduct.getMiaoshaProductStatus() != Const.ON_SALE){
            log.error("【按Id查询秒杀商品】操作失败");
            //return ResultVOUtil.fail("该秒杀商品已下架或已删除", null);
            return ResultVO.error(CodeMsgVO.PRODUCT_NOT_ON_SALE);
        }else{
            //return ResultVOUtil.success("按Id查询商品成功",miaoshaProduct);
            return ResultVO.success(CodeMsgVO.SUCCESS, miaoshaProduct);
        }
    }

}
