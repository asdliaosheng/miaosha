package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/20 22:06
 */
public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByParentId(Integer parentId);

    List<ProductCategory> findByParentIdIn(List<Integer> parentIdList);
}
