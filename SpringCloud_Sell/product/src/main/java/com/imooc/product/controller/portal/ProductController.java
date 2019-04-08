package com.imooc.product.controller.portal;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.common.OrderDetail;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.service.ProductService;
import com.imooc.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/22 17:49
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(@RequestParam(value = "keyword",required = false)String keyword,
                         @RequestParam(value = "categoryId",required = false)Integer categoryId){
        if(StringUtils.isEmpty(keyword) && categoryId == null){
            log.error("【查询商品】操作失败");
            return ResultVOUtil.fail("无参数传递",null);
        } else if(!StringUtils.isEmpty(keyword) && categoryId != null){
            List<ProductDTO> productDTOList = productService.getProductList(categoryId, keyword);
            return ResultVOUtil.success("类目与关键字查询商品列表成功",productDTOList);
        } else if(categoryId != null && StringUtils.isEmpty(keyword)){
            List<ProductDTO> productDTOList = productService.getProductList(categoryId);
            return ResultVOUtil.success("类目查询商品列表成功",productDTOList);
        } else if(!StringUtils.isEmpty(keyword) && categoryId == null){
            List<ProductDTO> productDTOList = productService.searchProductName(keyword);
            return ResultVOUtil.success("关键字查询商品列表成功",productDTOList);
        } else{
            return ResultVOUtil.fail("查询条件混乱",null);
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String productId){
        ProductInfo productInfo = productService.getProductInfo(productId);
        if(StringUtils.isEmpty(productInfo)){
            log.error("【按Id查询商品】操作失败");
            return ResultVOUtil.fail("无该商品或操作失败", null);
        }else if(productInfo.getProductStatus() != 1){
            log.error("【按Id查询商品】操作失败");
            return ResultVOUtil.fail("该商品已下架或已删除", null);
        }else{
            return ResultVOUtil.success("按Id查询商品成功",productInfo);
        }
    }

    //给购物车服务用的
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ProductInfo search(@RequestBody String productId){
        ProductInfo productInfo = productService.getProductInfo(productId);
        return productInfo;
    }

    //给订单服务,扣存库
    @RequestMapping(value = "/decrease_stock", method = RequestMethod.POST)
    public String decreaseStock(@RequestBody List<OrderDetail> orderDetailList){
        return productService.decreaseStock(orderDetailList);
    }
}
