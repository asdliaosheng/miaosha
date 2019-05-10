package com.imooc.product.controller.portal;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.VO.CodeMsgVO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.client.UserClient;
import com.imooc.product.common.OrderDetail;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.service.ProductService;
import com.imooc.product.utils.AccessUtil;
import com.imooc.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/keyword_search", method = RequestMethod.POST)
    public ResultVO list(@RequestParam(value = "keyword",required = false)String keyword,
                         HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(StringUtils.isEmpty(keyword)){
            log.error("【查询商品】无参数传递，查询失败");
            return ResultVO.error(CodeMsgVO.NO_PARAMETER);
        } else{
            List<ProductDTO> productDTOList = productService.getProductList(keyword);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        }
    }

    @RequestMapping(value = "/keyword_search_test", method = RequestMethod.GET)
    public ResultVO listTest(@RequestParam(value = "keyword",required = false)String keyword,
                         HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(StringUtils.isEmpty(keyword)){
            log.error("【查询商品】无参数传递，查询失败");
            return ResultVO.error(CodeMsgVO.NO_PARAMETER);
        } else{
            List<ProductDTO> productDTOList = productService.getProductList(keyword);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        }
    }

    @RequestMapping(value = "/category_id_search", method = RequestMethod.POST)
    public ResultVO list(@RequestParam(value = "categoryId",required = false)Integer categoryId,
                         HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(categoryId == null){
            log.error("【查询商品】无参数传递，查询失败");
            return ResultVO.error(CodeMsgVO.NO_PARAMETER);
        } else{
            List<ProductDTO> productDTOList = productService.getProductList(categoryId);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(@RequestParam(value = "keyword",required = false)String keyword,
                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                         HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(StringUtils.isEmpty(keyword) && categoryId == null){
            log.error("【查询商品】无参数传递，查询失败");
            return ResultVO.error(CodeMsgVO.NO_PARAMETER);
        } else if(!StringUtils.isEmpty(keyword) && categoryId != null){
            List<ProductDTO> productDTOList = productService.getProductList(categoryId, keyword);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        } else if(categoryId != null && StringUtils.isEmpty(keyword)){
            List<ProductDTO> productDTOList = productService.getProductList(categoryId);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        } else if(!StringUtils.isEmpty(keyword) && categoryId == null){
            List<ProductDTO> productDTOList = productService.searchProductName(keyword);
            return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
        } else{
            return null;
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO detail(String productId,
                           HttpServletResponse response){
        AccessUtil.allowAccess(response);
        ProductInfo productInfo = productService.getProductInfo(productId);
        if(productInfo == null){
            log.error("【按Id查询商品】操作失败");
            return ResultVO.error(CodeMsgVO.PRODUCT_NOT_EXIST);
        }else if(productInfo.getProductStatus() != 1){
            log.error("【按Id查询商品】操作失败");
            return ResultVO.error(CodeMsgVO.PRODUCT_STATUS_ERROR);
        }else{
            return ResultVO.success(CodeMsgVO.SUCCESS,productInfo);
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
