package com.imooc.product.controller.backend;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.VO.CodeMsgVO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.common.Const;
import com.imooc.product.common.UserInfo;
import com.imooc.product.common.UserUtil;
import com.imooc.product.dataobject.ProductForm;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.redis.MiaoshaUserService;
import com.imooc.product.service.ProductService;
import com.imooc.product.utils.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * create by ASheng
 * 2019/3/22 13:08
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

//    @Autowired
//    private UserClient userClient;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO getProductList(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【查询商品列表】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }else {
            List<ProductDTO> productDTOList = productService.getProductList();
            if (productDTOList.isEmpty()) {
                log.error("【查询商品列表】操作失败");
                return ResultVO.error(CodeMsgVO.PRODUCT_LIST_IS_EMPTY);
            } else {
                return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
            }
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResultVO searchProductName(String productName,
                                      HttpServletRequest request, HttpServletResponse response) {
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【按名称查询商品】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }else {
            List<ProductDTO> productDTOList = productService.searchProductName(productName);
            if (productDTOList.isEmpty()) {
                log.error("【按名称查询商品】操作失败");
                return ResultVO.error(CodeMsgVO.PRODUCT_LIST_IS_EMPTY);
            } else {
                return ResultVO.success(CodeMsgVO.SUCCESS, productDTOList);
            }
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultVO getProductInfo(String productId,
                                   HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【按Id查询商品】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }else {
            ProductInfo productInfo = productService.getProductInfo(productId);
            if (productInfo == null) {
                log.error("【按Id查询商品】操作失败");
                return ResultVO.error(CodeMsgVO.PRODUCT_NOT_EXIST);
            } else {
                return ResultVO.success(CodeMsgVO.SUCCESS, productInfo);
            }
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultVO createProduct(ProductForm productForm,
                                  HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【创建或更新商品】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }else {
            if(null != productService.getProductInfoByProductName(productForm.getProductName())){
                return ResultVO.error(CodeMsgVO.PRODUCT_ALREADY_EXIST);
            }
            ProductInfo productInfo = productService.createProduct(productForm);
            return ResultVO.success(CodeMsgVO.SUCCESS, productInfo);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO updateProduct(ProductInfo productInfo,
                                          HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【创建或更新商品】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }else {
            if(null != productService.getProductInfo(productInfo.getProductId())){
                productInfo.setCreateTime(productService.getProductInfo(productInfo.getProductId()).getCreateTime());
            }else{
                productInfo.setCreateTime(new Date());
            }
            productInfo.setUpdateTime(new Date());
            productInfo = productService.updateProduct(productInfo);
            if (productInfo == null) {//无用
                log.error("【创建或更新商品】操作失败");
                return ResultVO.error(CodeMsgVO.CU_PRODUCT_ERROR);
            } else {
                return ResultVO.success(CodeMsgVO.SUCCESS, productInfo);
            }
        }
    }

    private boolean checkAdminLogin(HttpServletRequest request, HttpServletResponse response){
        //判断管理员是否登录
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo.getUserRole() != Const.Role.ROLE_ADMIN){
            return false;
        }else{
            return true;
        }
    }
}
