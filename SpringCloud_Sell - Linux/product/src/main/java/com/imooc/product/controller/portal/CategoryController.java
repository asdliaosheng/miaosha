package com.imooc.product.controller.portal;

import com.imooc.product.VO.CodeMsgVO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.common.UserInfo;
import com.imooc.product.common.UserUtil;
import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.redis.MiaoshaUserService;
import com.imooc.product.service.CategoryService;
import com.imooc.product.utils.AccessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.imooc.product.VO.CodeMsgVO.NO_AUTHORITY;

/**
 * create by ASheng
 * 2019/3/20 23:14
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

//    @Autowired
//    private UserClient userClient;

    @RequestMapping(value = "/get_category", method = RequestMethod.POST)
    public ResultVO getCategory(Integer categoryId,
                                HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        List<ProductCategory> categoryList = categoryService.getCategory(categoryId);
        if(categoryList.isEmpty()){
            log.error("【获取品类子类】未找到该品类");
            return ResultVO.error(CodeMsgVO.SUBCATEGORY_NOT_EXIST);
        }else{
            return ResultVO.success(categoryList);
        }
    }

    @RequestMapping(value = "/get_deep_category", method = RequestMethod.POST)
    public ResultVO getDeepCategory(Integer categoryId,
                                    HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        List<ProductCategory> deepCategoryList = categoryService.getDeepCategory(categoryId);
        if(deepCategoryList.isEmpty()){
            log.error("【获取品类及所有递归子类】未找到该品类");
            return ResultVO.error(CodeMsgVO.CATEGORY_NOT_EXIST);
        }else{
            return ResultVO.success(deepCategoryList);
        }
    }


}