package com.imooc.cart.controller;

import com.imooc.cart.DTO.CartDTO;
import com.imooc.cart.VO.CodeMsgVO;
import com.imooc.cart.VO.ResultVO;
import com.imooc.cart.common.UserDTO;
import com.imooc.cart.common.UserException;
import com.imooc.cart.common.UserInfo;
import com.imooc.cart.common.UserUtil;
import com.imooc.cart.redis.MiaoshaUserService;
import com.imooc.cart.service.CartService;
import com.imooc.cart.utils.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private MiaoshaUserService miaoshaUserService;

//    @Autowired
//    private UserClient userClient;

    //有问题,不能在这取
    //private String userId = userClient.getUserId();

    //该方法同时供order服务调用???
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.list(userId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO add(String productId, Integer productQuantity,
                        HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.add(userId, productId, productQuantity);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String productId, Integer productQuantity,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.update(userId, productId, productQuantity);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultVO delete(String productId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.delete(userId, productId);
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public ResultVO select(String productId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.select(userId, productId);
    }

    @RequestMapping(value = "/unselect", method = RequestMethod.POST)
    public ResultVO unselect(String productId,
                             HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.unselect(userId, productId);
    }

    @RequestMapping(value = "/get_cart_product_count", method = RequestMethod.POST)
    public ResultVO getCartProductCount(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.getCartProductCount(userId);
    }

    @RequestMapping(value = "/select_all", method = RequestMethod.POST)
    public ResultVO selectAll(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.selectAll(userId);
    }

    @RequestMapping(value = "/unselect_all", method = RequestMethod.POST)
    public ResultVO unselectAll(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return cartService.unselectAll(userId);
    }

    private UserDTO checkLogin(HttpServletRequest request, HttpServletResponse response){
        //判断登录状态
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo == null){
            log.error("登录已失效");
            throw new UserException(CodeMsgVO.TOKEN_ERROR);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDTO);
        return userDTO;
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

    //该方法同时供order服务调用
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public CartDTO search(@RequestBody String userId){
        //String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return null;
        }else {
            return cartService.search(userId);
        }
    }

    //该方法同时供order服务调用
    @RequestMapping(value = "/delete_checked", method = RequestMethod.POST)
    public CartDTO deleteChecked(@RequestBody String userId){
        //String userId = userClient.getUserId();
        if(StringUtils.isEmpty(userId)){
            return null;
        }else {
            return cartService.deleteChecked(userId);
        }
    }

}
