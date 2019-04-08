package com.imooc.order.service.impl;

import com.imooc.order.DTO.CartDTO;
import com.imooc.order.DTO.CartDetail;
import com.imooc.order.DTO.OrderDTO;
import com.imooc.order.VO.ResultVO;
import com.imooc.order.client.CartClient;
import com.imooc.order.client.ProductClient;
import com.imooc.order.dataobject.OrderDetail;
import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.repository.OrderDetailRepository;
import com.imooc.order.repository.OrderMasterRepository;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.KeyUtil;
import com.imooc.order.utils.ResultVOUtil;
import com.sun.org.apache.xml.internal.security.keys.KeyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/26 10:54
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private ProductClient productClient;

    //创建订单
    @Override
    public ResultVO create() {
        //取购物车cartDTO对象,一个用户只有一个购物车主表,复用
        CartDTO cartDTO = cartClient.search();
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(KeyUtil.genUniqueKey());
        orderMaster.setUserId(cartDTO.getUserId());
        orderMaster.setShippingId(null);//TODO
        orderMaster.setOrderTotalPrice(cartDTO.getCartTotalPrice());
        orderMaster.setPaymentType(1);//写死
        orderMaster.setOrderPostage(new BigDecimal("0"));
        orderMaster.setOrderStatus(10);//默认为未付款
        orderMaster.setCreateTime(new Date());
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);//订单主表OrderMaster入库
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartDetail cartDetail : cartDTO.getCartDetailList()){
            if(cartDetail.getChecked() == 1){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDetailId(KeyUtil.genUniqueKey());
                orderDetail.setOrderId(orderMaster.getOrderId());
                orderDetail.setProductId(cartDetail.getProductId());
                orderDetail.setProductName(cartDetail.getProductName());
                orderDetail.setProductCurrentPrice(cartDetail.getProductCurrentPrice());
                orderDetail.setProductQuantity(cartDetail.getProductQuantity());
                orderDetail.setProductImage(cartDetail.getProductImage());
                orderDetail.setCreateTime(new Date());
                orderDetail.setUpdateTime(new Date());
                orderDetailRepository.save(orderDetail);//订单详情表OrderDetail入库
                orderDetailList.add(orderDetail);
            }

        }

        //扣库存
        String lowProductId = productClient.decreaseStock(orderDetailList);
        if(!StringUtils.isEmpty(lowProductId)){
            return ResultVOUtil.fail("该商品库存不足",lowProductId);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return ResultVOUtil.success(orderDTO);
    }

    @Override
    public List<OrderDTO> list(String userId) {
        List<OrderMaster> orderMasterList = orderMasterRepository.findByUserId(userId);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(OrderMaster orderMaster: orderMasterList){
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(orderMaster, orderDTO);
            orderDTO.setOrderDetailList(orderDetailRepository.findByOrderId(orderMaster.getOrderId()));
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    @Override
    public OrderDTO detail(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findByOrderId(orderId);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailRepository.findByOrderId(orderId));
        return orderDTO;
    }

    @Override
    public OrderDTO cancel(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findByOrderId(orderId);
        orderMaster.setOrderStatus(0);
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailRepository.findByOrderId(orderId));
        return orderDTO;
    }

    @Override
    public OrderDTO updateShippingId(String orderId, String shippingId) {
        OrderMaster orderMaster = orderMasterRepository.findByOrderId(orderId);
        orderMaster.setShippingId(shippingId);
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailRepository.findByOrderId(orderId));
        return orderDTO;
    }

    //管理员使用
    @Override
    public OrderDTO update(String orderId, Integer orderStatus) {
        OrderMaster orderMaster = orderMasterRepository.findByOrderId(orderId);
        orderMaster.setOrderStatus(orderStatus);
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailRepository.findByOrderId(orderId));
        return orderDTO;
    }
}
