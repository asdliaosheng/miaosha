package com.imooc.cart.utils;

import com.imooc.cart.DTO.CartDTO;
import com.imooc.cart.dataobject.CartDetail;
import com.imooc.cart.dataobject.CartMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/25 11:10
 */
public class CartVOUtil {

    public static CartDTO get(CartMaster cartMaster, List<CartDetail> cartDetailList) {
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cartMaster, cartDTO);
        cartDTO.setCartDetailList(cartDetailList);

        return cartDTO;
    }
}
