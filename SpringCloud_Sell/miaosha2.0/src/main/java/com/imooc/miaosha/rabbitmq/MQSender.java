package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by ASheng
 * 2019/4/9 16:53
 */
@Slf4j
@Service
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm){
        String msg = RedisService.beanToString(mm);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
