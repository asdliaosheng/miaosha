package com.imooc.cart.service.impl;

import com.imooc.cart.DTO.CartDTO;
import com.imooc.cart.VO.ResultVO;
import com.imooc.cart.client.ProductClient;
import com.imooc.cart.dataobject.CartDetail;
import com.imooc.cart.dataobject.CartMaster;
import com.imooc.cart.dataobject.ProductInfo;
import com.imooc.cart.repository.CartDetailRepository;
import com.imooc.cart.repository.CartMasterRepository;
import com.imooc.cart.service.CartService;
import com.imooc.cart.utils.CartVOUtil;
import com.imooc.cart.utils.KeyUtil;
import com.imooc.cart.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/25 10:58
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMasterRepository cartMasterRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductClient productClient;

    //公共输出方法
    @Override
    public ResultVO list(String userId) {
        CartMaster cartMaster =  checkCart(userId);
        List<CartDetail> cartDetailList = cartDetailRepository.findAllByCartId(cartMaster.getCartId());
        BigDecimal amount = new BigDecimal("0");
        for(CartDetail cartDetail : cartDetailList){
            if(cartDetail.getChecked() == 1) {//1为选定
                amount = cartDetail.getProductCurrentPrice()
                        .multiply(new BigDecimal(cartDetail.getProductQuantity()))
                        .add(amount);
            }
        }
        cartMaster.setCartTotalPrice(amount);
        cartMaster.setUpdateTime(new Date());
        cartMasterRepository.save(cartMaster);
        return ResultVOUtil.success(CartVOUtil.get(cartMaster, cartDetailList));
    }

    @Override
    public ResultVO add(String userId, String productId, Integer productQuantity) {
        CartMaster cartMaster =  checkCart(userId);
        //已存在
        CartDetail exitsCartDetail = cartDetailRepository.findByCartIdAndProductId(cartMaster.getCartId(), productId);
        if(!StringUtils.isEmpty(exitsCartDetail)){
            return update(userId, productId, productQuantity + exitsCartDetail.getProductQuantity());
        }
        //不存在
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCartDetailId(KeyUtil.genUniqueKey());
        cartDetail.setCartId(cartMaster.getCartId());
        cartDetail.setChecked(1);
        cartDetail.setProductId(productId);
        ProductInfo productInfo = productClient.search(productId);
        cartDetail.setProductName(productInfo.getProductName());
        cartDetail.setProductCurrentPrice(productInfo.getProductPrice());
        cartDetail.setProductImage(productInfo.getProductMainImage());
        cartDetail.setProductQuantity(productQuantity);
        cartDetail.setCreateTime(new Date());
        cartDetail.setUpdateTime(new Date());
        cartDetailRepository.save(cartDetail);
        return list(userId);
    }

    @Override
    public ResultVO update(String userId, String productId, Integer productQuantity) {
        CartMaster cartMaster =  checkCart(userId);
        CartDetail cartDetail = cartDetailRepository.findByCartIdAndProductId(cartMaster.getCartId(), productId);
        cartDetail.setProductQuantity(productQuantity);
        cartDetail.setUpdateTime(new Date());
        cartDetailRepository.save(cartDetail);
        return list(userId);
    }

    @Override
    public ResultVO delete(String userId, String productId) {
        CartMaster cartMaster =  checkCart(userId);
        CartDetail cartDetail = cartDetailRepository.findByCartIdAndProductId(cartMaster.getCartId(), productId);
        cartDetailRepository.delete(cartDetail);
        return list(userId);
    }

    @Override
    public ResultVO select(String userId, String productId) {
        CartMaster cartMaster =  checkCart(userId);
        CartDetail cartDetail = cartDetailRepository.findByCartIdAndProductId(cartMaster.getCartId(), productId);
        cartDetail.setChecked(1);
        cartDetail.setUpdateTime(new Date());
        cartDetailRepository.save(cartDetail);
        return list(userId);
    }

    @Override
    public ResultVO unselect(String userId, String productId) {
        CartMaster cartMaster = checkCart(userId);
        CartDetail cartDetail = cartDetailRepository.findByCartIdAndProductId(cartMaster.getCartId(), productId);
        cartDetail.setChecked(0);
        cartDetail.setUpdateTime(new Date());
        cartDetailRepository.save(cartDetail);
        return list(userId);
    }

    @Override
    public ResultVO getCartProductCount(String userId) {
        Integer count = 0;
        CartMaster cartMaster = checkCart(userId);
        for(CartDetail cartDetail : cartDetailRepository.findAllByCartId(cartMaster.getCartId())){
            if(cartDetail.getChecked() == 1) {
                count += cartDetail.getProductQuantity();
            }
        }
        return ResultVOUtil.success(count);
    }

    @Override
    public ResultVO selectAll(String userId) {
        CartMaster cartMaster = checkCart(userId);
        for(CartDetail cartDetail : cartDetailRepository.findAllByCartId(cartMaster.getCartId())){
            cartDetail.setChecked(1);
            cartDetail.setUpdateTime(new Date());
            cartDetailRepository.save(cartDetail);
        }
        return list(userId);
    }

    @Override
    public ResultVO unselectAll(String userId) {
        CartMaster cartMaster = checkCart(userId);
        for(CartDetail cartDetail : cartDetailRepository.findAllByCartId(cartMaster.getCartId())){
            cartDetail.setChecked(0);
            cartDetail.setUpdateTime(new Date());
            cartDetailRepository.save(cartDetail);
        }
        return list(userId);
    }

    //判断存不存在用户对应的购物车
    private CartMaster checkCart(String userId){
        if(cartMasterRepository.findByUserId(userId) == null){
            CartMaster cartMaster = new CartMaster();
            cartMaster.setCartId(KeyUtil.genUniqueKey());
            cartMaster.setUserId(userId);
            cartMaster.setCartTotalPrice(new BigDecimal("0"));
            cartMaster.setCreateTime(new Date());
            cartMaster.setUpdateTime(new Date());
            return cartMasterRepository.save(cartMaster);
        }else{
            return cartMasterRepository.findByUserId(userId);
        }
    }

    @Override
    public CartDTO search(String userId) {
        CartMaster cartMaster =  checkCart(userId);
        List<CartDetail> cartDetailList = cartDetailRepository.findAllByCartId(cartMaster.getCartId());
        BigDecimal amount = new BigDecimal("0");
        for(CartDetail cartDetail : cartDetailList){
            if(cartDetail.getChecked() == 1) {//1为选定
                amount = cartDetail.getProductCurrentPrice()
                        .multiply(new BigDecimal(cartDetail.getProductQuantity()))
                        .add(amount);
            }
        }
        cartMaster.setCartTotalPrice(amount);
        cartMaster.setUpdateTime(new Date());
        cartMasterRepository.save(cartMaster);
        return CartVOUtil.get(cartMaster, cartDetailList);
    }
}
