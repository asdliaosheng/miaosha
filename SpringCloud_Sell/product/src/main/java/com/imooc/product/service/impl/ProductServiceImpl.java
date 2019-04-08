package com.imooc.product.service.impl;

import com.imooc.product.DTO.ProductDTO;
import com.imooc.product.common.OrderDetail;
import com.imooc.product.converter.ProductInfo2ProductDTOConverter;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.repository.ProductRepository;
import com.imooc.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * create by ASheng
 * 2019/3/22 12:38
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getProductList() {
        List<ProductInfo> productInfoList = productRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(ProductInfo productInfo: productInfoList){
            ProductDTO productDTO = new ProductDTO();
            productDTO = ProductInfo2ProductDTOConverter.converter(productInfo);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public List<ProductDTO> searchProductName(String productName) {
        //模糊查询方法一
        productName = new StringBuilder().append("%").append(productName).append("%").toString();
        List<ProductInfo> productInfoList = productRepository.findByProductNameLike(productName);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(ProductInfo productInfo: productInfoList){
            ProductDTO productDTO = new ProductDTO();
            productDTO = ProductInfo2ProductDTOConverter.converter(productInfo);
            productDTOList.add(productDTO);
        }
        return productDTOList;

    }

    @Override
    public ProductInfo getProductInfo(String productId) {
        ProductInfo productInfo = productRepository.findByProductId(productId);
        return productInfo;
    }

    @Override
    public ProductInfo createOrUpdateProduct(ProductInfo productInfo) {
        return productRepository.save(productInfo);
    }

    @Override
    public List<ProductDTO> getProductList(Integer categoryId) {
        List<ProductInfo> productInfoList = productRepository.findByCategoryId(categoryId);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(ProductInfo productInfo: productInfoList){
            ProductDTO productDTO = new ProductDTO();
            productDTO = ProductInfo2ProductDTOConverter.converter(productInfo);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public List<ProductDTO> getProductList(Integer categoryId, String productName){
        productName = new StringBuilder().append("%").append(productName).append("%").toString();
        List<ProductInfo> productInfoList = productRepository.findByCategoryIdAndProductNameLike(categoryId, productName);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(ProductInfo productInfo: productInfoList){
            ProductDTO productDTO = new ProductDTO();
            productDTO = ProductInfo2ProductDTOConverter.converter(productInfo);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public String decreaseStock(List<OrderDetail> orderDetailList) {
        for(OrderDetail orderDetail: orderDetailList){
            ProductInfo productInfo = productRepository.findByProductId(orderDetail.getProductId());
            //判断库存是否足够
            Integer result = productInfo.getProductStock() - orderDetail.getProductQuantity();
            if(result < 0){
                //若库存不足,打印productId
                log.error(String.format("%s商品库存不足",orderDetail.getProductId()));
                throw new RuntimeException(String.format("%s商品库存不足",orderDetail.getProductId()));
                //return orderDetail.getProductId();
            }
            productInfo.setProductStock(result);
            productInfo.setUpdateTime(new Date());
            productRepository.save(productInfo);
        }
        return null;
    }
}
