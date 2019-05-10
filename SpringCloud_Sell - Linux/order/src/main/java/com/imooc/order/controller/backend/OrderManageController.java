package com.imooc.order.controller.backend;

import com.imooc.order.VO.ResultVO;
import com.imooc.order.client.UserClient;
import com.imooc.order.common.UserInfo;
import com.imooc.order.common.UserUtil;
import com.imooc.order.redis.MiaoshaUserService;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.AccessUtil;
import com.imooc.order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.order.VO.CodeMsgVO.NO_AUTHORITY;

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

//    @Autowired
//    private UserClient userClient;

    @Autowired
    private MiaoshaUserService miaoshaUserService;


    //列出某个用户所有订单
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(String userId,
                         HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else {
            return ResultVOUtil.success(orderService.list(userId));
        }
    }

    //查询某个订单
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String orderId,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else {
            return ResultVOUtil.success(orderService.detail(orderId));
        }
    }

    //修改某个订单状态
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(String orderId, Integer orderStatus,
                           HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else {
            return ResultVOUtil.success(orderService.update(orderId, orderStatus));
        }
    }

    private boolean checkAdminLogin(HttpServletRequest request, HttpServletResponse response){
        //判断管理员是否登录
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo.getUserRole() != 0){
            return false;
        }else{
            return true;
        }
    }
}
