package com.imooc.miaosha.service;

import com.imooc.miaosha.dataobject.MiaoshaOrder;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.dataobject.MiaoshaUser;
import com.imooc.miaosha.repository.MiaoshaOrderRepository;
import com.imooc.miaosha.utils.Const;
import com.imooc.miaosha.utils.KeyUtil;
import com.imooc.miaosha.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * create by ASheng
 * 2019/4/3 20:32
 */
@Service
public class MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderRepository miaoshaOrderRepository;

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    public MiaoshaOrder findByUserIdAndProductId(String userId, String productId) {
        return miaoshaOrderRepository.findByUserIdAndMiaoshaProductId(userId, productId);
    }

    /**
     * 减库存,下订单
     * 显然,下订单之间若是注释一人只能秒杀一次相同商品规则,那么下订单之间几乎没有冲突(存在id相同的冲突,但是较少)
     * 扣库存之后更新,但是并发读取的商品信息还是未更新的商品库存,那么就存在少减了库存值
     */
    @Transactional
    public MiaoshaOrder miaosha(MiaoshaUser user, MiaoshaProduct product){
        miaoshaProductService.reduceStock(product);//一次mysql,更新商品库存信息
//        int result = product.getMiaoshaProductStock() - 1;
//        product.setMiaoshaProductStock(result);
//        miaoshaProductService.save(product);//存减库存后的MiaoshaProduct
        MiaoshaOrder order = new MiaoshaOrder();
        order.setMiaoshaOrderId(UUIDUtil.uuid());//TODO
        order.setUserId(user.getUserId());
        order.setShippingId("001");//TODO
        order.setMiaoshaProductId(product.getMiaoshaProductId());
        order.setMiaoshaProductName(product.getMiaoshaProductName());
        order.setMiaoshaProductPrice(product.getMiaoshaProductPrice());
        order.setMiaoshaProductQuantity(1);
        order.setMiaoshaProductMainImage(product.getMiaoshaProductMainImage());
        order.setPaymentType(1);
        order.setMiaoshaOrderPostage(new BigDecimal("0"));
        order.setMiaoshaOrderTotalPrice(new BigDecimal(
                product.getMiaoshaProductPrice().multiply(new BigDecimal("1")).add(new BigDecimal("0")) + ""));
        order.setMiaoshaOrderStatus(Const.OrderStatus.UNPAID);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        return miaoshaOrderRepository.save(order);//两次mysql
    }
}
