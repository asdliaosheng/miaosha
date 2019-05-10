package com.imooc.miaosha.controller;

import com.imooc.miaosha.VO.CodeMsgVO;
import com.imooc.miaosha.VO.ResultVO;
import com.imooc.miaosha.dataobject.MiaoshaProduct;
import com.imooc.miaosha.dataobject.UserInfo;
import com.imooc.miaosha.service.MiaoshaProductService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.utils.AccessUtil;
import com.imooc.miaosha.utils.ResultVOUtil;
import com.imooc.miaosha.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    //添加秒杀商品
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultVO createOrUpdateMiaoshaProduct(MiaoshaProduct miaoshaProduct,
                                                 HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【查询商品列表】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }
        if(null != miaoshaProductService.getMiaoshaProduct(miaoshaProduct.getMiaoshaProductId())){
            miaoshaProduct.setCreateTime(miaoshaProductService.getMiaoshaProduct(miaoshaProduct.getMiaoshaProductId()).getCreateTime());
        }else{
            miaoshaProduct.setCreateTime(new Date());
        }
        miaoshaProduct.setUpdateTime(new Date());
        miaoshaProduct = miaoshaProductService.createOrUpdateProduct(miaoshaProduct);
        if (StringUtils.isEmpty(miaoshaProduct)) {//无用
            log.error("【创建或更新秒杀商品】操作失败");
            return ResultVO.error(CodeMsgVO.PRODUCT_CREATE_ERROR);
        } else {
            return ResultVO.success(CodeMsgVO.SUCCESS, miaoshaProduct);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO getProductList(HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【查询商品列表】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }
        List<MiaoshaProduct> miaoshaProductList = miaoshaProductService.getMiaoshaProductList();
        if (miaoshaProductList.isEmpty()) {
            log.error("【查询秒杀商品列表】操作失败");
            return ResultVO.error(CodeMsgVO.PRODUCT_LIST_SEARCH_ERROR);
        } else {
            return ResultVO.success(CodeMsgVO.SUCCESS, miaoshaProductList);
        }
    }

    //删除
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultVO deleteMiaoshaProduct(String miaoshaProductId,
                                         HttpServletRequest request, HttpServletResponse response){
        AccessUtil.allowAccess(response);
        if(!checkAdminLogin(request, response)){//方法返回true为有管理员权限
            log.error("【查询商品列表】管理员未登录");
            return ResultVO.error(CodeMsgVO.ADMIN_NOT_LOGIN);
        }
        miaoshaProductService.deleteMiaoshaProduct(miaoshaProductId);
        return ResultVO.success(CodeMsgVO.SUCCESS, null);
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
