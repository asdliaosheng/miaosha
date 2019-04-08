package com.imooc.cart.controller;

import com.imooc.cart.DTO.CartDTO;
import com.imooc.cart.VO.ResultVO;
import com.imooc.cart.client.UserClient;
import com.imooc.cart.service.CartService;
import com.imooc.cart.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by ASheng
 * 2019/3/25 10:34
 */
@Slf4j
@RestController
@RequestMapping("/cart")
@Transactional
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserClient userClient;

    //有问题,不能在这取
    //private String userId = userClient.getUserId();

    //该方法同时供order服务调用???
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.list(userId);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO add(String productId, Integer productQuantity){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.add(userId, productId, productQuantity);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String productId, Integer productQuantity){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.update(userId, productId, productQuantity);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultVO delete(String productId){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.delete(userId, productId);
        }
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public ResultVO select(String productId){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.select(userId, productId);
        }
    }

    @RequestMapping(value = "/unselect", method = RequestMethod.POST)
    public ResultVO unselect(String productId){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.unselect(userId, productId);
        }
    }

    @RequestMapping(value = "/get_cart_product_count", method = RequestMethod.POST)
    public ResultVO getCartProductCount(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.getCartProductCount(userId);
        }
    }

    @RequestMapping(value = "/select_all", method = RequestMethod.POST)
    public ResultVO selectAll(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.selectAll(userId);
        }
    }

    @RequestMapping(value = "/unselect_all", method = RequestMethod.POST)
    public ResultVO unselectAll(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("用户未登录,请登录",null);
        }else {
            return cartService.unselectAll(userId);
        }
    }

    /*
    private Object getUserId(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return ResultVOUtil.fail("获取登录信息失败",null);
        }else{
            return null;
        }
    }
    */

    //该方法同时供order服务调用???
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public CartDTO search(){
        String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return null;
        }else {
            return cartService.search(userId);
        }
    }
}
