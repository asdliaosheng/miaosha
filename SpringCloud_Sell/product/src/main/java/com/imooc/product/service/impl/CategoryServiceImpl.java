package com.imooc.product.service.impl;

import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.repository.CategoryRepository;
import com.imooc.product.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * create by ASheng
 * 2019/3/20 20:03
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductCategory addCategory(Integer categoryId, Integer parentId, String categoryName) {
        ProductCategory category = new ProductCategory();
        category.setCategoryId(categoryId);
        category.setParentId(parentId);
        category.setCategoryName(categoryName);
        category.setCategoryStatus(1);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return categoryRepository.save(category);
    }

    @Override
    public ProductCategory updateCategoryName(Integer categoryId, String categoryName) {
        Optional optional = categoryRepository.findById(categoryId);
        ProductCategory category = (ProductCategory) optional.get();
        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    //查询当前类目的平级子类目
    @Override
    public List<ProductCategory> getCategory(Integer categoryId) {
        List<ProductCategory> categoryList =  categoryRepository.findByParentId(categoryId);
        return categoryList;
    }

    //查询该类目和所有级子类目
    @Override
    public List<ProductCategory> getDeepCategory(Integer categoryId) {
        List<ProductCategory> categoryList = new ArrayList<>();
        List<Integer> categoryIdList = new ArrayList<>();
        //取自己
        Optional optional = categoryRepository.findById(categoryId);
        ProductCategory category = (ProductCategory) optional.get();
        categoryList.add(category);
        categoryIdList.add(categoryId);
        do{
            List<ProductCategory> nextCategoryList = categoryRepository.findByParentIdIn(categoryIdList);
            categoryList.addAll(nextCategoryList);
            categoryIdList.clear();
            categoryIdList.addAll(nextCategoryList.stream()
                    .map(ProductCategory::getCategoryId)
                    .collect(Collectors.toList()));
        }while(!categoryRepository.findByParentIdIn(categoryIdList).isEmpty());
        return categoryList;
    }
}
