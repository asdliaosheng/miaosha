package com.imooc.product.service;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.common.OrderDetail;
import com.imooc.product.dataobject.ProductForm;
import com.imooc.product.dataobject.ProductInfo;

import java.util.List;

/**
 * create by ASheng
 * 2019/3/22 10:20
 */
public interface ProductService {
    List<ProductDTO> getProductList();
    List<ProductDTO> searchProductName(String productName);
    ProductInfo getProductInfo(String productId);
    ProductInfo getProductInfoByProductName(String productName);
    ProductInfo createProduct(ProductForm productForm);
    ProductInfo updateProduct(ProductInfo productInfo);
    List<ProductDTO> getProductList(Integer categoryId);
    List<ProductDTO> getProductList(String productName);
    List<ProductDTO> getProductList(Integer categoryId, String productName);
    String decreaseStock(List<OrderDetail> orderDetailList);
}
