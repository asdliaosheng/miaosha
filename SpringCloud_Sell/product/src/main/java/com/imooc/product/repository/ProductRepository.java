package com.imooc.product.repository;

import com.imooc.product.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/20 17:54
 */
public interface ProductRepository extends JpaRepository<ProductInfo, String> {

    //模糊查询方法二
    //@Query(value = "select t from ProductInfo t where t.productName like '%?1%'")
    List<ProductInfo> findByProductNameLike(String productName);

    ProductInfo findByProductId(String productId);

    List<ProductInfo> findByCategoryId(Integer categoryId);

    List<ProductInfo> findByCategoryIdAndProductNameLike(Integer categoryId, String productName);
}
