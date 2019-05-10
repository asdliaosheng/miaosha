package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.dataobject.MiaoshaOrder;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.dataobject.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.service.MiaoshaOrderService;
import com.imooc.miaosha.service.MiaoshaProductService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.utils.Const;
import com.imooc.miaosha.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/4/7 16:43
 */
@Slf4j
@RestController
@RequestMapping("/miaosha")
public class MiaoshaOrderController {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    //下订单,减库存
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    public ResultVO miaosha(HttpServletRequest request, HttpServletResponse response, String miaoshaProductId){
        //判断登录状态
        String token = UserUtil.checkSession(request);
        MiaoshaUser user = miaoshaUserService.getByToken(response, token);//拿到登录用户对象__一次读redis
        //查询商品,判断库存
        MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(miaoshaProductId);//一次mysql,取MiaoshaProduct
        if(miaoshaProduct.getMiaoshaProductStatus() != Const.ON_SALE){
            log.error("[%s]商品已下架或已删除",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.PRODUCT_NOT_ON_SALE);
        }
        int stock = miaoshaProduct.getMiaoshaProductStock();
        if(stock <= 0){
            log.error("[%s]商品库存不足",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.MIAO_SHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), miaoshaProductId);//两次mysql
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
        }
        //减库存下订单
        return ResultVO.success(miaoshaOrderService.miaosha(user, miaoshaProduct));//至此:四次mysql
    }

    /*
    * QPS <20
    */
    @RequestMapping(value = "/do_miaosha_test",method = RequestMethod.POST)
    public ResultVO miaoshaTest(HttpServletRequest request, HttpServletResponse response, String miaoshaProductId, String token){
        //判断登录状态
        //String token = UserUtil.checkSession(request);
        MiaoshaUser user = miaoshaUserService.getByToken(response, token);//拿到登录用户对象__一次读redis
        //查询商品,判断库存
        MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(miaoshaProductId);//一次mysql,取MiaoshaProduct
        if(miaoshaProduct.getMiaoshaProductStatus() != Const.ON_SALE){
            log.error("[%s]商品已下架或已删除",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.PRODUCT_NOT_ON_SALE);
        }
        int stock = miaoshaProduct.getMiaoshaProductStock();
        if(stock <= 0){
            log.error("[%s]商品库存不足",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.MIAO_SHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), miaoshaProductId);//两次mysql
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
        }
        //减库存下订单
        return ResultVO.success(miaoshaOrderService.miaosha(user, miaoshaProduct));//至此:四次mysql
    }
}
