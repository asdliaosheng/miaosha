package com.imooc.product.service;

import com.imooc.product.dataobject.ProductCategory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/20 18:50
 */
public interface CategoryService {

    ProductCategory addCategory(Integer categoryId, Integer parentId, String categoryName);

    ProductCategory updateCategoryName(Integer categoryId, String categoryName);

    //查询当前类目的一级子类目
    List<ProductCategory> getCategory(Integer categoryId);

    //查询该类目和所有级子类目
    List<ProductCategory> getDeepCategory(Integer categoryId);
}
