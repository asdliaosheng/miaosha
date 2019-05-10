package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.dataobject.MiaoshaOrder;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.dataobject.UserInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.ProductKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaOrderService;
import com.imooc.miaosha.service.MiaoshaProductService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.utils.AccessUtil;
import com.imooc.miaosha.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * create by ASheng
 * 2019/4/7 16:43
 */
@Slf4j
@RestController
@RequestMapping("/miaosha")
@Transactional
public class MiaoshaOrderController implements InitializingBean {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    private HashMap<String, Boolean> localOverMap = new HashMap<>();

    //系统初始化,加载商品列表进redis
    @Override
    public void afterPropertiesSet() throws Exception {
        List<MiaoshaProduct> productList = miaoshaProductService.getMiaoshaProductList();
        if(productList == null){
            return;
        }
        for(MiaoshaProduct product: productList){
            redisService.set(ProductKey.getMiaoshaProductStock, product.getMiaoshaProductId(), product.getMiaoshaProductStock());
            localOverMap.put(product.getMiaoshaProductId(), false);
        }
    }

    //重置
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResultVO<Boolean> reset(HttpServletResponse response){
        AccessUtil.allowAccess(response);
        List<MiaoshaProduct> productList = miaoshaProductService.getMiaoshaProductList();
        for(MiaoshaProduct product: productList){
            product.setMiaoshaProductStock(10);
            redisService.set(ProductKey.getMiaoshaProductStock, product.getMiaoshaProductId(), product.getMiaoshaProductStock());
            localOverMap.put(product.getMiaoshaProductId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidPid);
        redisService.delete(ProductKey.isProductOver);
        miaoshaOrderService.reset(productList);
        return ResultVO.success(true);
    }

    //下订单,减库存
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    public ResultVO miaosha(HttpServletRequest request, HttpServletResponse response, String miaoshaProductId){
        AccessUtil.allowAccess(response);
        //判断登录状态
        String token = UserUtil.checkSession(request);
        UserInfo user = miaoshaUserService.getByToken(response, token);//拿到登录用户对象__一次读redis
        //内存标记,可减少redis访问
        boolean isOver = localOverMap.get(miaoshaProductId);
        if(isOver){
            log.error("商品已售罄");
            return ResultVO.error(CodeMsgVO.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decrease(ProductKey.getMiaoshaProductStock, miaoshaProductId);
        if(stock < 0){
            localOverMap.put(miaoshaProductId, true);
            log.error("商品已售罄");
            //throw new GlobalException(CodeMsgVO.MIAO_SHA_OVER);
            return ResultVO.error(CodeMsgVO.MIAO_SHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), miaoshaProductId);
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            //throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
            return ResultVO.error(CodeMsgVO.REPEATE_MIAOSHA);
        }

        //RabbitMQ入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setProductId(miaoshaProductId);
        sender.sendMiaoshaMessage(mm);
        return ResultVO.success(0);//0表示排队中
        /*
        //查询商品,判断库存
        MiaoshaProduct miaoshaProduct = miaoshaProductService.getMiaoshaProduct(miaoshaProductId);//一次mysql,取MiaoshaProduct
        if(miaoshaProduct.getMiaoshaProductStatus() != Const.ON_SALE){
            log.error("[%s]商品已下架或已删除",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.PRODUCT_NOT_ON_SALE);
        }
        int stock = miaoshaProduct.getMiaoshaProductStock();
        if(stock <= 0){
            log.error("[%s]商品库存不足",miaoshaProduct.getMiaoshaProductName());
            throw new GlobalException(CodeMsgVO.MIAO_SHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), miaoshaProductId);//两次mysql
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
        }
        //减库存下订单
        return ResultVO.success(miaoshaOrderService.miaosha(user, miaoshaProduct));//至此:四次mysql
        */
    }

    /*
    * QPS <5
    * 500 * 10
    */
    @RequestMapping(value = "/do_miaosha_test",method = RequestMethod.POST)
    public ResultVO miaoshaTest(HttpServletRequest request, HttpServletResponse response, String miaoshaProductId, String token){
        AccessUtil.allowAccess(response);
        //判断登录状态
        //String token = UserUtil.checkSession(request);
        UserInfo user = miaoshaUserService.getByToken(response, token);//拿到登录用户对象__一次读redis
        //内存标记,可减少redis访问
        boolean isOver = localOverMap.get(miaoshaProductId);
        if(isOver){
            log.error("[内存标记]商品已售罄");
            return ResultVO.error(CodeMsgVO.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decrease(ProductKey.getMiaoshaProductStock, miaoshaProductId);
        if(stock < 0){
            localOverMap.put(miaoshaProductId, true);
            log.error("[查redis]商品已售罄");
            //throw new GlobalException(CodeMsgVO.MIAO_SHA_OVER);
            return ResultVO.error(CodeMsgVO.MIAO_SHA_OVER);
        }
        //判断是否秒杀到了
        MiaoshaOrder miaoshaOrder = miaoshaOrderService.findByUserIdAndProductId(user.getUserId(), miaoshaProductId);
        if(miaoshaOrder != null){
            log.error("秒杀订单已存在");
            //throw new GlobalException(CodeMsgVO.REPEATE_MIAOSHA);
            return ResultVO.error(CodeMsgVO.REPEATE_MIAOSHA);
        }

        //RabbitMQ入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setProductId(miaoshaProductId);
        sender.sendMiaoshaMessage(mm);
        return ResultVO.success(0);//0表示排队中
    }


}
