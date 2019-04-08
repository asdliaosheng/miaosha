package com.imooc.order.controller.backend;

import com.imooc.order.VO.ResultVO;
import com.imooc.order.client.UserClient;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by ASheng
 * 2019/3/26 10:22
 */
@Slf4j
@RestController
@RequestMapping("/manage/order")
@Transactional
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserClient userClient;

    //列出某个用户所有订单
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(String userId){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【订单列表】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            return ResultVOUtil.success(orderService.list(userId));
        }
    }

    //查询某个订单
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String orderId){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【查询订单】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            return ResultVOUtil.success(orderService.detail(orderId));
        }
    }

    //修改某个订单状态
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String orderId, Integer orderStatus){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【修改订单状态】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            return ResultVOUtil.success(orderService.update(orderId, orderStatus));
        }
    }
}
