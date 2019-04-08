package com.imooc.product.controller.backend;

import com.imooc.product.VO.ResultVO;
import com.imooc.product.client.UserClient;
import com.imooc.product.common.Const;
import com.imooc.product.common.UserDTO;
import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.service.CategoryService;
import com.imooc.product.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    private UserClient userClient;

    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public ResultVO addCategory(HttpSession session, Integer categoryId, Integer parentId, String categoryName) {
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【添加品类】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        } else {
            ProductCategory category = categoryService.addCategory(categoryId, parentId, categoryName);
            if (category == null) {//不可能
                log.error("【添加品类】操作失败");
                return ResultVOUtil.fail("添加品类失败", null);
            } else {
                return ResultVOUtil.success("添加品类成功", category);
            }
        }
    }

    @RequestMapping(value = "/update_category_name", method = RequestMethod.POST)
    public ResultVO updateCategory(HttpSession session, Integer categoryId, String categoryName) {
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【更新品类】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else{
            ProductCategory category = categoryService.updateCategoryName(categoryId, categoryName);
            if(category == null){//不可能失败,无用代码
                log.error("【更新品类】更新品类失败");
                return ResultVOUtil.fail("更新品类名字失败",null);
            }else{
                return ResultVOUtil.success("更新品类名字成功",category);
            }
        }

    }

    @RequestMapping(value = "/get_category", method = RequestMethod.POST)
    public ResultVO getCategory(HttpSession session, Integer categoryId){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【获取品类子类】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            List<ProductCategory> categoryList = categoryService.getCategory(categoryId);
            if(categoryList.isEmpty()){
                log.error("【获取品类子类】未找到该品类");
                return ResultVOUtil.fail("未找到该品类子类",null);
            }else{
                return ResultVOUtil.success("查询成功", categoryList);
            }
        }
    }

    @RequestMapping(value = "/get_deep_category", method = RequestMethod.POST)
    public ResultVO getDeepCategory(HttpSession session, Integer categoryId){
        if(!userClient.check()){//方法返回true为有管理员权限
            log.error("【获取品类及所有递归子类】管理员未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else {
            List<ProductCategory> deepCategoryList = categoryService.getDeepCategory(categoryId);
            if(deepCategoryList.isEmpty()){
                log.error("【获取品类及所有递归子类】未找到该品类");
                return ResultVOUtil.fail("未找到该品类",null);
            }else{
                return ResultVOUtil.success("查询成功",deepCategoryList);
            }
        }
    }

    /*
    //管理员权限判断
    //TODO 权限校验,要用到服务通信?
    public ResultVO judgeAuth (HttpSession session) {
        UserDTO userDTO = (UserDTO)session.getAttribute(Const.CURRENT_USER);
        if(userDTO == null){
            log.error("【登录状态】用户未登录");
            return ResultVOUtil.fail("管理员未登录",null);
        }else if(userDTO.getUserRole() != Const.Role.ROLE_ADMIN){
            log.error("【添加品类】权限不足");
            return ResultVOUtil.fail("权限不足",null);
        }else{
            return null;
        }
    }
    */

}