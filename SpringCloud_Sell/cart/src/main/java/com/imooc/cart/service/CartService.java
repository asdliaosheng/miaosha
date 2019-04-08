package com.imooc.cart.service;

import com.imooc.cart.DTO.CartDTO;
import com.imooc.cart.VO.ResultVO;

/**
 * create by ASheng
 * 2019/3/25 10:33
 */
public interface CartService {

    ResultVO list(String userId);
    ResultVO add(String userId, String productId, Integer productQuantity);
    ResultVO update(String userId, String productId, Integer productQuantity);
    ResultVO delete(String userId, String productId);
    ResultVO select(String userId, String productId);
    ResultVO unselect(String userId, String productId);
    ResultVO getCartProductCount(String userId);
    ResultVO selectAll(String userId);
    ResultVO unselectAll(String userId);
    CartDTO search(String userId);
}
