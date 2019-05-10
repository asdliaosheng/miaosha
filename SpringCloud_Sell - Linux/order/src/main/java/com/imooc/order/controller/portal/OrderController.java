package com.imooc.order.controller.portal;

import com.imooc.order.VO.CodeMsgVO;
import com.imooc.order.VO.ResultVO;
import com.imooc.order.client.UserClient;
import com.imooc.order.common.UserDTO;
import com.imooc.order.common.UserException;
import com.imooc.order.common.UserInfo;
import com.imooc.order.common.UserUtil;
import com.imooc.order.redis.MiaoshaUserService;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.AccessUtil;
import com.imooc.order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by ASheng
 * 2019/3/26 10:22
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Transactional
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    /**
     * QPS: 126
     * 设备: 本机
     */
    //创建订单
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultVO create(String shippingId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return orderService.create(userId, shippingId);
    }

    //列出该用户所有订单
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        UserDTO userDTO = checkLogin(request, response);
        String userId = userDTO.getUserId();
        return ResultVOUtil.success(orderService.list(userId));
    }

    //查询某个订单
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String orderId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        checkLogin(request, response);
        return ResultVOUtil.success(orderService.detail(orderId));
    }

    //取消某个订单
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResultVO cancel(String orderId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        checkLogin(request, response);
        return ResultVOUtil.success(orderService.cancel(orderId));
    }

    //修改订单收货地址
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String orderId, String shippingId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        checkLogin(request, response);
        return ResultVOUtil.success(orderService.updateShippingId(orderId, shippingId));
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
}
