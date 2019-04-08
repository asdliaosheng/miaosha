package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.service.MiaoshaProductService;
import com.imooc.miaosha.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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

    //删除
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultVO deleteMiaoshaProduct(String miaoshaProductId){
        miaoshaProductService.deleteMiaoshaProduct(miaoshaProductId);
        return ResultVOUtil.success("删除秒杀商品成功", null);
    }
}
