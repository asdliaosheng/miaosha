package com.imooc.product.controller.backend;

import com.imooc.product.VO.CodeMsgVO;
import com.imooc.product.VO.ResultVO;
import com.imooc.product.client.UserClient;
import com.imooc.product.common.Const;
import com.imooc.product.common.UserDTO;
import com.imooc.product.common.UserInfo;
import com.imooc.product.common.UserUtil;
import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.redis.MiaoshaUserService;
import com.imooc.product.service.CategoryService;
import com.imooc.product.utils.AccessUtil;
import com.imooc.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.imooc.product.VO.CodeMsgVO.NO_AUTHORITY;

/**
 * create by ASheng
 * 2019/3/20 23:14
 */
@Slf4j
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MiaoshaUserService miaoshaUserService;

//    @Autowired
//    private UserClient userClient;

    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public ResultVO addCategory(Integer categoryId, Integer parentId, String categoryName,
                                HttpServletRequest request, HttpServletResponse response) {
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        } else {
            ProductCategory category = categoryService.addCategory(categoryId, parentId, categoryName);
            if (category == null) {//不可能
                log.error("【添加品类】操作失败");
                return ResultVO.error(CodeMsgVO.ADD_CATEGORY_ERROR);
            } else {
                return ResultVO.success(category);
            }
        }
    }

    @RequestMapping(value = "/update_category_name", method = RequestMethod.POST)
    public ResultVO updateCategory(Integer categoryId, String categoryName,
                                   HttpServletRequest request, HttpServletResponse response) {
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else{
            ProductCategory category = categoryService.updateCategoryName(categoryId, categoryName);
            if(category == null){//不可能失败,无用代码
                log.error("【更新品类】更新品类失败");
                return ResultVO.error(CodeMsgVO.UPDATE_CATEGORY_NAME_ERROR);
            }else{
                return ResultVO.success(category);
            }
        }

    }

    @RequestMapping(value = "/get_category", method = RequestMethod.POST)
    public ResultVO getCategory(Integer categoryId,
                                HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else {
            List<ProductCategory> categoryList = categoryService.getCategory(categoryId);
            if(categoryList.isEmpty()){
                log.error("【获取品类子类】未找到该品类");
                return ResultVO.error(CodeMsgVO.SUBCATEGORY_NOT_EXIST);
            }else{
                return ResultVO.success(categoryList);
            }
        }
    }

    @RequestMapping(value = "/get_deep_category", method = RequestMethod.POST)
    public ResultVO getDeepCategory(Integer categoryId,
                                    HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【订单列表】权限不足");
            return ResultVO.error(NO_AUTHORITY);
        }else {
            List<ProductCategory> deepCategoryList = categoryService.getDeepCategory(categoryId);
            if(deepCategoryList.isEmpty()){
                log.error("【获取品类及所有递归子类】未找到该品类");
                return ResultVO.error(CodeMsgVO.CATEGORY_NOT_EXIST);
            }else{
                return ResultVO.success(deepCategoryList);
            }
        }
    }

    private boolean checkAdminLogin(HttpServletRequest request, HttpServletResponse response){
        //判断管理员是否登录
        String token = UserUtil.checkSession(request);
        UserInfo userInfo = miaoshaUserService.getByToken(response, token);//拿到登录用户对象
        if(userInfo.getUserRole() != 0){
            return false;
        }else{
            return true;
        }
    }

}