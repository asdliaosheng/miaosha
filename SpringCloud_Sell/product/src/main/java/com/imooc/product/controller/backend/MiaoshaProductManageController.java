package com.imooc.product.controller.backend;

import com.imooc.product.VO.ResultVO;
import com.imooc.product.dataobject.MiaoshaProduct;
import com.imooc.product.service.MiaoshaProductService;
import com.imooc.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/4/3 12:51
 */
@Slf4j
@RestController
@RequestMapping("/manage/miaosha_product")
public class MiaoshaProductManageController {

    @Autowired
    private MiaoshaProductService miaoshaProductService;

    //添加秒杀商品
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultVO createOrUpdateMiaoshaProduct(MiaoshaProduct miaoshaProduct){
        if(null != miaoshaProductService.getMiaoshaProduct(miaoshaProduct.getMiaoshaProductId())){
            miaoshaProduct.setCreateTime(miaoshaProductService.getMiaoshaProduct(miaoshaProduct.getMiaoshaProductId()).getCreateTime());
        }else{
            miaoshaProduct.setCreateTime(new Date());
        }
        miaoshaProduct.setUpdateTime(new Date());
        miaoshaProduct = miaoshaProductService.createOrUpdateProduct(miaoshaProduct);
        if (StringUtils.isEmpty(miaoshaProduct)) {//无用
            log.error("【创建或更新秒杀商品】操作失败");
            return ResultVOUtil.fail("创建或更新秒杀商品失败", null);
        } else {
            return ResultVOUtil.success("创建或更新成功", miaoshaProduct);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO getProductList(){
        List<MiaoshaProduct> miaoshaProductList = miaoshaProductService.getMiaoshaProductList();
        if (miaoshaProductList.isEmpty()) {
            log.error("【查询秒杀商品列表】操作失败");
            return ResultVOUtil.fail("查询秒杀商品列表失败", null);
        } else {
            return ResultVOUtil.success("查询商品列表成功", miaoshaProductList);
        }
    }
}
