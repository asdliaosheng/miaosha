package com.imooc.order.controller.portal;

import com.imooc.order.DTO.OrderDTO;
import com.imooc.order.VO.ResultVO;
import com.imooc.order.client.UserClient;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by ASheng
 * 2019/3/26 10:22
 */
@RestController
@RequestMapping("/order")
@Transactional
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserClient userClient;

    /**
     * QPS: 126
     * 设备: 本机
     */
    //创建订单
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultVO create(){
        return orderService.create();
    }

    //列出该用户所有订单
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(){
        String userId = userClient.getUserId();
        return ResultVOUtil.success(orderService.list(userId));
    }

    //查询某个订单
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String orderId){
        return ResultVOUtil.success(orderService.detail(orderId));
    }

    //取消某个订单
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResultVO cancel(String orderId){
        return ResultVOUtil.success(orderService.cancel(orderId));
    }

    //修改订单收货地址
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String orderId, String shippingId){
        return ResultVOUtil.success(orderService.updateShippingId(orderId, shippingId));
    }
}
