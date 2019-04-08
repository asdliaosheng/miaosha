package com.imooc.product.controller.backend;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.client.UserClient;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.service.ProductService;
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
 * 2019/3/22 13:08
 */
@Slf4j
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO getProductList(){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【查询商品列表】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            List<ProductDTO> productDTOList = productService.getProductList();
            if (productDTOList.isEmpty()) {
                log.error("【查询商品列表】操作失败");
                return ResultVOUtil.fail("查询商品列表失败", null);
            } else {
                return ResultVOUtil.success("查询商品列表成功", productDTOList);
            }
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResultVO searchProductName(String productName) {
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【按名称查询商品】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            List<ProductDTO> productDTOList = productService.searchProductName(productName);
            if (productDTOList.isEmpty()) {
                log.error("【按名称查询商品】操作失败");
                return ResultVOUtil.fail("按名称查询商品失败", null);
            } else {
                return ResultVOUtil.success("按名称查询商品成功", productDTOList);
            }
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO getProductInfo(String productId){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【按Id查询商品】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            ProductInfo productInfo = productService.getProductInfo(productId);
            if (StringUtils.isEmpty(productInfo)) {
                log.error("【按Id查询商品】操作失败");
                return ResultVOUtil.fail("无该商品或操作失败", null);
            } else {
                return ResultVOUtil.success("按Id查询商品成功", productInfo);
            }
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultVO createOrUpdateProduct(ProductInfo productInfo){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【创建或更新商品】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            if(null != productService.getProductInfo(productInfo.getProductId())){
                productInfo.setCreateTime(productService.getProductInfo(productInfo.getProductId()).getCreateTime());
            }else{
                productInfo.setCreateTime(new Date());
            }
            productInfo.setUpdateTime(new Date());
            productInfo = productService.createOrUpdateProduct(productInfo);
            if (StringUtils.isEmpty(productInfo)) {//无用
                log.error("【创建或更新商品】操作失败");
                return ResultVOUtil.fail("创建或更新商品失败", null);
            } else {
                return ResultVOUtil.success("创建或更新成功", productInfo);
            }
        }
    }

}
