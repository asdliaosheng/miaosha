package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.dataobject.MiaoshaOrder;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.dataobject.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.ProductKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaOrderService;
import com.imooc.miaosha.service.MiaoshaProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by ASheng
 * 2019/4/9 16:59
 */
@Slf4j
@Service
public class MQReceive {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private RedisService redisService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        log.info("receice message:" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        String productId = mm.getProductId();
        MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(productId);
        int stock = miaoshaProduct.getMiaoshaProductStock();//TODO 又是从mysql中取的
        //int stock = redisService.get(ProductKey.getMiaoshaProductStock, productId, Integer.class);
        if(stock <= 0){
            return;
        }
        //MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(productId);
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), productId);
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
        }
        //减库存下订单
        miaoshaOrderService.miaosha(user, miaoshaProduct);
    }
}
